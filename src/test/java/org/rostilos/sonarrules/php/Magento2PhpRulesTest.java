package org.rostilos.sonarrules.php;

import org.junit.jupiter.api.Test;
import org.sonar.api.server.rule.RulesDefinition;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Magento2PhpRulesTest {
    @Test
    void shouldDefineRules() {
        Magento2PhpRules rulesDefinition = new Magento2PhpRules();
        RulesDefinition.Context context = new RulesDefinition.Context();
        rulesDefinition.define(context);
        RulesDefinition.Repository repository = context.repository("magento2");
        assert repository != null;
        assertEquals(28, repository.rules().size());
    }
}