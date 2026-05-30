package Designite.metrics;

import Designite.SourceModel.SM_Method;
import Designite.SourceModel.SM_Type;
import Designite.visitors.MethodControlFlowVisitor;

import java.util.List;

public class MethodMetricsExtractor implements MetricExtractor{

    private SM_Method method;
    private MethodMetrics methodMetrics;

    public MethodMetricsExtractor(SM_Method method) {
        this.method = method;
    }

    @Override
    public MethodMetrics extractMetrics() {
        methodMetrics = new MethodMetrics();
        extractNumOfParametersMetrics();
        extractCyclomaticComplexity();
        extractNumberOfLines();
        extractCommentMetrics();
        methodMetrics.setMethod(method);
        return methodMetrics;
    }


    private void extractNumOfParametersMetrics() {
       methodMetrics.setNumOfParameters(method.getParameterList().size());
    }

    private void extractCyclomaticComplexity() {
        methodMetrics.setCyclomaticComplexity(calculateCyclomaticComplexity());
    }

    private int calculateCyclomaticComplexity() {
        MethodControlFlowVisitor visitor = new MethodControlFlowVisitor();
        method.getMethodDeclaration().accept(visitor);
        return visitor.getNumOfIfStatements()
                + visitor.getNumOfSwitchCaseStatementsWitoutDefault()
                + visitor.getNumOfForStatements()
                + visitor.getNumOfWhileStatements()
                + visitor.getNumOfDoStatements()
                + visitor.getNumOfForeachStatements()
                + 1;
    }

    private void extractNumberOfLines() {
        if (methodHasBody()) {
            String body = method.getMethodDeclaration().getBody().toString();
            int length = body.length();
//			long newlines = body.lines().count();
            methodMetrics.setNumOfLines(length - body.replace("\n", "").length());
        }
    }

    private void extractCommentMetrics() {
        methodMetrics.setCommentLines(method.getCommentLines());
        methodMetrics.setTotalComments(method.getTotalComments());
        methodMetrics.setGoodComments(method.getGoodComments());
        methodMetrics.setBadComments(method.getBadComments());
        methodMetrics.setUnclassifiedComments(method.getUnclassifiedComments());
        methodMetrics.setCommentQuality(method.getCommentQuality());
        methodMetrics.setLlmGoodComments(method.getLlmGoodComments());
        methodMetrics.setLlmBadComments(method.getLlmBadComments());
        methodMetrics.setLlmNeutralComments(method.getLlmNeutralComments());
        methodMetrics.setCommentQualityScore(method.getCommentQualityScore());
        methodMetrics.setCqiCategory(method.getCqiCategory());
    }


    private boolean methodHasBody() {
        return method.getMethodDeclaration().getBody() != null;
    }




}
