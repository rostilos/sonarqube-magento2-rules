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