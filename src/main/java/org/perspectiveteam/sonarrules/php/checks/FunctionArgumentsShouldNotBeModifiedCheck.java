package org.perspectiveteam.sonarrules.php.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.SeparatedList;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.declaration.MethodDeclarationTree;
import org.sonar.plugins.php.api.tree.declaration.ParameterTree;
import org.sonar.plugins.php.api.tree.expression.AssignmentExpressionTree;
import org.sonar.plugins.php.api.tree.expression.ExpressionTree;
import org.sonar.plugins.php.api.tree.expression.IdentifierTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

@Rule(
        key = org.perspectiveteam.sonarrules.php.checks.FunctionArgumentsShouldNotBeModifiedCheck.KEY,
        name = org.perspectiveteam.sonarrules.php.checks.FunctionArgumentsShouldNotBeModifiedCheck.MESSAGE,
        tags = {"convention"}
)

public class FunctionArgumentsShouldNotBeModifiedCheck extends PHPVisitorCheck {

    public static final String KEY = "M1.1";
    public static final String MESSAGE = "Function arguments SHOULD NOT be modified.";

    @Override
    public void visitAssignmentExpression(AssignmentExpressionTree tree) {
        ExpressionTree variable = tree.variable();
        if(variable instanceof IdentifierTree){
            IdentifierTree identifier = (IdentifierTree) variable;
            Tree parent = identifier.getParent();

            while (parent != null) {
                if (parent.is(Tree.Kind.METHOD_DECLARATION)) {
                    MethodDeclarationTree methodDeclarationTree = (MethodDeclarationTree) parent;
                    String identifierName = identifier.token().toString();
                    boolean isArgModified = checkIfFunctionArgumentIsModified(identifier, methodDeclarationTree);
                    if(isArgModified){
                        context().newIssue(this, tree.variable(), "Function argument '" + identifierName + "' should not be modified.");
                    }
                    break;
                }
                parent = parent.getParent();
            }
        }
        super.visitAssignmentExpression(tree);
    }

    private boolean checkIfFunctionArgumentIsModified(IdentifierTree identifier, MethodDeclarationTree methodDeclarationTree) {
        SeparatedList<ParameterTree> parameters = methodDeclarationTree.parameters().parameters();

        for (ParameterTree param : parameters) {
            if (param.referenceToken() != null) {
                param.variableIdentifier();
                return param.variableIdentifier().toString().equals(identifier.token().toString());
            }
        }
        return false;
    }
}



