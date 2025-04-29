package org.perspectiveteam.sonarrules.php.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.expression.MemberAccessTree;
import org.sonar.plugins.php.api.tree.expression.FunctionCallTree;
import org.sonar.plugins.php.api.tree.expression.NameIdentifierTree;

import java.util.Arrays;
import java.util.List;

@Rule(
        key = DeprecatedModelMethodCheck.KEY,
        name = "Deprecated Magento 2 model resource methods should not be used",
        priority = org.sonar.check.Priority.MAJOR,
        tags = {"magento2", "deprecated"}
)
public class DeprecatedModelMethodCheck extends PHPVisitorCheck {

    public static final String KEY = "DeprecatedModelMethod";
    private static final String RESOURCE_METHOD = "getResource";
    private static final String MESSAGE = "The use of the deprecated method 'getResource()' to '%s' the data detected.";

    private static final List<String> DEPRECATED_METHODS = Arrays.asList(
            "save", "load", "delete"
    );

    @Override
    public void visitMemberAccess(MemberAccessTree tree) {
        if (tree.member().is(Tree.Kind.NAME_IDENTIFIER) &&
                ((NameIdentifierTree) tree.member()).text().equals(RESOURCE_METHOD)) {

            Tree parent = tree.getParent();

            if (parent.is(Tree.Kind.FUNCTION_CALL)) {
                FunctionCallTree functionCall = (FunctionCallTree) parent;

                processDeprecatedMethodChain(functionCall);
            }
        }

        super.visitMemberAccess(tree);
    }

    private void processDeprecatedMethodChain(FunctionCallTree getResourceCall) {
        Tree parent = getResourceCall.getParent();

        if (parent == null || !parent.is(Tree.Kind.OBJECT_MEMBER_ACCESS)) {
            return;
        }

        MemberAccessTree memberAccess = (MemberAccessTree) parent;

        if (memberAccess.member().is(Tree.Kind.NAME_IDENTIFIER)) {
            String methodName = ((NameIdentifierTree) memberAccess.member()).text();

            if (DEPRECATED_METHODS.contains(methodName)) {
                Tree getResourceTree = getResourceCall.callee();
                if (getResourceTree.is(Tree.Kind.OBJECT_MEMBER_ACCESS)) {
                    MemberAccessTree resourceAccess = (MemberAccessTree) getResourceTree;
                    if (resourceAccess.member().is(Tree.Kind.NAME_IDENTIFIER)) {
                        context().newIssue(this, resourceAccess.member(), String.format(MESSAGE, methodName));
                    }
                }
            }
        }
    }
}