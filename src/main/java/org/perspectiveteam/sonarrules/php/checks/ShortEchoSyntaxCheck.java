package org.perspectiveteam.sonarrules.php.checks;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.ScriptTree;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.expression.ExpressionTree;
import org.sonar.plugins.php.api.tree.expression.FunctionCallTree;
import org.sonar.plugins.php.api.tree.statement.ExpressionStatementTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

@Rule(
        key = ShortEchoSyntaxCheck.KEY,
        name = "Short echo tag syntax must be used.",
        description = "Magento2 recommends using the short echo syntax in .phtml files",
        priority = Priority.MINOR,
        tags = {"magento2", "best-practices"}
)

public class ShortEchoSyntaxCheck extends PHPVisitorCheck {
    public static final String KEY = "ShortEchoSyntax";
    public static final String MESSAGE = "Short echo tag syntax must be used; expected \"<?=\" but found \"<?php echo\"";

    @Override
    public void visitExpressionStatement(ExpressionStatementTree tree) {
        super.visitExpressionStatement(tree);
        if (!isIncludedFile() || !tree.expression().is(Tree.Kind.FUNCTION_CALL)) {
            return;
        }
        if (tree.getParent() == null || tree.getParent().getKind() != Tree.Kind.SCRIPT) {
            return;
        }

        ScriptTree parent = (ScriptTree) tree.getParent();
        ExpressionTree callee = ((FunctionCallTree) tree.expression()).callee();
        String calleeName = callee.toString();
        boolean isOneLineEcho = isOneLineEcho(parent, calleeName);

        if (isOneLineEcho) {
            context().newIssue(this, tree, MESSAGE);
        }
    }

    private boolean isOneLineEcho(ScriptTree parent, String calleeName) {
        return calleeName.equals("echo") && parent.fileOpeningTagToken().text().equals("<?php");
    }

    private boolean isIncludedFile() {
        return context().getPhpFile().filename().endsWith(".phtml");
    }
}
