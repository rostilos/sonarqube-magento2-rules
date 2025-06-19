/*
 * Copyright (C) 2025 Rostislav Suleimanov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.rostilos.sonarrules.php.checks;

import org.rostilos.sonarrules.php.utils.CheckUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.declaration.MethodDeclarationTree;
import org.sonar.plugins.php.api.tree.declaration.ParameterTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

import java.util.List;
import java.util.regex.Pattern;

@Rule(
        key = DiscouragedDependenciesCheck.KEY,
        name = DiscouragedDependenciesCheck.MESSAGE,
        description = "Proxies and interceptors must never be explicitly requested in constructors. Use dependency injection with the class type instead.",
        priority = Priority.BLOCKER,
        tags = {"magento2", "dependency-injection", "class", "bug"}
)
public class DiscouragedDependenciesCheck extends PHPVisitorCheck {
    public static final String KEY = "DiscouragedDependencies";
    public static final String MESSAGE = "No explicit proxy/interceptor requests in constructors.";
    public static final Pattern PROXY_INTERCEPTOR_PATTERN = Pattern.compile(".*(\\\\Proxy|\\\\Interceptor)$");

    @Override
    public void visitMethodDeclaration(MethodDeclarationTree tree) {
        if (CheckUtils.isConstructorMethodPromotion(tree)) {
            List<ParameterTree> parameters = tree.parameters().parameters();
            for (ParameterTree param : parameters) {
                if(param.declaredType() == null || param.declaredType().toString() == null){
                    continue;
                }
                if (PROXY_INTERCEPTOR_PATTERN.matcher(param.declaredType().toString()).matches()) {
                    context().newIssue(this, param, MESSAGE);
                }
            }
        }

        super.visitMethodDeclaration(tree);
    }
}