<?php
class TestClass
{
    public function testMethod(){
        echo 'Direct output'; // Noncompliant {{Use of echo language construct is discouraged.}}
        $var = `ls`; // Noncompliant {{Incorrect usage of back quote string constant. Back quotes should be always inside strings.}}
        $s = 'select echo, print, exit, die from `ls`'; // Compliant
    }
}
print 'Another direct output'; // Noncompliant {{Use of print language construct is discouraged.}}