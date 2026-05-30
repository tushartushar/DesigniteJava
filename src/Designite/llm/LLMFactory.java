package Designite.llm;

public class LLMFactory {

    public static LLMClient getClient() {
        return new GroqClient();
    }
}
