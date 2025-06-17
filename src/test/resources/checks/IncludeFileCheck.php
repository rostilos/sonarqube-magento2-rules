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

require 'a.inc'; // Noncompliant {{Include statement detected. File manipulations are discouraged.}}
require_once 'b.inc'; // Noncompliant {{Include statement detected. File manipulations are discouraged.}}

require_once('c.inc'); // Noncompliant {{Include statement detected. File manipulations are discouraged.}}
require('d.inc'); // Noncompliant {{Include statement detected. File manipulations are discouraged.}}

include 'a1.inc'; // Noncompliant {{Include statement detected. File manipulations are discouraged.}}
include_once 'b1.php'; // Noncompliant {{Include statement detected. File manipulations are discouraged.}}

include_once('http://c1.inc'); // Noncompliant {{Include statement detected. File manipulations are discouraged.}}
include('ftp://d1/file.php'); // Noncompliant {{Include statement detected. File manipulations are discouraged.}}

include('a' . 'b'); // Noncompliant {{Include statement detected. File manipulations are discouraged.}}

require_once 'a' . 'b'; // Noncompliant {{Include statement detected. File manipulations are discouraged.}}

class TestInclude
{
    public function go($a)
    {
        include "$a"; // Noncompliant {{Include statement detected. File manipulations are discouraged.}}
        include "$a" . 'test'; // Noncompliant {{Include statement detected. File manipulations are discouraged.}}
    }
}

require_once 'test.php'; // Noncompliant {{Include statement detected. File manipulations are discouraged.}}

class Some_MagentoController extends Mage_Adminhtml_Controller_Action
{
    public function a()
    {
        require_once "o.inc"; // Noncompliant {{Include statement detected. File manipulations are discouraged.}}
    }
}

//require_once 'controller.php';

class Some_MagentoController2 extends Some_OtherClass
{
}