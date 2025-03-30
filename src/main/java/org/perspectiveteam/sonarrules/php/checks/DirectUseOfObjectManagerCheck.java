package org.perspectiveteam.sonarrules.php.checks;

import org.perspectiveteam.sonarrules.php.utils.CheckUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.declaration.ClassDeclarationTree;
import org.sonar.plugins.php.api.tree.declaration.MethodDeclarationTree;
import org.sonar.plugins.php.api.tree.declaration.NamespaceNameTree;
import org.sonar.plugins.php.api.tree.expression.*;
import org.sonar.plugins.php.api.tree.statement.UseClauseTree;
import org.sonar.plugins.php.api.tree.statement.UseStatementTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

@Rule(
        key = org.perspectiveteam.sonarrules.php.checks.DirectUseOfObjectManagerCheck.KEY,
        name = org.perspectiveteam.sonarrules.php.checks.DirectUseOfObjectManagerCheck.MESSAGE,
        description = "The application prohibits the direct use of the ObjectManager in your code because it hides the real dependencies of a class",
        priority = Priority.MAJOR,
        tags = {"magento2", "convention", "maintainability"}
)
public class DirectUseOfObjectManagerCheck extends PHPVisitorCheck {
    public static final String KEY = "MCS0.2";
    public static final String MESSAGE = "The application prohibits the direct use of the ObjectManager in your code.";

    private static final Pattern ALLOWED_CLASS_PATTERN = Pattern.compile("(?i).*(Factory|Proxy|Interceptor)$");

    private static final Set<String> OBJECT_MANAGER_CLASSES = new HashSet<>();
    private static final Set<String> OBJECT_MANAGER_ALIASES = new HashSet<>();

    private boolean isAllowedClass = false;
    private boolean isConstructorMethod = false;

    static {
        OBJECT_MANAGER_CLASSES.add("\\Magento\\Framework\\ObjectManager\\ObjectManager");
        OBJECT_MANAGER_CLASSES.add("\\Magento\\Framework\\App\\ObjectManager");
        OBJECT_MANAGER_CLASSES.add("\\Magento\\Framework\\ObjectManagerInterface");
    }

    @Override
    public void visitClassDeclaration(ClassDeclarationTree tree) {
        String className = tree.name().text();
        isAllowedClass = ALLOWED_CLASS_PATTERN.matcher(className).matches();

        super.visitClassDeclaration(tree);

        isAllowedClass = false;
    }

    @Override
    public void visitUseStatement(UseStatementTree tree) {
        for (UseClauseTree useClause : tree.clauses()) {
            String fullName = useClause.namespaceName().fullName();

            for (String objectManagerClass : OBJECT_MANAGER_CLASSES) {
                if (fullName.equals(objectManagerClass.substring(1))) {
                    String alias = useClause.alias() != null
                            ? Objects.requireNonNull(useClause.alias()).text()
                            : useClause.namespaceName().name().text();
                    OBJECT_MANAGER_ALIASES.add(alias);
                }
            }
        }

        super.visitUseStatement(tree);
    }

    @Override
    public void visitMethodDeclaration(MethodDeclarationTree tree) {
        isConstructorMethod = CheckUtils.isConstructorMethodPromotion(tree);

        super.visitMethodDeclaration(tree);

        isConstructorMethod = false;
    }

    @Override
    public void visitFunctionCall(FunctionCallTree tree) {
        super.visitFunctionCall(tree);

        boolean isAllowedUsage = isAllowedUsage();
        if (isAllowedUsage) {
            return;
        }

        if (tree.callee().is(Tree.Kind.OBJECT_MEMBER_ACCESS) || tree.callee().is(Tree.Kind.CLASS_MEMBER_ACCESS)) {
            MemberAccessTree memberAccess = (MemberAccessTree) tree.callee();

            String variableName;
            switch (memberAccess.object().getKind()) {
                case VARIABLE_IDENTIFIER:
                    variableName = ((VariableIdentifierTree) memberAccess.object()).text();
                    break;
                case CLASS_MEMBER_ACCESS:
                case OBJECT_MEMBER_ACCESS:
                    variableName = ((MemberAccessTree) memberAccess.object()).member().toString();
                    break;
                default:
                    return;
            }

            String methodName = null;
            if (memberAccess.member().is(Tree.Kind.NAME_IDENTIFIER)) {
                methodName = ((NameIdentifierTree) memberAccess.member()).text();
            }

            if (("get".equals(methodName) || "create".equals(methodName)) &&
                    (variableName.contains("objectManager") ||
                            variableName.equals("_objectManager") ||
                            variableName.equals("$objectManager"))) {

                context().newIssue(this, tree, MESSAGE);
            }
        }
    }

    @Override
    public void visitMemberAccess(MemberAccessTree tree) {
        super.visitMemberAccess(tree);

        boolean isAllowedUsage = isAllowedUsage();
        if (isAllowedUsage) {
            return;
        }
        String memberName = getMemberName(tree.member());
        String objectName = getObjectName(tree.object());

        if (objectName != null && memberName != null) {
            checkIfDirectUsageOfObjectManager(objectName, memberName, tree);
        }
    }


    @Nullable
    private static String getObjectName(Tree objectTree) {
        return objectTree.is(Tree.Kind.NAMESPACE_NAME) ? ((NamespaceNameTree) objectTree).fullName() : null;
    }

    @Nullable
    private static String getMemberName(Tree memberTree) {
        return memberTree.is(Tree.Kind.NAME_IDENTIFIER) ? ((NameIdentifierTree) memberTree).text() : null;
    }

    public void checkIfDirectUsageOfObjectManager(String objectName, String memberName, Tree contextTree) {
        if ((OBJECT_MANAGER_ALIASES.contains(objectName) || objectName.contains("ObjectManager")) && memberName.equals("getInstance")) {
            context().newIssue(this, contextTree, MESSAGE);
        }
    }

    public boolean isAllowedUsage() {
        return isAllowedClass || isConstructorMethod;
    }
}
