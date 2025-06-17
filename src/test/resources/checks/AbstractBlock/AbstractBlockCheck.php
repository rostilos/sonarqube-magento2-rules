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

class TestBlock extends AbstractBlock
{
    public function testMethod() {
        $this->getChildHtml('block_name', true); //Compliant
        $this->getChildHtml('block_name', true, true); // Noncompliant {{3rd parameter is not needed anymore for getChildHtml()}}
        $this->getChildChildHtml('a', 'b', 'c'); //Compliant
        $this->getChildChildHtml('a', 'b', 'c', 'd'); // Noncompliant {{4th parameter is not needed anymore for getChildChildHtml()}}
    }
}