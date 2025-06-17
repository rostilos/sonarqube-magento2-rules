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

namespace Magento\Module;

class TestClass
{
    public function badFunction($param1, $param2)
    {
        $param1 = 'modified value'; // Noncompliant {{Function argument "$param1" should not be modified. Use a local variable instead.}}
        return $param1 . $param2;
    }

    public function goodFunction($param1, $param2)
    {
        $localVar = $param1;
        $localVar = 'modified value'; // Compliant
        return $localVar . $param2;
    }

    public function arrayFunction(array $items)
    {
        $items[] = 'new item'; // Noncompliant {{Function argument "$items" should not be modified. Use a local variable instead.}}
        $items['key'] = 'value'; // Noncompliant {{Function argument "$items" should not be modified. Use a local variable instead.}}

        foreach ($items as &$item) { //Noncompliant {{Potential modification of the function argument by reference variable. Use a local variable instead.}}
            $item = strtoupper($item); // This modifies $items indirectly
        }

        return $items;
    }
}