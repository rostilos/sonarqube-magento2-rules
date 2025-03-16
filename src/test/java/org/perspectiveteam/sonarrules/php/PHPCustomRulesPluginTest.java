package org.perspectiveteam.sonarrules.php;

import org.junit.jupiter.api.Test;
import org.sonar.api.Plugin;
import org.sonar.api.SonarEdition;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.SonarRuntime;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.utils.Version;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class PHPCustomRulesPluginTest {
    @Test
    void shouldTestContext() {
        PHPCustomRulesPlugin plugin = new PHPCustomRulesPlugin();
        SonarRuntime runtime = SonarRuntimeImpl.forSonarQube(Version.create(10, 7), SonarQubeSide.SCANNER, SonarEdition.COMMUNITY);
        Plugin.Context context = new Plugin.Context(runtime);

        plugin.define(context);

        assertEquals(1, context.getExtensions().size());
    }
}