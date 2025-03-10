package org.perspectiveteam.sonarrules.php.checks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.expression.FunctionCallTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

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
    public void visitFunctionCall(FunctionCallTree tree) {
        Logger logger = LoggerFactory.getLogger(EventsInConstructorsCheck.class);
        logger.info("MyPlugin is being executed");

        if(tree.callee().toString().contains("dispatch")) {
            context().newIssue(this, tree.callee(), MESSAGE);
        }
        super.visitFunctionCall(tree);
    }

    private boolean isEventDispatch(FunctionCallTree functionCallTree) {
        String callee = functionCallTree.callee().toString();
        return "dispatch".equalsIgnoreCase(callee);
    }
}
