<?php // Noncompliant {{PHP files must declare strict types.}}

namespace Magento\Module;

class TestClass
{

}
?>

<?php // Compliant
declare(strict_types=1);

namespace Magento\Module;

class TestClass
{

}