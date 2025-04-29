<?php

interface Customer { // Noncompliant {{Interface should have name that ends with "Interface" suffix.}}
    public function getName(): string;
    public function getEmail(): string;
}