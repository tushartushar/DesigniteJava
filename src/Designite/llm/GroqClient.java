package Designite.llm;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import Designite.utils.Constants;

public class GroqClient implements LLMClient {

    private static final String API_KEY = System.getenv("GROQ_API_KEY");
    // Using one of the most powerful and fastest models on Groq
    private static final String MODEL_ID = "llama-3.3-70b-versatile";
    private static final String ENDPOINT = "https://api.groq.com/openai/v1/chat/completions";

    @Override
    public LLMResult analyze(String code, String comment) {
        int maxRetries = 2;
        int attempt = 0;

        while (attempt <= maxRetries) {
            try {
                if (!LLMConfig.ENABLE_LLM) return null;
                if (comment == null || comment.trim().length() < 8) return null;

                String prompt = buildPrompt(code, comment);

                HttpURLConnection conn = (HttpURLConnection) new URL(ENDPOINT).openConnection(Proxy.NO_PROXY);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(60000);
                conn.setDoOutput(true);

                String body = buildRequestBody(prompt);
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(body.getBytes(StandardCharsets.UTF_8));
                }

                int statusCode = conn.getResponseCode();
                InputStream is = (statusCode >= 400) ? conn.getErrorStream() : conn.getInputStream();
                String responseBody = readResponse(is);

                if (statusCode >= 400) {
                    if (Constants.DEBUG) {
                        System.out.println("\n================ GROQ ERROR ================");
                        System.out.println("Status: " + statusCode);
                        System.out.println("Response: " + responseBody);
                        System.out.println("============================================\n");
                    }
                    
                    if (statusCode == 429) {
                        System.out.println("Groq Rate limited, waiting 5s...");
                        Thread.sleep(5000);
                        attempt++;
                        continue;
                    }
                    return null;
                }

                String generatedText = extractChatContent(responseBody);
                String cleanJson = JSONUtils.extractJSON(generatedText);
                return parseResult(cleanJson);

            } catch (Exception e) {
                System.out.println("GROQ FAILURE: " + e.getMessage());
                attempt++;
            }
        }
        return null;
    }

    @Override
    public List<LLMResult> analyzeBatch(String context, List<String> comments) throws Exception {
        if (API_KEY == null || API_KEY.isEmpty()) throw new Exception("GROQ_API_KEY is not set.");
        if (comments == null || comments.isEmpty()) return new ArrayList<>();

        String prompt = buildBatchPrompt(context, comments);
        int attempt = 0;

        while (attempt < 3) {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(ENDPOINT).openConnection(Proxy.NO_PROXY);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(90000); // More time for batch
                conn.setDoOutput(true);

                String body = buildRequestBody(prompt);
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(body.getBytes(StandardCharsets.UTF_8));
                }

                int statusCode = conn.getResponseCode();
                InputStream is = (statusCode >= 400) ? conn.getErrorStream() : conn.getInputStream();
                String responseBody = readResponse(is);

                if (statusCode >= 400) {
                    if (statusCode == 429) {
                        System.out.println("Groq Rate limited, waiting 10s...");
                        Thread.sleep(10000);
                        attempt++;
                        continue;
                    }
                    return null;
                }

                String generatedText = extractChatContent(responseBody);
                String cleanJson = JSONUtils.extractJSON(generatedText);
                List<LLMResult> results = parseBatchResult(cleanJson);
                
                if (results != null && !results.isEmpty()) {
                    return results;
                }
                attempt++;
            } catch (Exception e) {
                System.out.println("GROQ BATCH FAILURE: " + e.getMessage());
                attempt++;
                Thread.sleep(2000);
            }
        }
        return null;
    }

    private String buildPrompt(String context, String comment) {
        return buildBatchPrompt(context, java.util.Collections.singletonList(comment));
    }

    private String buildBatchPrompt(String context, List<String> comments) {
        StringBuilder sb = new StringBuilder();
        sb.append("You are an expert code reviewer. Evaluate the quality of the provided comments based on the code context.\n\n");
        sb.append("CONTEXT INFORMATION:\n").append(context).append("\n\n");
        sb.append("LIST OF COMMENTS TO EVALUATE:\n");
        for (int i = 0; i < comments.size(); i++) {
            sb.append(i + 1).append(". \"").append(comments.get(i)).append("\"\n");
        }
        sb.append("\nINSTRUCTIONS:\n");
        sb.append("1. Evaluate each comment's relevance to the ANCHORED CODE (the lines immediately following it in the context).\n");
        sb.append("2. A comment is REDUNDANT if it repeats what the code clearly says.\n");
        sb.append("3. A comment is MISLEADING if it contradicts the code.\n");
        sb.append("4. Return a JSON ARRAY of objects (one for each comment, in the same order).\n\n");
        sb.append("Return JSON ARRAY in this exact format:\n");
        sb.append("[\n");
        sb.append("  {\n");
        sb.append("    \"relevance\": number (0-5),\n");
        sb.append("    \"redundancy\": number (0-5),\n");
        sb.append("    \"clarity\": number (0-5),\n");
        sb.append("    \"usefulness\": number (0-5),\n");
        sb.append("    \"type\": \"Useful/Redundant/Misleading/Noise\",\n");
        sb.append("    \"improvedComment\": \"...\"\n");
        sb.append("  }, ...\n");
        sb.append("]");
        return sb.toString();
    }

    private String buildRequestBody(String prompt) {
        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", prompt);

        JsonArray messages = new JsonArray();
        messages.add(message);

        JsonObject body = new JsonObject();
        body.addProperty("model", MODEL_ID);
        body.add("messages", messages);
        body.addProperty("temperature", 0.1);
        body.addProperty("stream", false);

        return body.toString();
    }

    private String extractChatContent(String responseBody) {
        try {
            JsonObject obj = JsonParser.parseString(responseBody).getAsJsonObject();
            JsonArray choices = obj.getAsJsonArray("choices");
            if (choices != null && choices.size() > 0) {
                return choices.get(0).getAsJsonObject()
                        .getAsJsonObject("message")
                        .get("content").getAsString();
            }
        } catch (Exception e) {
            return responseBody;
        }
        return responseBody;
    }

    private List<LLMResult> parseBatchResult(String json) {
        List<LLMResult> results = new ArrayList<>();
        try {
            com.google.gson.stream.JsonReader reader = new com.google.gson.stream.JsonReader(new java.io.StringReader(json));
            reader.setLenient(true);
            JsonElement element = JsonParser.parseReader(reader);
            
            if (element.isJsonArray()) {
                JsonArray arr = element.getAsJsonArray();
                for (JsonElement e : arr) {
                    results.add(parseSingleObject(e.getAsJsonObject()));
                }
            } else {
                results.add(parseSingleObject(element.getAsJsonObject()));
            }
            return results;
        } catch (Exception e) {
            if (Constants.DEBUG) {
                System.out.println("PARSE BATCH ERROR: " + e.getMessage());
            }
            return null;
        }
    }

    private LLMResult parseResult(String json) {
        try {
            com.google.gson.stream.JsonReader reader = new com.google.gson.stream.JsonReader(new java.io.StringReader(json));
            reader.setLenient(true);
            JsonElement element = JsonParser.parseReader(reader);
            return parseSingleObject(element.getAsJsonObject());
        } catch (Exception e) {
            return null;
        }
    }

    private LLMResult parseSingleObject(JsonObject obj) {
        LLMResult result = new LLMResult();
        result.relevance = obj.has("relevance") ? clampScore(obj.get("relevance").getAsInt()) : 0;
        result.redundancy = obj.has("redundancy") ? clampScore(obj.get("redundancy").getAsInt()) : 0;
        result.clarity = obj.has("clarity") ? clampScore(obj.get("clarity").getAsInt()) : 0;
        result.usefulness = obj.has("usefulness") ? clampScore(obj.get("usefulness").getAsInt()) : 0;
        result.type = obj.has("type") ? obj.get("type").getAsString() : "Neutral";
        result.improvedComment = obj.has("improvedComment") ? obj.get("improvedComment").getAsString() : "";
        return result;
    }

    private int clampScore(int value) {
        return Math.max(0, Math.min(5, value));
    }

    private String readResponse(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
        }
        return sb.toString();
    }
}
