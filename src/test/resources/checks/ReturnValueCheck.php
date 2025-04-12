<?php

namespace Magento\Module;

class TestClass
{
    function doSomethingBad() { // Noncompliant {{Functions must declare explicit return types.}}
        return 'test';
    }

   function doSomethingGood() :string { // Compliant
           return 'test';
   }
}
