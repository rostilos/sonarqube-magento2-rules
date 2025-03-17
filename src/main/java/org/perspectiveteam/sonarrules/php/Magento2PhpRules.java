package org.perspectiveteam.sonarrules.php;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.perspectiveteam.sonarrules.php.checks.EventsInConstructorsCheck;
import org.perspectiveteam.sonarrules.php.checks.ReturnTypesOnFunctionsCheck;
import org.perspectiveteam.sonarrules.php.checks.FunctionArgumentsShouldNotBeModifiedCheck;
import org.perspectiveteam.sonarrules.php.checks.StrictTypesDeclarationCheck;
import org.perspectiveteam.sonarrules.php.checks.ConstructorDependencyCheck;
import org.perspectiveteam.sonarrules.php.checks.EscapeOutputCheck;
import org.perspectiveteam.sonarrules.php.checks.NoObjectInstantiationInTemplatesCheck;
import org.perspectiveteam.sonarrules.php.checks.NoProxyInterceptorInConstructorRule;
import org.perspectiveteam.sonarrules.php.checks.StatelessPluginCheck;
import org.perspectiveteam.sonarrules.php.checks.DirectUseOfObjectManagerCheck;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionAnnotationLoader;
import org.sonar.plugins.php.api.visitors.PHPCustomRuleRepository;

/**
 * Extension point to define a PHP rule repository.
 */
public class Magento2PhpRules implements RulesDefinition, PHPCustomRuleRepository {

    /**
     * Provide the repository key
     */
    @Override
    public String repositoryKey() {
        return "Magento2";
    }

    /**
     * Provide the list of checks class that implements rules
     * to be part of the rule repository
     */
    @Override
    public List<Class<?>> checkClasses() {
        return List.of(
                EventsInConstructorsCheck.class,
                ReturnTypesOnFunctionsCheck.class,
                FunctionArgumentsShouldNotBeModifiedCheck.class,
                StrictTypesDeclarationCheck.class,
                ConstructorDependencyCheck.class,
                EscapeOutputCheck.class,
                NoObjectInstantiationInTemplatesCheck.class,
                NoProxyInterceptorInConstructorRule.class,
                StatelessPluginCheck.class,
                DirectUseOfObjectManagerCheck.class
        );
    }

    @Override
    public void define(Context context) {
        NewRepository repository = context.createRepository(repositoryKey(), "php")
                .setName("Magento2 Repository");

        RulesDefinitionAnnotationLoader annotationLoader = new RulesDefinitionAnnotationLoader();
        checkClasses().forEach(ruleClass -> annotationLoader.load(repository, ruleClass));

        repository.rules().forEach(rule -> rule.setHtmlDescription(loadResource("/org/sonar/l10n/php/rules/magento2/" + rule.key() + ".html")));

        Map<String, String> remediationCosts = getStringStringMap();

        repository.rules().forEach(rule -> rule.setDebtRemediationFunction(
                rule.debtRemediationFunctions().constantPerIssue(remediationCosts.get(rule.key()))));

        repository.done();
    }

    private static Map<String, String> getStringStringMap() {
        Map<String, String> remediationCosts = new HashMap<>();

        remediationCosts.put(EventsInConstructorsCheck.KEY, "5min");
        remediationCosts.put(ReturnTypesOnFunctionsCheck.KEY, "2min");
        remediationCosts.put(FunctionArgumentsShouldNotBeModifiedCheck.KEY, "5min");
        remediationCosts.put(StrictTypesDeclarationCheck.KEY, "5min");
        remediationCosts.put(ConstructorDependencyCheck.KEY, "5min");
        remediationCosts.put(EscapeOutputCheck.KEY, "2min");
        remediationCosts.put(NoObjectInstantiationInTemplatesCheck.KEY, "10min");
        remediationCosts.put(NoProxyInterceptorInConstructorRule.KEY, "5min");
        remediationCosts.put(StatelessPluginCheck.KEY, "15min");
        remediationCosts.put(DirectUseOfObjectManagerCheck.KEY, "30min");
        return remediationCosts;
    }

    private String loadResource(String path) {
        URL resource = getClass().getResource(path);
        if (resource == null) {
            throw new IllegalStateException("Resource not found: " + path);
        }
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try (InputStream in = resource.openStream()) {
            byte[] buffer = new byte[1024];
            for (int len = in.read(buffer); len != -1; len = in.read(buffer)) {
                result.write(buffer, 0, len);
            }
            return new String(result.toByteArray(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read resource: " + path, e);
        }
    }
}
