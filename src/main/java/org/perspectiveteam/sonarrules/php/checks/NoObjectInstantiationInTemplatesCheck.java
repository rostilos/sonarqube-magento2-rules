package org.perspectiveteam.sonarrules.php.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.expression.NewExpressionTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

@Rule(
        key = org.perspectiveteam.sonarrules.php.checks.NoObjectInstantiationInTemplatesCheck.KEY,
        name = org.perspectiveteam.sonarrules.php.checks.NoObjectInstantiationInTemplatesCheck.MESSAGE,
        tags = {"convention"}
)

public class NoObjectInstantiationInTemplatesCheck extends PHPVisitorCheck {

    public static final String KEY = "M6.2.6";
    public static final String MESSAGE = "Templates MUST NOT instantiate objects. All objects MUST be passed from the Block objects";

    @Override
    public void visitNewExpression(NewExpressionTree tree) {
        if (context().getPhpFile().filename().endsWith(".phtml")) {
            context().newIssue(this, tree, MESSAGE);
        }
        super.visitNewExpression(tree);
    }
}