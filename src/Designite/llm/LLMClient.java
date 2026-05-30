package Designite.llm;

import java.util.List;

public interface LLMClient {
    LLMResult analyze(String code, String comment) throws Exception;
    List<LLMResult> analyzeBatch(String context, List<String> comments) throws Exception;
}
