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
namespace Magento\Catalog\Model;

use Magento\Framework\Model\AbstractModel;
use Magento\Tests\Framework\Fixture\Product; // Noncompliant {{Application modules should not use classes from test modules.}}
use Magento\Catalog\Api\Data\ProductInterface;
use Magento\Tests\Unit\Helper\ObjectManager; // Noncompliant {{Application modules should not use classes from test modules.}}
use \Magento\{
    Tests\String, // Noncompliant {{Application modules should not use classes from test modules.}}
    Tests\Int     // Noncompliant {{Application modules should not use classes from test modules.}}
};
