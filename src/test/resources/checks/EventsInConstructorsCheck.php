<?php

namespace Magento\Module;

class TestClass
{
    private $fileReader;
    private $eventManager;

    public function __construct($fileReader, $eventManager)
        {
            $this->data = $fileReader->read('cache.xml');
            $eventManager->dispatch('config_read_after'); // Noncompliant {{No events in constructors.}}
        }
}