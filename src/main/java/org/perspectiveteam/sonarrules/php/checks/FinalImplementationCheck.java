package org.perspectiveteam.sonarrules.php.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.declaration.ClassDeclarationTree;
import org.sonar.plugins.php.api.tree.lexical.SyntaxToken;
import org.sonar.plugins.php.api.tree.declaration.MethodDeclarationTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

@Rule(
        key = FinalImplementationCheck.KEY,
        name = "Final keyword should not be used in Magento code",
        description = "Final keyword is prohibited in Magento. It decreases extensibility and is not compatible with plugins and proxies.",
        tags = {"convention", "magento2", "extensibility"}
)
public class FinalImplementationCheck extends PHPVisitorCheck {

    public static final String KEY = "FinalImplementation";
    private static final String MESSAGE = "Final keyword is prohibited in Magento.";

    @Override
    public void visitClassDeclaration(ClassDeclarationTree tree) {
        for (SyntaxToken modifier : tree.modifiersToken()) {
            if ("final".equals(modifier.text())) {
                context().newIssue(this, modifier, MESSAGE);
            }
        }

        super.visitClassDeclaration(tree);
    }

    @Override
    public void visitMethodDeclaration(MethodDeclarationTree tree) {
        for (SyntaxToken modifier : tree.modifiers()) {
            if ("final".equals(modifier.text())) {
                context().newIssue(this, modifier, MESSAGE);
            }
        }

        super.visitMethodDeclaration(tree);
    }
}