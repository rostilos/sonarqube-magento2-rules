package org.perspectiveteam.sonarrules.php.checks;

import org.perspectiveteam.sonarrules.php.utils.CheckUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.declaration.DeclaredTypeTree;
import org.sonar.plugins.php.api.tree.declaration.ReturnTypeClauseTree;
import org.sonar.plugins.php.api.tree.declaration.MethodDeclarationTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

import java.util.Objects;

@Rule(
        key = ReturnValueCheck.KEY,
        name = ReturnValueCheck.MESSAGE,
        description = "Explicit return types must be declared on all functions to improve code clarity and type safety.",
        priority = Priority.MAJOR,
        tags = {"magento2", "convention", "psr", "maintainability", "php7"}
)

//TODO: Sometimes (considering the peculiarities of M2) it is not quite possible to correctly specify the returned type. It is also necessary to add a possibility to check for return type by annotations
public class ReturnValueCheck extends PHPVisitorCheck {

    public static final String KEY = "ReturnValue";
    public static final String MESSAGE = "Functions must declare explicit return types.";

    @Override
    public void visitMethodDeclaration(MethodDeclarationTree tree) {
        if (!isExceptionMethod(tree)) {
            boolean hasReturnType = checkIfHasReturnType(tree);
            if (!hasReturnType) {
                context().newIssue(this, tree.name(), MESSAGE);
            }
        }

        super.visitMethodDeclaration(tree);
    }

    private boolean checkIfHasReturnType(MethodDeclarationTree tree) {
        if (tree.returnTypeClause() == null) {
            return false;
        }
        ReturnTypeClauseTree functionReturnClause = Objects.requireNonNull(tree.returnTypeClause());
        DeclaredTypeTree returnType = functionReturnClause.declaredType();
        return !returnType.toString().isEmpty();
    }

    private boolean isExceptionMethod(MethodDeclarationTree tree) {
        String methodName = tree.name().toString();
        return CheckUtils.MAGIC_METHODS.contains(methodName);
    }

}
