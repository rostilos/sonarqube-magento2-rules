package org.perspectiveteam.sonarrules.php.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.declaration.MethodDeclarationTree;
import org.sonar.plugins.php.api.tree.expression.FunctionCallTree;
import org.sonar.plugins.php.api.tree.expression.MemberAccessTree;
import org.sonar.plugins.php.api.tree.expression.NameIdentifierTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;
import org.perspectiveteam.sonarrules.php.utils.CheckUtils;

@Rule(
        key = EventsInConstructorsCheck.KEY,
        name = EventsInConstructorsCheck.MESSAGE,
        tags = {"convention"}
)

public class EventsInConstructorsCheck extends PHPVisitorCheck {

    public static final String KEY = "M2.3.2";
    public static final String FORBIDDEN_METHOD = "dispatch";
    public static final String MESSAGE = "Events MUST NOT be triggered in constructors.";

    @Override
    public void visitFunctionCall(FunctionCallTree tree){
        boolean isCalledInConstructor = isCalledInConstructor(tree);
        if(isCalledInConstructor && isEventDispatch(tree)) {
            context().newIssue(this, tree.callee(), MESSAGE);
        }
        super.visitFunctionCall(tree);
    }

    private boolean isCalledInConstructor(Tree tree) {
        if(!tree.is(Tree.Kind.METHOD_DECLARATION)){
            if(tree.getParent() == null){
                return false;
            }
            return isCalledInConstructor(tree.getParent());
        }else{
            return CheckUtils.isConstructorMethodPromotion((MethodDeclarationTree) tree);
        }

    }

    private boolean isEventDispatch(FunctionCallTree functionCallTree) {
        if(functionCallTree.callee().is(Tree.Kind.OBJECT_MEMBER_ACCESS)){
            MemberAccessTree callee = (MemberAccessTree) functionCallTree.callee();
            NameIdentifierTree member = (NameIdentifierTree) callee.member();
            return member.text().equals(FORBIDDEN_METHOD);
        }
        return false;
    }
}
