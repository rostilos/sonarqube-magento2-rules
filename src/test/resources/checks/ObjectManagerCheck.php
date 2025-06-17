/*
 * Copyright (C) 2025 Rostislav Suleimanov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
