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

class Fixture
{
    public function ok()
    {
        $this->create(Magento\CustomerSegment\Model\Segment\Condition\Customer\Address::class); // Compliant
    }

    public function notOk()
    {
        $this->create('Magento\CustomerSegment\Model\Segment\Condition\Customer\Address'); // Noncompliant {{Use ::class notation instead.}}
    }
}   