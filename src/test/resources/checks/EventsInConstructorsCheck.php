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
    private $fileReader;
    private $eventManager;

    public function __construct($fileReader, $eventManager)
        {
            $this->data = $fileReader->read('cache.xml');
            $eventManager->dispatch('config_read_after'); // Noncompliant {{No events in constructors.}}
        }
}