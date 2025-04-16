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