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

namespace App\String\Test\Service; // Noncompliant {{Cannot use "String" in namespace as it is reserved since PHP 7}}
namespace App\Test\Test\Bool; // Noncompliant {{Cannot use "Bool" in namespace as it is reserved since PHP 7}}
namespace App\Test\Match\Service; // Noncompliant {{Cannot use "Match" in namespace as it is reserved since PHP 8}}

class INT // Noncompliant {{Cannot use "INT" as class name as it is reserved since PHP 7}}
{
    public function someMethod()
    {
        return "This class name is a reserved word";
    }
}

interface Float // Noncompliant {{Cannot use "Float" as class name as it is reserved since PHP 7}}
{
    public function interfaceMethod();
}

trait Iterable // Noncompliant {{Cannot use "Iterable" as class name as it is reserved since PHP 7.1}}
{
    public function traitMethod()
    {
        return "This trait name is a reserved word";
    }
}

class ValidClassName // Compliant
{
    public function someMethod()
    {
        return "This class name is valid";
    }
}


class StringHandler // Compliant - not exactly a reserved word
{
    public function handle()
    {
        return "This class handles strings";
    }
}

// Edge cases
class Integer // Compliant - "Integer" is not reserved, only "int" is
{
}

class Boolean // Compliant - "Boolean" is not reserved, only "bool" is
{
}

namespace Something\match\Something; // Noncompliant {{Cannot use "match" in namespace as it is reserved since PHP 8}}

class Void // Noncompliant {{Cannot use "Void" as class name as it is reserved since PHP 7.1}}
{
}

class Resource // Noncompliant {{Cannot use "Resource" as class name as it is reserved since PHP 7}}
{
}

class Object // Noncompliant {{Cannot use "Object" as class name as it is reserved since PHP 7}}
{
}

class Mixed // Noncompliant {{Cannot use "Mixed" as class name as it is reserved since PHP 7}}
{
}

class Numeric // Noncompliant {{Cannot use "Numeric" as class name as it is reserved since PHP 7}}
{
}

class TRUE // Noncompliant {{Cannot use "TRUE" as class name as it is reserved since PHP 7}}
{
}

class FALSE // Noncompliant {{Cannot use "FALSE" as class name as it is reserved since PHP 7}}
{
}

class NULL // Noncompliant {{Cannot use "NULL" as class name as it is reserved since PHP 7}}
{
}

class Match // Noncompliant {{Cannot use "Match" as class name as it is reserved since PHP 8}}
{
}