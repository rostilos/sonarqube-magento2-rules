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