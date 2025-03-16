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