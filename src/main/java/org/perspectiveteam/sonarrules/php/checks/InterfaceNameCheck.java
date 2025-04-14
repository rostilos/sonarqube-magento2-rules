package org.perspectiveteam.sonarrules.php.checks;

import org.sonar.check.Rule;
import org.sonar.check.Priority;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.declaration.ClassDeclarationTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

@Rule(
        key = InterfaceNameCheck.KEY,
        name = "Interface names should end with 'Interface' suffix",
        description = "Interfaces should have names that end with the 'Interface' suffix to clearly identify their purpose.",
        priority = Priority.MINOR,
        tags = {"convention", "magento2"}
)
public class InterfaceNameCheck extends PHPVisitorCheck {

    public static final String KEY = "InterfaceName";
    private static final String MESSAGE = "Interface should have name that ends with \"Interface\" suffix.";
    private static final String INTERFACE_SUFFIX = "Interface";

    @Override
    public void visitClassDeclaration(ClassDeclarationTree tree) {
        if (tree.is(Tree.Kind.INTERFACE_DECLARATION)) {
            String interfaceName = tree.name().text();

            if (!interfaceName.endsWith(INTERFACE_SUFFIX)) {
                context().newIssue(this, tree.name(), MESSAGE);
            }
        }

        super.visitClassDeclaration(tree);
    }
}