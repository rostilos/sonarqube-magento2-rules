<?php

namespace Magento\Module;

class TestClass
{
    private $counter;
    protected static $count = 0; // Noncompliant {{Plugins must be stateless. Found potential stateful behavior: static property utilization}}

    public function __construct()
    {
        $this->counter = 0;
    }

    public function aroundProcess(\Magento\Checkout\Model\Cart $subject, callable $proceed)
    {

        $this->counter++; // Noncompliant {{Plugins must be stateless. Found potential stateful behavior: object property modification}}

        return $proceed();
    }

    public function afterProcess(\Magento\Checkout\Model\Cart $subject, $arg)
    {

        $this->prop = "test"; // Noncompliant {{Plugins must be stateless. Found potential stateful behavior: object property modification}}

        return $proceed();
    }

    public function beforeSomeMethod($subject, $arg)
    {
        self::$count++;
        return [$arg];
    }
}