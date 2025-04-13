package org.perspectiveteam.sonarrules.php;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.perspectiveteam.sonarrules.php.checks.*;

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
        return "magento2";
    }

    /**
     * Provide the list of checks class that implements rules to be part of the
     * rule repository
     */
    @Override
    public List<Class<?>> checkClasses() {
        return List.of(
                EventsInConstructorsCheck.class,
                ReturnValueCheckCheck.class,
                FunctionArgumentsShouldNotBeModifiedCheck.class,
                StrictTypesDeclarationCheck.class,
                ConstructorDependencyCheck.class,
                XssTemplateCheck.class,
                NoObjectInstantiationInTemplatesCheck.class,
                DiscouragedDependenciesCheck.class,
                StatelessPluginCheck.class,
                ObjectManagerCheck.class,
                LiteralNamespaceCheck.class,
                ThisInTemplatesCheck.class,
                TryProcessSystemResourcesCheck.class,
                ObsoleteInstallUpgradeScriptsCheck.class,
                InterfaceNameCheck.class,
                PerformanceArrayOperationsInLoopCheck.class,
                ArrayAutovivificationCheck.class,
                ObsoleteConnectionCheck.class,
                FinalImplementationCheck.class,
                RawSqlQueryCheck.class
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
        return Map.ofEntries(
                Map.entry(EventsInConstructorsCheck.KEY, "10min"),
                Map.entry(ReturnValueCheckCheck.KEY, "15min"),
                Map.entry(FunctionArgumentsShouldNotBeModifiedCheck.KEY, "5min"),
                Map.entry(StrictTypesDeclarationCheck.KEY, "10min"),
                Map.entry(ConstructorDependencyCheck.KEY, "15min"),
                Map.entry(XssTemplateCheck.KEY, "10min"),
                Map.entry(NoObjectInstantiationInTemplatesCheck.KEY, "10min"),
                Map.entry(DiscouragedDependenciesCheck.KEY, "20min"),
                Map.entry(StatelessPluginCheck.KEY, "15min"),
                Map.entry(ObjectManagerCheck.KEY, "30min"),
                Map.entry(LiteralNamespaceCheck.KEY, "5min"),
                Map.entry(ThisInTemplatesCheck.KEY, "45min"),
                Map.entry(TryProcessSystemResourcesCheck.KEY, "10min"),
                Map.entry(ObsoleteInstallUpgradeScriptsCheck.KEY, "40min"),
                Map.entry(InterfaceNameCheck.KEY, "5min"),
                Map.entry(PerformanceArrayOperationsInLoopCheck.KEY, "20min"),
                Map.entry(ArrayAutovivificationCheck.KEY, "10min"),
                Map.entry(ObsoleteConnectionCheck.KEY, "20min"),
                Map.entry(FinalImplementationCheck.KEY, "5min"),
                Map.entry(RawSqlQueryCheck.KEY, "45min")
        );
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
