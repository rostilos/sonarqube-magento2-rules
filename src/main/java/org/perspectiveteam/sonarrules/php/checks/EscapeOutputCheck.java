package org.perspectiveteam.sonarrules.php.checks;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.CompilationUnitTree;
import org.sonar.plugins.php.api.tree.SeparatedList;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.expression.ConditionalExpressionTree;
import org.sonar.plugins.php.api.tree.expression.ExpressionTree;
import org.sonar.plugins.php.api.tree.expression.FunctionCallTree;
import org.sonar.plugins.php.api.tree.lexical.SyntaxTrivia;
import org.sonar.plugins.php.api.tree.statement.EchoTagStatementTree;
import org.sonar.plugins.php.api.tree.statement.ExpressionStatementTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Rule(
        key = org.perspectiveteam.sonarrules.php.checks.EscapeOutputCheck.KEY,
        name = org.perspectiveteam.sonarrules.php.checks.EscapeOutputCheck.MESSAGE,
        description = "All output that could contain user-supplied data must be properly escaped before being rendered in HTML, JavaScript, or other contexts to prevent XSS vulnerabilities.",
        priority = Priority.CRITICAL,
        tags = {"magento2", "security", "xss"}
)

public class EscapeOutputCheck extends PHPVisitorCheck {
    public static final String KEY = "M15.3.1";
    public static final String MESSAGE = "Escape output for display";

    public static final Pattern NO_ESCAPE_COMMENT_PATTERN = Pattern.compile("@(?:noEscape|escapeNotVerified)\\b");
    public static final Pattern SAFE_METHODS_PATTER = Pattern.compile("^(.*?)Html(.*)?$");

    private final Set<Integer> noEscapeLines = new HashSet<>();
    private final Map<Integer, Tree> unescapedEchos = new HashMap<>();

    @Override
    public void visitCompilationUnit(CompilationUnitTree tree) {
        noEscapeLines.clear();
        unescapedEchos.clear();
        super.visitCompilationUnit(tree);

        reportIssues();
    }

    @Override
    public void visitTrivia(SyntaxTrivia trivia) {
        super.visitTrivia(trivia);
        if (isIncludedFile()) {
            String comment = trivia.text();
            Matcher matcher = NO_ESCAPE_COMMENT_PATTERN.matcher(comment);
            if (matcher.find()) {
                noEscapeLines.add(trivia.line());
            }
        }
    }

    @Override
    public void visitEchoTagStatement(EchoTagStatementTree tree) {
        super.visitEchoTagStatement(tree);
        if(isIncludedFile()){
            SeparatedList<ExpressionTree> expressions = tree.expressions();
            ExpressionTree firstExpression = expressions.get(0);
            int startLine = tree.eosToken().line();
            if(isXSSVulnerableOutput(tree.expressions().get(0), firstExpression.toString())){
                unescapedEchos.put(startLine, firstExpression);
            }
        }
    }

    @Override
    public void visitExpressionStatement(ExpressionStatementTree tree) {
        super.visitExpressionStatement(tree);
        if(isIncludedFile() && tree.expression().is(Tree.Kind.FUNCTION_CALL)){
            ExpressionTree callee = ((FunctionCallTree)tree.expression()).callee();
            String functionName = callee.toString();
            int startLine = tree.eosToken().line();
            if ("echo".equals(functionName) && isXSSVulnerableOutput(tree.expression(), tree.toString())) {
                unescapedEchos.put(startLine, callee);
            }
        }
    }

    private boolean isXSSVulnerableOutput(Tree tree, String expressionName) {
        return isNotEscaped(expressionName) && !isAllowedMethod(tree);
    }

    private boolean isNotEscaped(String content) {
        return !content.matches(".*\\$escaper->escape[A-Za-z]+\\(.*\\).*");
    }

    private boolean isAllowedMethod(Tree tree) {
        switch (tree.getKind()) {
            case FUNCTION_CALL:
                String calleeName = ((FunctionCallTree) tree).callee().toString();
                Matcher matcher = SAFE_METHODS_PATTER.matcher(calleeName);
                return matcher.find();
            case CONDITIONAL_EXPRESSION:
                ConditionalExpressionTree conditionalExpressionTree = (ConditionalExpressionTree) tree;
                boolean allowedTrueExpression = isAllowedMethod(Objects.requireNonNull(conditionalExpressionTree.trueExpression()));
                boolean allowedFalseExpression = isAllowedMethod(Objects.requireNonNull(conditionalExpressionTree.falseExpression()));
                return allowedTrueExpression && allowedFalseExpression;
            //Only CAST_EXPRESSION allowed
            case VARIABLE_IDENTIFIER:
                return false;
            default:
                return true;
        }
    }

    private void reportIssues() {
        for (Map.Entry<Integer, Tree> entry : unescapedEchos.entrySet()) {
            int line = entry.getKey();
            if (!noEscapeLines.contains(line)) {
                context().newIssue(this, entry.getValue(), MESSAGE);
            }
        }
    }

    private boolean isIncludedFile() {
        return context().getPhpFile().filename().endsWith(".phtml");
    }
}
