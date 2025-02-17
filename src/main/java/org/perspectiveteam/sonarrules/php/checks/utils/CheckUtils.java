package org.perspectiveteam.sonarrules.php.checks.utils;

import java.util.EnumSet;
import java.util.Locale;

import javax.annotation.Nullable;

import org.perspectiveteam.sonarrules.php.tree.TreeUtils;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.declaration.ClassDeclarationTree;
import org.sonar.plugins.php.api.tree.declaration.NamespaceNameTree;
import org.sonar.plugins.php.api.tree.expression.FunctionCallTree;
import org.sonar.plugins.php.api.tree.expression.MemberAccessTree;
import org.sonar.plugins.php.api.tree.expression.NameIdentifierTree;
import org.sonar.plugins.php.api.tree.Tree.Kind;
import org.sonar.plugins.php.api.tree.expression.VariableIdentifierTree;

public final class CheckUtils {

    /**
     * @return Returns function, static method's or known dynamic method's name,
     * like "f" or "A::f". Warning, use case insensitive comparison of the
     * result.
     */
    @Nullable
    public static String getFunctionName(FunctionCallTree functionCall) {
        return nameOf(functionCall.callee());
    }

    /**
     * @return Returns function, static method's or known dynamic method's lower
     * case name, like "f" or "a::f".
     */
    @Nullable
    public static String getLowerCaseFunctionName(FunctionCallTree functionCall) {
        String name = getFunctionName(functionCall);
        return name != null ? name.toLowerCase(Locale.ROOT) : null;
    }

    /**
     * @return Returns the name of a tree.
     */
    @Nullable
    public static String nameOf(Tree tree) {
        if (tree.is(Tree.Kind.NAMESPACE_NAME)) {
            return ((NamespaceNameTree) tree).qualifiedName();
        } else if (tree.is(Tree.Kind.NAME_IDENTIFIER)) {
            return ((NameIdentifierTree) tree).text();
        } else if (tree.is(Tree.Kind.CLASS_MEMBER_ACCESS) || tree.is(Tree.Kind.OBJECT_MEMBER_ACCESS)) {
            MemberAccessTree memberAccess = (MemberAccessTree) tree;
            String className = nameOf(memberAccess.object());
            String memberName = nameOf(memberAccess.member());
            if (className != null && memberName != null) {
                return className + "::" + memberName;
            }
        } else if (tree.is(Tree.Kind.VARIABLE_IDENTIFIER)) {
            VariableIdentifierTree variableIdentifier = (VariableIdentifierTree) tree;
            if (variableIdentifier.text().equals("$this")) {
                ClassDeclarationTree classDeclaration = (ClassDeclarationTree) TreeUtils.findAncestorWithKind(tree,
                        EnumSet.of(Kind.CLASS_DECLARATION, Kind.TRAIT_DECLARATION));
                if (classDeclaration != null) {
                    return nameOf(classDeclaration.name());
                }
            }
        }
        return null;
    }

    public static boolean isExitExpression(FunctionCallTree functionCallTree) {
        String callee = functionCallTree.callee().toString();
        return "die".equalsIgnoreCase(callee) || "exit".equalsIgnoreCase(callee);
    }
}
