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

namespace Foo\Bar;

use Exception;

class StreamHandler
{
    public function handleException()
    {
        try {
            $strChar = stream_get_contents(STDIN, 1); // Compliant
        } catch (Exception $exception) {

        }
    }
}

function executeStream()
{
    $strChar = stream_get_contents(STDIN, 1); // Noncompliant {{The code must be wrapped with a try block if the method uses system resources.}}
    $sock = socket_create(AF_INET, SOCK_STREAM, SOL_TCP); // Noncompliant {{The code must be wrapped with a try block if the method uses system resources.}}
    socket_bind($sock); // Noncompliant {{The code must be wrapped with a try block if the method uses system resources.}}
    socket_close($sock); // Noncompliant {{The code must be wrapped with a try block if the method uses system resources.}}
}