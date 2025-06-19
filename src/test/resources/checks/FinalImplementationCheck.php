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

final class FinalClass // Noncompliant {{Final keyword is prohibited in Magento.}}
{
    public function regularMethod()
    {
        return "This is in a final class";
    }
}

class RegularClass
{
    public function regularMethod()
    {
        return "This is a regular method";
    }

    final public function finalMethod() // Noncompliant {{Final keyword is prohibited in Magento.}}
    {
        return "This is a final method";
    }

    public final function anotherFinalMethod() // Noncompliant {{Final keyword is prohibited in Magento.}}
    {
        return "This is another final method";
    }
}
