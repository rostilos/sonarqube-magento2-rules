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

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.rostilos.sonarrules.php.TestUtils;
import org.sonar.plugins.php.api.tests.PHPCheckTest;
import org.sonar.plugins.php.api.visitors.FileIssue;

import org.sonar.plugins.php.api.visitors.PhpIssue;

class ObsoleteInstallUpgradeScriptsCheckTest {

    private final ObsoleteInstallUpgradeScriptsCheck check = new ObsoleteInstallUpgradeScriptsCheck();
    private static final String TEST_DIR = "InstallUpgrade/";

    @Test
    void okTest() {
        checkNoIssue("valid-file.php");
        checkNoIssue("some-sql-folder/file.php");
        checkNoIssue("some-data-folder/file.php");
    }

    @Test
    void koTest() {
        checkIssue(
                "install-sample.php",
                "Install scripts are obsolete. Please use declarative schema approach in module's etc/db_schema.xml file"
        );
        checkIssue(
                "InstallSchema.php",
                "InstallSchema scripts are obsolete. Please use declarative schema approach in module's etc/db_schema.xml file"
        );
        checkIssue(
                "InstallData.php",
                "InstallData scripts are obsolete. Please use data patches approach in module's Setup/Patch/Data dir"
        );
        checkIssue(
                "data-install-.php",
                "Install scripts are obsolete. Please create class InstallData in module's Setup folder"
        );
        checkIssue(
                "upgrade-.php",
                "Upgrade scripts are obsolete. Please use declarative schema approach in module's etc/db_schema.xml file"
        );
        checkIssue(
                "UpgradeSchema.php",
                "UpgradeSchema scripts are obsolete. Please use declarative schema approach in module's etc/db_schema.xml file"
        );
        checkIssue(
                "UpgradeData.php",
                "UpgradeData scripts are obsolete. Please use data patches approach in module's Setup/Patch/Data dir"
        );
        checkIssue(
                "data-upgrade-.php",
                "Upgrade scripts are obsolete. Please use data patches approach in module's Setup/Patch/Data dir"
        );
        checkIssue(
                "recurring.php",
                "Recurring scripts are obsolete. Please create class Recurring in module's Setup folder"
        );
        checkIssue(
                "sql/file.php",
                "file.php is in an invalid directory sql:\n" +
                        "- Create a data patch within module's Setup/Patch/Data folder for data upgrades.\n" +
                        "- Use declarative schema approach in module's etc/db_schema.xml file for schema changes."
        );
        checkIssue(
                "data/file.php",
                "file.php is in an invalid directory data:\n" +
                        "- Create a data patch within module's Setup/Patch/Data folder for data upgrades.\n" +
                        "- Use declarative schema approach in module's etc/db_schema.xml file for schema changes."
        );
    }

    private void checkNoIssue(String fileName) {
        check(fileName, Collections.emptyList());
    }

    private void checkIssue(String fileName, String expectedIssueMessage) {
        check(fileName, Collections.singletonList(new FileIssue(check, expectedIssueMessage)));
    }

    private void check(String fileName, List<PhpIssue> expectedIssues) {
        PHPCheckTest.check(check, TestUtils.getCheckFile(TEST_DIR + fileName), expectedIssues);
    }

}
