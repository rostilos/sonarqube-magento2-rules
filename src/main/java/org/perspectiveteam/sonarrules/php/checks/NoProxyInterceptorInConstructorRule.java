package org.perspectiveteam.sonarrules.php.checks;

import org.perspectiveteam.sonarrules.php.utils.CheckUtils;
import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.declaration.MethodDeclarationTree;
import org.sonar.plugins.php.api.tree.declaration.ParameterTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Rule(
        key = org.perspectiveteam.sonarrules.php.checks.NoProxyInterceptorInConstructorRule.KEY,
        name = org.perspectiveteam.sonarrules.php.checks.NoProxyInterceptorInConstructorRule.MESSAGE,
        tags = {"convention"}
)
public class NoProxyInterceptorInConstructorRule extends PHPVisitorCheck {
    public static final String KEY = "M2.5";
    public static final String MESSAGE = "Proxies and interceptors MUST NEVER be explicitly requested in constructors.";
    public static final Pattern PROXY_INTERCEPTOR_PATTERN = Pattern.compile(".*(Proxy|Interceptor)$");

    @Override
    public void visitMethodDeclaration(MethodDeclarationTree tree) {
        if (CheckUtils.isConstructorMethodPromotion(tree)) {
            List<ParameterTree> parameters = tree.parameters().parameters();
            for (ParameterTree param : parameters) {
                if (Objects.requireNonNull(param.declaredType()).toString() != null && PROXY_INTERCEPTOR_PATTERN.matcher(Objects.requireNonNull(param.declaredType()).toString()).matches()) {
                    context().newIssue(this, param, MESSAGE);
                }
            }
        }

        super.visitMethodDeclaration(tree);
    }
}