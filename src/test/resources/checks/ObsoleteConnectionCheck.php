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

class ObsoleteConnectionCheckTest
{
    public function method_usage_violations()
    {
        $object = new SomeClass();

        $connection1 = $object->_getReadConnection(); // Noncompliant {{Contains obsolete method: _getReadConnection. Please use getConnection method instead.}}
        $connection2 = $object->_getWriteConnection(); // Noncompliant {{Contains obsolete method: _getWriteConnection. Please use getConnection method instead.}}
        $connection3 = $object->_getReadAdapter(); // Noncompliant {{Contains obsolete method: _getReadAdapter. Please use getConnection method instead.}}
        $connection4 = $object->_getWriteAdapter(); // Noncompliant {{Contains obsolete method: _getWriteAdapter. Please use getConnection method instead.}}
        $connection5 = $object->getReadConnection(); // Noncompliant {{Contains obsolete method: getReadConnection. Please use getConnection method instead.}}
        $connection6 = $object->getWriteConnection(); // Noncompliant {{Contains obsolete method: getWriteConnection. Please use getConnection method instead.}}
        $connection7 = $object->getReadAdapter(); // Noncompliant {{Contains obsolete method: getReadAdapter. Please use getConnection method instead.}}
        $connection8 = $object->getWriteAdapter(); // Noncompliant {{Contains obsolete method: getWriteAdapter. Please use getConnection method instead.}}

        $properConnection = $object->getConnection(); // Compliant
    }
}
