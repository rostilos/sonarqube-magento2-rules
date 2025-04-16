<?php
namespace Magento\Catalog\Model;

use Magento\Framework\Model\AbstractModel;
use Magento\Tests\Framework\Fixture\Product; // Noncompliant {{Application modules should not use classes from test modules.}}
use Magento\Catalog\Api\Data\ProductInterface;
use Magento\Tests\Unit\Helper\ObjectManager; // Noncompliant {{Application modules should not use classes from test modules.}}
use \Magento\{
    Tests\String, // Noncompliant {{Application modules should not use classes from test modules.}}
    Tests\Int     // Noncompliant {{Application modules should not use classes from test modules.}}
};
