package org.perspectiveteam.sonarrules.php.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;
import org.sonar.plugins.php.api.tree.declaration.MethodDeclarationTree;
import org.sonar.plugins.php.api.tree.expression.MemberAccessTree;

import java.util.Arrays;
import java.util.List;


@Rule(
        key = ObsoleteConnectionCheck.KEY,
        name = "Obsolete connection methods should not be used",
        description = "Obsolete connection methods should not be used. Use getConnection method instead.",
        tags = {"convention", "obsolete", "magento2"}
)
public class ObsoleteConnectionCheck extends PHPVisitorCheck {
    public static final String KEY = "ObsoleteConnection";
    private static final List<String> OBSOLETE_METHODS = Arrays.asList(
            "_getReadConnection",
            "_getWriteConnection",
            "_getReadAdapter",
            "_getWriteAdapter",
            "getReadConnection",
            "getWriteConnection",
            "getReadAdapter",
            "getWriteAdapter"
    );

    @Override
    public void visitMemberAccess(MemberAccessTree tree) {
        String memberName = tree.member().toString();

        if (OBSOLETE_METHODS.contains(memberName)) {
            context().newIssue(this, tree.member(),
                    String.format("Contains obsolete method: %s. Please use getConnection method instead.", memberName));
        }

        super.visitMemberAccess(tree);
    }

    @Override
    public void visitMethodDeclaration(MethodDeclarationTree tree) {
        String methodName = tree.name().text();

        if (OBSOLETE_METHODS.contains(methodName)) {
            context().newIssue(this, tree.name(),
                    String.format("Contains obsolete method: %s. Please use getConnection method instead.", methodName));
        }

        super.visitMethodDeclaration(tree);
    }
}