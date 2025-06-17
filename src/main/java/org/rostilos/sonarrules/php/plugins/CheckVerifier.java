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

package org.rostilos.sonarrules.php.plugins;

import org.sonar.plugins.php.api.tests.PHPCheckVerifier;
import org.sonar.plugins.php.api.visitors.PHPCheck;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

public class CheckVerifier extends PHPCheckVerifier {
    private CheckVerifier(boolean readExpectedIssuesFromComments, boolean frameworkDetectionEnabled) {
        super(readExpectedIssuesFromComments, frameworkDetectionEnabled);
    }

    public static void verify(PHPCheck check, String... relativePaths) {
        PHPCheckVerifier.verify(check, Arrays.stream(relativePaths).map(CheckVerifier::checkFile).toArray(File[]::new));
    }

    public static void verifyNoIssue(PHPCheck check, String relativePath) {
        PHPCheckVerifier.verifyNoIssue(checkFile(relativePath), check);
    }

    public static void verifyNoIssueIgnoringExpected(PHPCheck check, String relativePath) {
        new CheckVerifier(false, true).createVerifier(Collections.singletonList(checkFile(relativePath)), check).assertNoIssues();
    }

    private static File checkFile(String relativePath) {
        return new File("src/test/resources/checks/" + relativePath);
    }
}



