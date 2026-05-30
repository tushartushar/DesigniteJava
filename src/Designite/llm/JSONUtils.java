package Designite.llm;

public class JSONUtils {

    public static String extractJSON(String response) {
        int startObj = response.indexOf("{");
        int startArr = response.indexOf("[");
        
        int start = -1;
        int end = -1;

        if (startArr >= 0 && (startObj < 0 || startArr < startObj)) {
            start = startArr;
            end = response.lastIndexOf("]");
        } else {
            start = startObj;
            end = response.lastIndexOf("}");
        }

        if (start >= 0 && end > start) {
            return response.substring(start, end + 1);
        }

        System.out.println("FAILED TO EXTRACT JSON: " + response);
        return response.contains("[") ? "[]" : "{}";
    }
}
