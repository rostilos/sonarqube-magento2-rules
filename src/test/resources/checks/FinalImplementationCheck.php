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
