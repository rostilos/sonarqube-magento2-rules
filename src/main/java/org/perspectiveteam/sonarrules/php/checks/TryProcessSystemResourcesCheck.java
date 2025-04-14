package org.perspectiveteam.sonarrules.php.checks;

import org.sonar.check.Rule;
import org.sonar.check.Priority;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.expression.FunctionCallTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

import java.util.Arrays;
import java.util.List;

@Rule(
        key = TryProcessSystemResourcesCheck.KEY,
        name = "System resource functions should be wrapped in try-catch blocks",
        description = "Functions that use system resources should be properly wrapped in try-catch blocks to ensure proper resource handling and cleanup.",
        priority = Priority.MAJOR,
        tags = {"magento2", "exceptions", "error-handling", "resource-management"}
)
public class TryProcessSystemResourcesCheck extends PHPVisitorCheck {

    public static final String KEY = "TryProcessSystemResources";
    private static final String MESSAGE = "The code must be wrapped with a try block if the method uses system resources.";

    private static final List<String> SYSTEM_RESOURCE_PREFIXES = Arrays.asList(
            "stream_",
            "socket_",
            "file_",
            "ftp_",
            "curl_"
    );

    @Override
    public void visitFunctionCall(FunctionCallTree tree) {
        String functionName = tree.callee().toString();

        if (isSystemResourceFunction(functionName) && !isInsideTryBlock(tree)) {
            context().newIssue(this, tree, MESSAGE);
        }

        super.visitFunctionCall(tree);
    }

    private boolean isSystemResourceFunction(String functionName) {
        return SYSTEM_RESOURCE_PREFIXES.stream()
                .anyMatch(functionName::startsWith);
    }


    private boolean isInsideTryBlock(Tree tree) {
        Tree parent = tree.getParent();

        while (parent != null) {
            if (parent.is(Tree.Kind.TRY_STATEMENT)) {
                return true;
            }
            parent = parent.getParent();
        }

        return false;
    }
}