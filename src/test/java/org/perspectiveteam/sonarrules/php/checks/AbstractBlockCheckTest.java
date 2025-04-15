package org.perspectiveteam.sonarrules.php.checks;

import org.junit.jupiter.api.Test;
import org.perspectiveteam.sonarrules.plugins.php.CheckVerifier;

public class AbstractBlockCheckTest {
    @Test
    void test() throws Exception {
        CheckVerifier.verify(new AbstractBlockCheck(), "AbstractBlock/AbstractBlockCheck.php");
        CheckVerifier.verify(new AbstractBlockCheck(), "AbstractBlock/AbstractBlockCheck.phtml");
    }
}
