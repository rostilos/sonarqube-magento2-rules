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