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
