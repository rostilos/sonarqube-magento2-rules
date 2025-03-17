<?php

namespace Magento\TestModule\Model;

use Magento\Framework\App\ObjectManager;
use Magento\Framework\Model\AbstractModel;
use Magento\Framework\ObjectManagerInterface as ObjManager;

class TestClass extends AbstractModel
{
    /**
     * @var \Magento\Framework\ObjectManagerInterface
     */
    protected $_objectManager;

    public function __construct($fileReader, $eventManager)
        {
            $this->dataHelper = $dataHelper ?: ObjManager::getInstance() // Compliant
                        ->get(Data::class);
        }

    public function objectManagerGetInstance()
    {
        $objectManager = ObjManager::getInstance(); // Noncompliant {{The application prohibits the direct use of the ObjectManager in your code.}}
        return $objectManager->get('Magento\Catalog\Model\Product'); // Noncompliant {{The application prohibits the direct use of the ObjectManager in your code.}}
    }

    public function objectManagerGet()
    {
        $product = $this->_objectManager->get('Magento\Catalog\Model\Product'); // Noncompliant {{The application prohibits the direct use of the ObjectManager in your code.}}
        return $product;
    }

    public function objectManagerCreate()
    {
        $objectManager = \Magento\Framework\App\ObjectManager::getInstance(); // Noncompliant {{The application prohibits the direct use of the ObjectManager in your code.}}
        $product = $objectManager->create('Magento\Catalog\Model\Product'); // Noncompliant {{The application prohibits the direct use of the ObjectManager in your code.}}
        return $product;
    }

    public function usingAlias()
    {
        $manager = ObjManager::getInstance(); // Noncompliant {{The application prohibits the direct use of the ObjectManager in your code.}}
        return $manager;
    }

    public function properWay(
        \Magento\Catalog\Model\ProductFactory $productFactory
    ) {
        $product = $productFactory->create(); // Compliant
        return $product;
    }
}

class ProductFactory
{
    public function create()
    {
        $objectManager = ObjectManager::getInstance(); // Compliant
        return $objectManager->create('Magento\Catalog\Model\Product');
    }
}

class CategoryProxy
{
    protected $objectManager;

    public function getCategory()
    {
        $this->objectManager = ObjectManager::getInstance(); // Compliant
        return $this->objectManager->get('Magento\Catalog\Model\Category');
    }
}
