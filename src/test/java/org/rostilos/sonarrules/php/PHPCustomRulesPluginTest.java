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

package org.rostilos.sonarrules.php;

import org.junit.jupiter.api.Test;
import org.sonar.api.Plugin;
import org.sonar.api.SonarEdition;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.SonarRuntime;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.utils.Version;


import static org.junit.jupiter.api.Assertions.assertEquals;

class PHPCustomRulesPluginTest {
    @Test
    void shouldTestContext() {
        PHPCustomRulesPlugin plugin = new PHPCustomRulesPlugin();
        SonarRuntime runtime = SonarRuntimeImpl.forSonarQube(Version.create(10, 7), SonarQubeSide.SCANNER, SonarEdition.COMMUNITY);
        Plugin.Context context = new Plugin.Context(runtime);

        plugin.define(context);

        assertEquals(1, context.getExtensions().size());
    }
}