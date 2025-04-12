package org.perspectiveteam.sonarrules.php.checks;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.CompilationUnitTree;
import org.sonar.plugins.php.api.tree.SeparatedList;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.declaration.CallArgumentTree;
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
        key = XssTemplateCheck.KEY,
        name = XssTemplateCheck.MESSAGE,
        description = "All output that could contain user-supplied data must be properly escaped before being rendered in HTML, JavaScript, or other contexts to prevent XSS vulnerabilities.",
        priority = Priority.CRITICAL,
        tags = {"magento2", "security", "xss"}
)

public class XssTemplateCheck extends PHPVisitorCheck {
    public static final String KEY = "XssTemplate";
    public static final String MESSAGE = "Unescaped output detected.";

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
        if (isIncludedFile()) {
            SeparatedList<ExpressionTree> expressions = tree.expressions();
            ExpressionTree firstExpression = expressions.get(0);

            checkXSSVulnerableOutput(firstExpression, firstExpression.toString());
        }
    }

    @Override
    public void visitExpressionStatement(ExpressionStatementTree tree) {
        super.visitExpressionStatement(tree);
        if (isIncludedFile() && tree.expression().is(Tree.Kind.FUNCTION_CALL)) {
            ExpressionTree callee = ((FunctionCallTree) tree.expression()).callee();
            String calleeName = callee.toString();
            if (!"echo".equals(calleeName)) {
                return;
            }

            SeparatedList<CallArgumentTree> callArgument = ((FunctionCallTree) tree.expression()).callArguments();
            Tree firstArgumentExpression = callArgument.get(0).value();
            checkXSSVulnerableOutput(firstArgumentExpression, firstArgumentExpression.toString());
        }
    }


    private boolean isEscaped(String content) {
        return content.matches(".*\\$escaper->escape[A-Za-z]+\\(.*\\).*");
    }

    private void checkXSSVulnerableOutput(Tree tree, String functionName) {
        if (isEscaped(functionName)) {
            return;
        }
        switch (tree.getKind()) {
            case FUNCTION_CALL:
                String calleeName = ((FunctionCallTree) tree).callee().toString();
                Matcher matcher = SAFE_METHODS_PATTER.matcher(calleeName);
                if (!matcher.find()) {
                    int line = ((FunctionCallTree) tree).openParenthesisToken().line();
                    unescapedEchos.put(line, ((FunctionCallTree) tree).callee());
                }
                break;
            case CONDITIONAL_EXPRESSION:
                ConditionalExpressionTree conditionalExpressionTree = (ConditionalExpressionTree) tree;
                if (conditionalExpressionTree.trueExpression() == null || conditionalExpressionTree.falseExpression() == null) {
                    return;
                }
                if (conditionalExpressionTree.trueExpression() != null && conditionalExpressionTree.trueExpression().toString() != null) {
                    checkXSSVulnerableOutput(conditionalExpressionTree.trueExpression(), conditionalExpressionTree.trueExpression().toString());
                }
                checkXSSVulnerableOutput(conditionalExpressionTree.falseExpression(), conditionalExpressionTree.falseExpression().toString());
                break;
            case EXPRESSION_STATEMENT:
                unescapedEchos.put(((ExpressionStatementTree) tree).eosToken().line(), tree);
                break;
            default:
                break;
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
