package org.perspectiveteam.sonarrules.php.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.declaration.ReturnTypeClauseTree;
import org.sonar.plugins.php.api.tree.declaration.TypeTree;
import org.sonar.plugins.php.api.tree.declaration.MethodDeclarationTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Rule(
        key = ReturnTypesOnFunctionsCheck.KEY,
        name = ReturnTypesOnFunctionsCheck.MESSAGE,
        tags = {"convention"}
)

public class ReturnTypesOnFunctionsCheck extends PHPVisitorCheck {

    public static final String KEY = "M1.2";
    public static final String MESSAGE = "Explicit return types MUST BE declared on functions.";
    private static final Set<String> MAGIC_METHODS = Set.of(
            "__construct", "__destruct", "__call", "__callStatic", "__get",
            "__set", "__isset", "__unset", "__sleep", "__wakeup", "__toString", "__invoke",
            "__set_state", "__clone", "__debugInfo");

    @Override
    public void visitMethodDeclaration(MethodDeclarationTree tree){
        if(!isExceptionMethod(tree)){
            boolean hasReturnType = checkIfHasReturnType(tree);
            if(!hasReturnType){
                context().newIssue(this, tree.name(), MESSAGE);
            }
        }

        super.visitMethodDeclaration(tree);
    }

    private boolean checkIfHasReturnType(MethodDeclarationTree tree) {
        if(tree.returnTypeClause() == null){
            return false;
        }
        ReturnTypeClauseTree functionReturnClause = Objects.requireNonNull(tree.returnTypeClause());
        TypeTree returnType = functionReturnClause.type();
        return !returnType.typeName().toString().isEmpty();
    }

    private boolean isExceptionMethod(MethodDeclarationTree tree) {
        String methodName = tree.name().toString();
        return MAGIC_METHODS.contains(methodName);
    }

}
