package Designite.SourceModel;

import Designite.llm.LLMClient;
import Designite.llm.LLMConfig;
import Designite.llm.LLMFactory;
import Designite.llm.LLMResult;
import java.util.regex.Pattern;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;

import Designite.utils.models.Vertex;
import Designite.visitors.DirectAceessFieldVisitor;
import Designite.visitors.InstanceOfVisitor;
import Designite.visitors.ThrowVisitor;

public class SM_Method extends SM_SourceItem implements Vertex, Parsable {

	private boolean abstractMethod;
	private boolean finalMethod;
	private boolean staticMethod;
	private boolean isConstructor;
	private boolean throwsException;
	private SM_Type parentType;

	private MethodDeclaration methodDeclaration;

	private List<SM_Method> calledMethodsList = new ArrayList<SM_Method>();
	private List<SM_Parameter> parameterList = new ArrayList<SM_Parameter>();
	private List<SM_LocalVar> localVarList = new ArrayList<SM_LocalVar>();

	// --- Comment quality fields ---
	private String sourceCode; // original file source, set during construction
	private int commentLines       = 0;   // total lines occupied by comments
	private int totalComments      = 0;   // total number of comment blocks
	private int goodComments       = 0;   // comments classified as confirmed good
	private int badComments        = 0;   // comments classified as confirmed bad
	private int unclassifiedComments = 0; // comments that matched no pattern (half-weight bad)
	private String commentQuality = "NO_COMMENTS"; // GOOD | BAD | UNCLASSIFIED | NO_COMMENTS
	private int llmGoodComments = 0;
	private int llmBadComments = 0;
	private int llmNeutralComments = 0;
	private double commentQualityScore = 0.0;
	private String cqiCategory = "UNKNOWN";

	private static final LLMClient llmClient = LLMFactory.getClient();

	private List<MethodInvocation> calledMethods = new ArrayList<MethodInvocation>();
	private List<SM_Type> referencedTypeList = new ArrayList<SM_Type>();
	private List<SimpleName> namesInMethod = new ArrayList<>();
	private List<FieldAccess> thisAccessesInMethod = new ArrayList<>();
	private List<SM_Field> directFieldAccesses = new ArrayList<>();
	private List<Type> typesInInstanceOf = new ArrayList<>();
	private List<SM_Type> smTypesInInstanceOf = new ArrayList<>();

	// =========================================================================
	// Constructor & setup
	// =========================================================================

	public SM_Method(MethodDeclaration methodDeclaration, SM_Type typeObj, String sourceCode) {
		name = methodDeclaration.getName().toString();
		this.parentType = typeObj;
		this.methodDeclaration = methodDeclaration;
		this.sourceCode = sourceCode;
		setMethodInfo(methodDeclaration);
		setAccessModifier(methodDeclaration.getModifiers());
	}

	public void setMethodInfo(MethodDeclaration method) {
		int modifiers = method.getModifiers();
		if (Modifier.isAbstract(modifiers))  abstractMethod = true;
		if (Modifier.isFinal(modifiers))     finalMethod    = true;
		if (Modifier.isStatic(modifiers))    staticMethod   = true;
		if (method.isConstructor())          isConstructor  = true;
	}

	public void setSourceCode(String sourceCode) {
    	this.sourceCode = sourceCode;
	}

	// =========================================================================
	// Getters
	// =========================================================================

	public boolean isAbstract()    { return this.abstractMethod; }
	public boolean isStatic()      { return this.staticMethod; }
	public boolean isFinal()       { return this.finalMethod; }
	public boolean isConstructor() { return this.isConstructor; }
	public SM_Type getParentType() { return parentType; }
	public boolean throwsException() { return throwsException; }
	public boolean hasBody() { return this.methodDeclaration.getBody() != null; }

	public List<SM_Parameter> getParameterList() { return parameterList; }
	public List<SM_LocalVar>  getLocalVarList()  { return localVarList; }

	public int    getCommentLines()         { return commentLines; }
	public int    getTotalComments()        { return totalComments; }
	public int    getGoodComments()         { return goodComments; }
	public int    getBadComments()          { return badComments; }
	public int    getUnclassifiedComments() { return unclassifiedComments; }
	public String getCommentQuality()       { return commentQuality; }
	public int getLlmGoodComments() { return llmGoodComments; }
	public int getLlmBadComments() { return llmBadComments; }
	public int getLlmNeutralComments() { return llmNeutralComments; }
	public double getCommentQualityScore() { return commentQualityScore; }
	public String getCqiCategory() { return cqiCategory; }

	public List<SM_Method>    getCalledMethods()     { return calledMethodsList; }
	public MethodDeclaration  getMethodDeclaration() { return methodDeclaration; }

	public String getMethodBody() {
		return this.hasBody() ? this.getMethodDeclaration().getBody().toString() : "";
	}

	// =========================================================================
	// Comment classification helpers
	//
	// DESIGN NOTE: Each method receives the RAW comment text (markers intact).
	// Each method strips markers internally before analysing content.
	// This ensures Pattern 4 (closing brace) and Pattern 6 (banner) in
	// isRedundantComment() can still see the original '//' characters.
	// =========================================================================

	/**
	 * Strips comment syntax markers from a single collapsed comment string.
	 * Used internally by each classifier method.
	 *
	 * Handles: // single-line, /* block *\/, /** javadoc *\/
	 */
	private String stripMarkers(String comment) {
		return comment
				.replaceAll("^//+\\s*", "")
				.replaceAll("^/\\*+\\s*", "")
				.replaceAll("\\s*\\*+/$", "")
				.replaceAll("\\*\\s*", "")
				.trim();
	}

	// -------------------------------------------------------------------------
	// GOOD: Warning comment
	// -------------------------------------------------------------------------
	private boolean isWarningComment(String comment) {
		String c = stripMarkers(comment).toLowerCase();
		return c.contains("not thread safe")
			|| c.contains("do not")
			|| c.contains("don't")
			|| c.contains("warning")
			|| c.contains("caution")
			|| c.contains("danger")
			|| c.contains("must not")
			|| c.contains("never call")
			|| c.contains("important");
	}

	// -------------------------------------------------------------------------
	// GOOD: Intent / Clarification comment
	// -------------------------------------------------------------------------
	private boolean isIntentOrClarificationComment(String comment) {
		String c = stripMarkers(comment).toLowerCase();
		return c.contains("because")
			|| c.contains("in order to")
			|| c.contains("this is")
			|| c.contains("we need")
			|| c.contains("intent")
			|| c.contains("reason")
			|| c.contains("workaround")
			|| c.contains("hack")
			|| c.contains("note:")
			|| c.contains("fixme")
			|| c.contains("todo")
			|| c.contains("check")
			|| c.contains("ensure")
			|| c.contains("validate");
	}

	// -------------------------------------------------------------------------
	// GOOD: Amplification comment
	// Stresses the importance of something that looks trivial but isn't.
	//
	// Word-count guard (> 4 words) ensures single-word noise like
	// is not falsely classified as a meaningful amplification.
	// -------------------------------------------------------------------------
	private boolean isAmplificationComment(String comment) {
		String stripped = stripMarkers(comment);
		String c = stripped.toLowerCase();
		boolean hasAmplificationKeyword =
				c.contains("important")
			|| c.contains("critical")
			|| c.contains("must")
			|| c.contains("real important")
			|| c.contains("do not remove");
		// Require > 4 words so that a lone "// important" doesn't qualify
		return hasAmplificationKeyword && stripped.split("\\s+").length > 4;
	}

	// -------------------------------------------------------------------------
	// BAD: Commented-out code
	// -------------------------------------------------------------------------
	private boolean isCommentedOutCode(String comment) {
		String c = stripMarkers(comment);
		if (c.isEmpty()) return false;

		int score = 0;

		if (c.endsWith(";"))                                              score += 3;
		if (c.matches(".*\\w+\\s*\\(.*\\)\\s*;?.*"))                    score += 2;
		if (c.matches(".*\\b(return|throw|new|import)\\b.*"))           score += 2;
		if (c.matches(".*\\b(int|String|boolean|void|long|double|List|Map)\\b.*")) score += 2;
		if (c.matches(".*\\b(if|for|while|switch|try|catch)\\s*\\(.*")) score += 3;
		if (c.matches(".*\\w+\\s*=\\s*\\w+.*"))                          score += 1;
		if (c.contains("++") || c.contains("--"))                        score += 1;
		if (c.matches(".*\\b(public|private|protected|static|final)\\b.*")) score += 2;

		return score >= 4;
	}

	// -------------------------------------------------------------------------
	// BAD: Redundant / Noise comment
	// -------------------------------------------------------------------------
	private boolean isRedundantComment(String comment, String methodName) {
		// `c` = stripped + lowercased, used for content pattern matching
		String c = stripMarkers(comment).toLowerCase();

		if (c.isEmpty()) return true; // empty comment is pure noise

		// Pattern 1 - "Default constructor."
		if (c.matches("default constructor\\.?")) return true;

		// Pattern 2 - "The <anything>." - single noun phrase
		if (c.matches("the \\w+\\.?")) return true;

		// Pattern 3 - Javadoc that just restates the method name
		// e.g. "Returns the dayOfMonth" above getDayOfMonth()
		if (methodName != null && !methodName.isEmpty()) {
			String normalizedMethod = methodName.toLowerCase()
					.replaceFirst("^(get|set|is|has)", "");
			if (c.matches("returns?\\s+the\\s+" + Pattern.quote(normalizedMethod) + "\\.?")) return true;
			if (c.matches("sets?\\s+the\\s+"    + Pattern.quote(normalizedMethod) + "\\.?")) return true;
		}

		// Pattern 4 - Closing brace labels: "} //while", "} //main"
		// Uses RAW comment - needs '//' to be present
		if (comment.trim().matches(".*}\\s*//\\s*(end|while|if|for|try|catch|main|\\w+)")) return true;

		// Pattern 5 - Attribution bylines: "Added by X", "Author: X"
		if (c.matches(".*(added by|written by|created by|author:\\s*\\w+|modified by).*")) return true;

		// Pattern 6 - Position markers / banners: "// Actions ////"
		// Uses RAW comment - needs '//' to be present
		if (comment.trim().matches("//\\s*\\w*\\s*[=\\-\\/\\*#]{5,}\\s*")) return true;

		// Pattern 7 - Vague single-word filler or mumbling
		// A single lowercase word that isn't a recognised meaningful keyword
		String[] words = c.split("\\s+");
		if (words.length == 1 && c.matches("[a-z]+")) {
			boolean isMeaningful = c.equals("todo")    || c.equals("fixme")
								|| c.equals("note")    || c.equals("warning")
								|| c.equals("hack")    || c.equals("check")
								|| c.equals("ensure")  || c.equals("validate");
			if (!isMeaningful) return true;
		}

		return false;
	}

	// =========================================================================
	// Core comment counting and classification
	// =========================================================================

	private void countCommentLines() {

		totalComments        = 0;
		goodComments         = 0;
		badComments          = 0;
		unclassifiedComments = 0;
		commentQuality       = "NO_COMMENTS";
		commentLines         = 0;
		llmGoodComments      = 0;
		llmBadComments       = 0;
		llmNeutralComments   = 0;
		commentQualityScore  = 0.0;
		cqiCategory          = "UNKNOWN";

		cqiCategory          = "UNKNOWN";

		if (methodDeclaration == null) return;

		int start = methodDeclaration.getStartPosition();
		int end   = start + methodDeclaration.getLength();
		
		// Fix: Limit boundaries to the method body (between { and }) 
		// to exclude Javadoc and annotations above the method.
		if (methodDeclaration.getBody() != null) {
			start = methodDeclaration.getBody().getStartPosition();
			end = start + methodDeclaration.getBody().getLength();
		} else {
			// If no body (abstract/interface), there are no "inside" comments.
			return;
		}

		if (!(methodDeclaration.getRoot() instanceof org.eclipse.jdt.core.dom.CompilationUnit))
			return;

		org.eclipse.jdt.core.dom.CompilationUnit cu =
				(org.eclipse.jdt.core.dom.CompilationUnit) methodDeclaration.getRoot();

		List<?> comments = cu.getCommentList();
		String source = sourceCode;
		if (source == null) {
			// Fallback: use the AST root's toString if and only if it matches the length
			// otherwise we risk extracting the wrong text.
			source = cu.toString();
			if (source.length() < end) {
				return; // Cannot safely extract comments without original source
			}
		}
		// Step 1: Collect comments inside the method body
		List<org.eclipse.jdt.core.dom.Comment> internalComments = new ArrayList<>();
		for (Object obj : comments) {
			org.eclipse.jdt.core.dom.Comment comment = (org.eclipse.jdt.core.dom.Comment) obj;
			int cStart = comment.getStartPosition();
			int cEnd   = cStart + comment.getLength();
			if (cStart >= start && cEnd <= end) {
				internalComments.add(comment);
			}
		}

		// Step 2: Merge consecutive line comments
		List<String> mergedCommentsText = new ArrayList<>();
		List<Integer> mergedCommentLineCounts = new ArrayList<>();
		List<Integer> mergedCommentEndPositions = new ArrayList<>();
		
		for (int i = 0; i < internalComments.size(); i++) {
			org.eclipse.jdt.core.dom.Comment current = internalComments.get(i);
			int cStart = current.getStartPosition();
			int cEnd = cStart + current.getLength();
			int startLine = cu.getLineNumber(cStart);
			int endLine = cu.getLineNumber(cEnd);
			
			// Extract raw text
			String text = source.substring(cStart, cEnd).replaceAll("\\r", "").trim();
			int lines = (endLine - startLine + 1);

			// Look ahead: if next comment is a LineComment on the next line, merge it
			while (current.isLineComment() && i + 1 < internalComments.size()) {
				org.eclipse.jdt.core.dom.Comment next = internalComments.get(i + 1);
				if (!next.isLineComment()) break;
				
				int nextStartLine = cu.getLineNumber(next.getStartPosition());
				int currentEndLine = cu.getLineNumber(current.getStartPosition() + current.getLength());
				
				// Merge if it's on the next line or same line (consecutive)
				if (nextStartLine <= currentEndLine + 1) {
					text += " " + source.substring(next.getStartPosition(), next.getStartPosition() + next.getLength())
							.replaceAll("\\r", "").trim();
					lines += (cu.getLineNumber(next.getStartPosition() + next.getLength()) - nextStartLine + 1);
					i++;
					current = next;
				} else {
					break;
				}
			}
			mergedCommentsText.add(text.replaceAll("\\n", " ").trim());
			mergedCommentLineCounts.add(lines);
			mergedCommentEndPositions.add(current.getStartPosition() + current.getLength());
		}

		// Step 3: Identify comments for LLM analysis
		List<String> commentsToAnalyze = new ArrayList<>();
		List<Integer> commentIndices = new ArrayList<>();
		
		for (int i = 0; i < mergedCommentsText.size(); i++) {
			String commentText = mergedCommentsText.get(i);
			commentLines += mergedCommentLineCounts.get(i);
			totalComments++;

			boolean skipLLM = false;
			boolean classified = false;

			// Heuristic Classification
			if (!classified && isWarningComment(commentText)) { goodComments++; classified = true; }
			if (!classified && isIntentOrClarificationComment(commentText)) { goodComments++; classified = true; }
			if (!classified && isAmplificationComment(commentText)) { goodComments++; classified = true; }
			if (!classified && isCommentedOutCode(commentText)) { badComments++; classified = true; }
			if (!classified && isRedundantComment(commentText, name)) { badComments++; classified = true; }

			if (!classified) unclassifiedComments++;

			// Skip Filters
			if (commentText.length() < 5) skipLLM = true;
			if (isCommentedOutCode(commentText)) skipLLM = true;
			if (commentText.toLowerCase().contains("getter") || commentText.toLowerCase().contains("setter")) skipLLM = true;
			
			if (LLMConfig.ENABLE_LLM && !skipLLM) {
				commentsToAnalyze.add(commentText);
				commentIndices.add(i);
			} else {
				// Automatic categorization for skipped comments
				if (isCommentedOutCode(commentText) || isRedundantComment(commentText, name) || commentText.length() < 5) {
					llmBadComments++;
					commentQualityScore += 1.0;
				} else {
					llmNeutralComments++;
					commentQualityScore += 2.5;
				}
			}
		}

		// Step 4: Execute LLM Analysis in Batches
		int llmCallCount = 0;
		int MAX_LLM_CALLS = 15; 
		if (!commentsToAnalyze.isEmpty() && LLMConfig.ENABLE_LLM) {
			int batchSize = 5; 
			for (int i = 0; i < commentsToAnalyze.size(); i += batchSize) {
				if (llmCallCount >= MAX_LLM_CALLS) break;
				
				int endIdx = Math.min(i + batchSize, commentsToAnalyze.size());
				List<String> batch = commentsToAnalyze.subList(i, endIdx);
				
				try {
					String context = getMethodBody();
					if (context.length() > 1000) context = context.substring(0, 1000);
					
					List<LLMResult> results = llmClient.analyzeBatch(context, batch);
					llmCallCount++;

					if (results != null) {
						for (LLMResult result : results) {
							double score = (result.relevance + result.clarity + result.usefulness + (5 - result.redundancy)) / 4.0;
							if (score < 0) score = 0;
							if (score > 5) score = 5;

							if (score >= 3.0) llmGoodComments++;
							else if (score >= 2.0) llmNeutralComments++;
							else llmBadComments++;

							commentQualityScore += score;
						}
					} else {
						// Failover for failed batch
						for (int k = 0; k < batch.size(); k++) {
							llmNeutralComments++;
							commentQualityScore += 2.5;
						}
					}
				} catch (Exception e) {
					System.out.println("Batch error: " + e.getMessage());
				}
			}
		}

		if (totalComments > 0) {
			commentQualityScore = commentQualityScore / totalComments;
			cqiCategory = classifyCQI(commentQualityScore);
		}

		// ----------------------------------------------------------------
		// Quality label calculation
		//
		// Effective bad score = confirmed bad + 0.5 * unclassified
		// This gives unclassified comments lower weight than confirmed bad.
		//
		// Ratio = goodComments / totalComments (0.0 - 1.0)
		// Threshold >= 0.6 -> GOOD, else BAD.
		// ----------------------------------------------------------------
		if (totalComments == 0) {
			commentQuality = "NO_COMMENTS";
		} else if (goodComments == 0 && badComments == 0 && unclassifiedComments > 0) {
			commentQuality = "UNCLASSIFIED";
		} else {
			double effectiveBad  = badComments + (0.5 * unclassifiedComments);
			double effectiveGood = totalComments - effectiveBad;
			double ratio         = effectiveGood / totalComments;
			commentQuality = (ratio >= 0.6) ? "GOOD" : "BAD";
		}
	}

	private String classifyCQI(double cqi) {
		if (cqi >= 4.0) return "EXCELLENT";
		if (cqi >= 3.0) return "GOOD";
		if (cqi >= 2.0) return "WEAK";
		return "POOR";
	}

	// =========================================================================
	// Parse / Resolve
	// =========================================================================

	private void prepareCalledMethodsList() {
		MethodInvVisitor invVisitor = new MethodInvVisitor(methodDeclaration);
		methodDeclaration.accept(invVisitor);
		List<MethodInvocation> invList = invVisitor.getCalledMethods();
		if (invList.size() > 0) calledMethods.addAll(invList);
	}

	private void prepareInstanceOfVisitorList() {
		InstanceOfVisitor instanceOfVisitor = new InstanceOfVisitor();
		methodDeclaration.accept(instanceOfVisitor);
		List<Type> instanceOfTypes = instanceOfVisitor.getTypesInInstanceOf();
		if (instanceOfTypes.size() > 0) typesInInstanceOf.addAll(instanceOfTypes);
	}

	private void prepareParametersList(SingleVariableDeclaration var) {
		VariableVisitor parameterVisitor = new VariableVisitor(this);
		var.accept(parameterVisitor);
		List<SM_Parameter> pList = parameterVisitor.getParameterList();
		if (pList.size() > 0) parameterList.addAll(pList);
	}

	private void prepareLocalVarList() {
		LocalVarVisitor localVarVisitor = new LocalVarVisitor(this);
		methodDeclaration.accept(localVarVisitor);
		List<SM_LocalVar> lList = localVarVisitor.getLocalVarList();
		if (lList.size() > 0) localVarList.addAll(lList);
	}

	//TODO: Modularize parser with private functions
	@Override
	public void parse() {
		countCommentLines();
		prepareCalledMethodsList();

		List<SingleVariableDeclaration> variableList = methodDeclaration.parameters();
		for (SingleVariableDeclaration var : variableList) {
			prepareParametersList(var);
		}

		prepareLocalVarList();

		DirectAceessFieldVisitor directAceessFieldVisitor = new DirectAceessFieldVisitor();
		methodDeclaration.accept(directAceessFieldVisitor);
		List<SimpleName> names = directAceessFieldVisitor.getNames();
		List<FieldAccess> thisAccesses = directAceessFieldVisitor.getThisAccesses();
		if (names.size() > 0)       namesInMethod.addAll(names);
		if (thisAccesses.size() > 0) thisAccessesInMethod.addAll(thisAccesses);

		prepareInstanceOfVisitorList();

		ThrowVisitor throwVisitor = new ThrowVisitor();
		methodDeclaration.accept(throwVisitor);
		throwsException = throwVisitor.throwsException();
	}

	@Override
	public void resolve() {
		for (SM_Parameter param : parameterList) param.resolve();
		for (SM_LocalVar localVar : localVarList) localVar.resolve();
		calledMethodsList = (new Resolver()).inferCalledMethods(calledMethods, parentType);
		setReferencedTypes();
		setDirectFieldAccesses();
		setSMTypesInInstanceOf();
	}

	// =========================================================================
	// Debug log
	// =========================================================================

	@Override
	public void printDebugLog(PrintWriter writer) {
		print(writer, "\t\tMethod: "      + name);
		print(writer, "\t\tParent type: " + this.getParentType().getName());
		print(writer, "\t\tConstructor: " + isConstructor);
		print(writer, "\t\tReturns: "     + methodDeclaration.getReturnType2());
		print(writer, "\t\tAccess: "      + accessModifier);
		print(writer, "\t\tAbstract: "    + abstractMethod);
		print(writer, "\t\tFinal: "       + finalMethod);
		print(writer, "\t\tStatic: "      + staticMethod);
		print(writer, "\t\tCommentLines: "        + commentLines);
		print(writer, "\t\tTotalComments: "       + totalComments);
		print(writer, "\t\tGoodComments: "        + goodComments);
		print(writer, "\t\tBadComments: "         + badComments);
		print(writer, "\t\tUnclassifiedComments: "+ unclassifiedComments);
		print(writer, "\t\tCommentQuality: "      + commentQuality);
		print(writer, "\t\tCalled methods: ");
		for (SM_Method method : getCalledMethods())
			print(writer, "\t\t\t" + method.getName());
		for (SM_Parameter param : parameterList)
			param.printDebugLog(writer);
		for (SM_LocalVar var : localVarList)
			var.printDebugLog(writer);
		print(writer, "\t\t----");
	}

	// =========================================================================
	// Private helpers - type resolution, field access
	// =========================================================================

	private void setReferencedTypes() {
		for (SM_Parameter param : parameterList)
			if (!param.isPrimitiveType()) addunique(param.getType());
		for (SM_LocalVar localVar : localVarList)
			if (!localVar.isPrimitiveType()) addunique(localVar.getType());
		for (SM_Method methodCall : calledMethodsList)
			if (methodCall.isStatic()) addunique(methodCall.getParentType());
	}

	private void setDirectFieldAccesses() {
		for (FieldAccess thisAccess : thisAccessesInMethod) {
			SM_Field sameField = getFieldWithSameName(thisAccess.getName().toString());
			if (sameField != null && !directFieldAccesses.contains(sameField))
				directFieldAccesses.add(sameField);
		}
		for (SimpleName name : namesInMethod) {
			if (!existsAsNameInLocalVars(name.toString())) {
				SM_Field sameField = getFieldWithSameName(name.toString());
				if (sameField != null && !directFieldAccesses.contains(sameField))
					directFieldAccesses.add(sameField);
			}
		}
	}

	private boolean existsAsNameInLocalVars(String name) {
		for (SM_LocalVar localVar : localVarList)
			if (name.equals(localVar.getName())) return true;
		return false;
	}

	private SM_Field getFieldWithSameName(String name) {
		for (SM_Field field : parentType.getFieldList())
			if (name.equals(field.getName())) return field;
		return null;
	}

	private void setSMTypesInInstanceOf() {
		Resolver resolver = new Resolver();
		for (Type type : typesInInstanceOf) {
			SM_Type smType = resolver.resolveType(type, parentType.getParentPkg().getParentProject());
			if (smType != null && !smTypesInInstanceOf.contains(smType))
				smTypesInInstanceOf.add(smType);
		}
	}

	private void addunique(SM_Type variableType) {
		if (!referencedTypeList.contains(variableType))
			referencedTypeList.add(variableType);
	}

	private String extractAnchoredCode(String source, int start, int methodEnd) {
		if (start >= methodEnd || start >= source.length()) return "";
		
		int end = start;
		int linesCount = 0;
		int maxLines = 3;
		
		while (end < methodEnd && end < source.length() && linesCount < maxLines) {
			if (source.charAt(end) == '\n') {
				linesCount++;
			}
			end++;
		}
		
		return source.substring(start, end).trim();
	}

	public List<SM_Type>  getReferencedTypeList()   { return referencedTypeList; }
	public List<SM_Field> getDirectFieldAccesses()  { return directFieldAccesses; }
	public List<SM_Type>  getSMTypesInInstanceOf()  { return smTypesInInstanceOf; }
}
