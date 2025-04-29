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