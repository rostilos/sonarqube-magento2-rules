package org.perspectiveteam.sonarrules.php.checks;

import org.junit.jupiter.api.Test;
import org.perspectiveteam.sonarrules.plugins.php.CheckVerifier;

public class XssTemplateCheckTest {
    @Test
    void test() throws Exception {
        CheckVerifier.verify(new XssTemplateCheck(), "XssTemplateCheck.phtml");
    }
}
