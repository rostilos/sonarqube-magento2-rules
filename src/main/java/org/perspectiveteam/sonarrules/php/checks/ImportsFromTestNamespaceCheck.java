package org.perspectiveteam.sonarrules.php.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.statement.UseStatementTree;
import org.sonar.plugins.php.api.tree.declaration.NamespaceNameTree;
import org.sonar.plugins.php.api.tree.statement.UseClauseTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;


@Rule(
        key = ImportsFromTestNamespaceCheck.KEY,
        name = "Application modules should not use classes from test modules",
        description = "Detects imports from test namespaces like 'Magento\\Tests'",
        priority = org.sonar.check.Priority.MAJOR,
        tags = {"convention", "magento2"}
)
public class ImportsFromTestNamespaceCheck extends PHPVisitorCheck {
    public static final String KEY = "ImportsFromTestNamespace";
    private static final String PROHIBITED_NAMESPACE = "Magento\\Tests";
    private static final String MAGENTO_NAMESPACE = "Magento";
    private static final String TESTS_SEGMENT = "Tests";
    private static final String MESSAGE = "Application modules should not use classes from test modules.";

    @Override
    public void visitUseStatement(UseStatementTree tree) {
        super.visitUseStatement(tree);
        for (UseClauseTree useClause : tree.clauses()) {
            NamespaceNameTree namespaceName = useClause.namespaceName();
            String fullyQualifiedName = namespaceName.fullyQualifiedName();

            if (fullyQualifiedName.contains(PROHIBITED_NAMESPACE) || fullyQualifiedName.contains("\\" + PROHIBITED_NAMESPACE)) {
                context().newIssue(this, namespaceName, MESSAGE);
                continue;
            }

            if (tree.prefix() != null && !tree.prefix().toString().isEmpty()) {
                String prefix = tree.prefix().toString();
                if (!prefix.replace("\\", "").equals(MAGENTO_NAMESPACE)) {
                    return;
                }
                if (fullyQualifiedName.startsWith(TESTS_SEGMENT) ||
                        fullyQualifiedName.contains("\\" + TESTS_SEGMENT)) {
                    context().newIssue(this, namespaceName, MESSAGE);
                }
            }
        }
    }
}
