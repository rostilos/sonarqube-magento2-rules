package org.perspectiveteam.sonarrules.php.checks;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.expression.NewExpressionTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

@Rule(
        key = org.perspectiveteam.sonarrules.php.checks.NoObjectInstantiationInTemplatesCheck.KEY,
        name = org.perspectiveteam.sonarrules.php.checks.NoObjectInstantiationInTemplatesCheck.MESSAGE,
        description = "Templates must not instantiate objects. All objects must be passed from the Block objects.",
        priority = Priority.CRITICAL,
        tags = {"magento2", "performance", "convention", "security"}
)

public class NoObjectInstantiationInTemplatesCheck extends PHPVisitorCheck {

    public static final String KEY = "M6.2.6";
    public static final String MESSAGE = "Templates must not instantiate objects";

    @Override
    public void visitNewExpression(NewExpressionTree tree) {
        if (context().getPhpFile().filename().endsWith(".phtml")) {
            context().newIssue(this, tree, MESSAGE);
        }
        super.visitNewExpression(tree);
    }
}