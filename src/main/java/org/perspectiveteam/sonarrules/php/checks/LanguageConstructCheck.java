package org.perspectiveteam.sonarrules.php.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.expression.*;
import org.sonar.plugins.php.api.tree.statement.ExpressionStatementTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

@Rule(
        key = LanguageConstructCheck.KEY,
        name = "Discouraged PHP language constructs should not be used",
        description = "Certain PHP language constructs like exit, echo, print, and backticks are discouraged.",
        priority = org.sonar.check.Priority.MAJOR,
        tags = {"security", "magento2"}
)
public class LanguageConstructCheck extends PHPVisitorCheck {

    public static final String KEY = "LanguageConstruct";
    private static final String ERROR_MESSAGE = "Use of %s language construct is discouraged.";
    private static final String BACKTICK_ERROR_MESSAGE =
            "Incorrect usage of back quote string constant. Back quotes should be always inside strings.";

    @Override
    public void visitExecutionOperator(ExecutionOperatorTree tree) {
        super.visitExecutionOperator(tree);
        if(isExcludedFile()){
            return;
        }
        ExpandableStringLiteralTree literalTree = tree.literal();
        if(!literalTree.openDoubleQuoteToken().text().startsWith("`") || !literalTree.closeDoubleQuoteToken().text().endsWith("`")) {
            return;
        }
        if(literalTree.toString().length() > 1){
            context().newIssue(this, tree, BACKTICK_ERROR_MESSAGE);
        }
    }

    @Override
    public void visitExpressionStatement(ExpressionStatementTree tree) {
        super.visitExpressionStatement(tree);
        if(isExcludedFile()){
            return;
        }
        if (!tree.expression().is(Tree.Kind.FUNCTION_CALL)) {
            return;
        }
        ExpressionTree callee = ((FunctionCallTree) tree.expression()).callee();
        String calleeName = callee.toString();
        if(calleeName.equals("echo") || calleeName.equals("print")) {
            context().newIssue(this, tree, String.format(ERROR_MESSAGE, calleeName));
        }
    }

    private boolean isExcludedFile() {
        return context().getPhpFile().filename().endsWith(".phtml");
    }

}
