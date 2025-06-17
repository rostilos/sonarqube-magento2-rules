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

import org.sonar.check.Rule;
import org.sonar.check.Priority;
import org.sonar.plugins.php.api.tree.CompilationUnitTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

import java.io.File;
import java.util.Map;

@Rule(
        key = ObsoleteInstallUpgradeScriptsCheck.KEY,
        name = "Obsolete installation and upgrade scripts should not be used",
        description = "Magento 2 has deprecated install and upgrade scripts in favor of declarative schema and data patches.",
        priority = Priority.MAJOR,
        tags = {"magento2", "obsolete", "deprecated"}
)
public class ObsoleteInstallUpgradeScriptsCheck extends PHPVisitorCheck {

    public static final String KEY = "ObsoleteInstallUpgradeScripts";
    private static final Map<String, MessageData> OBSOLETE_PATTERNS;
    private static final Map<String, String> INVALID_DIRECTORIES_ERROR_CODES;

    static {
        OBSOLETE_PATTERNS = Map.of("install-", new MessageData(
                "ObsoleteInstallScript",
                "Install scripts are obsolete. Please use declarative schema approach in module's etc/db_schema.xml file"
        ), "InstallSchema", new MessageData(
                "ObsoleteInstallSchemaScript",
                "InstallSchema scripts are obsolete. Please use declarative schema approach in module's etc/db_schema.xml file"
        ), "InstallData", new MessageData(
                "ObsoleteInstallDataScript",
                "InstallData scripts are obsolete. Please use data patches approach in module's Setup/Patch/Data dir"
        ), "data-install-", new MessageData(
                "ObsoleteDataInstallScript",
                "Install scripts are obsolete. Please create class InstallData in module's Setup folder"
        ), "upgrade-", new MessageData(
                "ObsoleteUpgradeScript",
                "Upgrade scripts are obsolete. Please use declarative schema approach in module's etc/db_schema.xml file"
        ), "UpgradeSchema", new MessageData(
                "ObsoleteUpgradeSchemaScript",
                "UpgradeSchema scripts are obsolete. Please use declarative schema approach in module's etc/db_schema.xml file"
        ), "UpgradeData", new MessageData(
                "ObsoleteUpgradeDataScript",
                "UpgradeData scripts are obsolete. Please use data patches approach in module's Setup/Patch/Data dir"
        ), "data-upgrade", new MessageData(
                "ObsoleteDataUpgradeScript",
                "Upgrade scripts are obsolete. Please use data patches approach in module's Setup/Patch/Data dir"
        ), "recurring", new MessageData(
                "ObsoleteRecurringScript",
                "Recurring scripts are obsolete. Please create class Recurring in module's Setup folder"
        ));

        INVALID_DIRECTORIES_ERROR_CODES = Map.of("data", "DataInvalidDirectory", "sql", "SqlInvalidDirectory");
    }

    @Override
    public void visitCompilationUnit(CompilationUnitTree tree) {
        String filePath = context().getPhpFile().uri().getPath();
        File file = new File(filePath);
        String fileName = file.getName();

        for (Map.Entry<String, MessageData> entry : OBSOLETE_PATTERNS.entrySet()) {
            if (fileName.startsWith(entry.getKey())) {
                MessageData data = entry.getValue();
                context().newFileIssue(this, data.message);
            }
        }

        String parentDir = file.getParentFile().getName();
        if (INVALID_DIRECTORIES_ERROR_CODES.containsKey(parentDir)) {
            String message = fileName + " is in an invalid directory " + file.getParentFile().getName() + ":\n" +
                    "- Create a data patch within module's Setup/Patch/Data folder for data upgrades.\n" +
                    "- Use declarative schema approach in module's etc/db_schema.xml file for schema changes.";

            context().newFileIssue(this, message);
        }
    }

    private static class MessageData {
        final String code;
        final String message;

        MessageData(String code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}
