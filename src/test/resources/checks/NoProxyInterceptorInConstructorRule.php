<?php

namespace Magento\Module;

class TestClass
{
    private $product;
    private $customerRepo;

    public function __construct(
        \Magento\Catalog\Model\Product\Interceptor $product, // Noncompliant {{No explicit proxy/interceptor requests in constructors.}}
        \Magento\Customer\Model\ResourceModel\CustomerRepository\Proxy $customerRepo // Noncompliant {{No explicit proxy/interceptor requests in constructors.}}
    ) {
    }
}
