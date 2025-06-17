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

<?php
class TestClass
{
    public function testMethod(){
        echo 'Direct output'; // Noncompliant {{Use of echo language construct is discouraged.}}
        $var = `ls`; // Noncompliant {{Incorrect usage of back quote string constant. Back quotes should be always inside strings.}}
        $s = 'select echo, print, exit, die from `ls`'; // Compliant
    }
}
print 'Another direct output'; // Noncompliant {{Use of print language construct is discouraged.}}