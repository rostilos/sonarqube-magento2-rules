package org.perspectiveteam.sonarrules.php.checks;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;
import org.sonar.plugins.php.api.tree.expression.VariableIdentifierTree;

import java.util.Arrays;
import java.util.List;

@Rule(
        key = ThisInTemplatesCheck.KEY,
        name = "The use of $this in templates is forbidden. Using $helper is discouraged.",
        description = "The use of $this in templates is forbidden. Using $helper is discouraged.",
        priority = Priority.MAJOR,
        tags = {"magento2", "convention"}
)

public class ThisInTemplatesCheck extends PHPVisitorCheck {
    public static final String KEY = "MCS0.3";
    private static final List<String> DISCOURAGED_HELPER_VARS = Arrays.asList("$helper", "$_helper");


    @Override
    public void visitVariableIdentifier(VariableIdentifierTree tree) {
        super.visitVariableIdentifier(tree);

        String variableName = tree.variableExpression().text();

        if (!isIncludedFile()) {
            return;
        }
        if ("$this".equals(variableName)) {
            context().newIssue(this, tree, "The use of $this in templates is deprecated. Use $block instead.");
        } else if (DISCOURAGED_HELPER_VARS.contains(variableName)) {
            context().newIssue(this, tree, "The use of helpers in templates is discouraged. Use ViewModel instead.");
        }

    }

    private boolean isIncludedFile() {
        return context().getPhpFile().filename().endsWith(".phtml");
    }
}