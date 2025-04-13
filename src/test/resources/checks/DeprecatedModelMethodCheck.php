<?php
/**
 * Test file for DeprecatedModelMethodCheck
 */
namespace Vendor\Module\Model;

class ProductManagement
{
    private $productFactory;

    private $productRepository;

    public function __construct(
        \Magento\Catalog\Model\ProductFactory $productFactory,
        \Magento\Catalog\Api\ProductRepositoryInterface $productRepository
    ) {
        $this->productFactory = $productFactory;
        $this->productRepository = $productRepository;
    }

    public function saveProductDeprecated(array $productData)
    {
        $product = $this->productFactory->create();
        $product->setData($productData);
        $product->getResource()->save($product);  // Noncompliant {{The use of the deprecated method 'getResource()' to 'save' the data detected.}}
        return $product;
    }

    public function loadProductDeprecated($productId)
    {
        $product = $this->productFactory->create();
        $product->getResource()->load($product, $productId);  // Noncompliant {{The use of the deprecated method 'getResource()' to 'load' the data detected.}}
        return $product;
    }

    public function deleteProductDeprecated($product)
    {
        try {
            $product->getResource()->delete($product); // Noncompliant {{The use of the deprecated method 'getResource()' to 'delete' the data detected.}}
            return true;
        } catch (\Exception $e) {
            return false;
        }
    }

    public function saveProductCompliant(array $productData)
    {
        $product = $this->productFactory->create();
        $product->setData($productData);
        return $this->productRepository->save($product);
    }

    public function loadProductCompliant($productId)
    {
        return $this->productRepository->getById($productId);
    }

    public function deleteProductCompliant($product)
    {
        try {
            $this->productRepository->delete($product);
            return true;
        } catch (\Exception $e) {
            return false;
        }
    }

    public function getSomeOtherData($productId)
    {
        $product = $this->productFactory->create();
        return $product->getResource()->getSomeMetadata($productId);
    }
}