# sonarqube-magento2-rules
<p>Set of advanced rules for SonarQube, for Magento 2 CMS<p>
<p> A list of available rules is available at the bottom of the README. Implementation plans are described 
<a href="https://github.com/rostilos/sonarqube-magento2-rules/blob/main/docs/TODO.md">
  here
</a>
</p>

<h2>Installation</h2>
To install the plugin you can take either a compiled binary from the assets of the corresponding releases, or build from source.
To build a plugin from source, execute this command from the project root directory:

`mvn clean package`

The plugin jar file is generated in the project's `target/` directory.

<h2>Deploy</h2>
<h4>"Cold" Deploy</h4>

The standard way to install the plugin for regular users is to copy the jar artifact, from the `target/` directory to the `extensions/plugins/` directory of your SonarQube Server installation, then start the server.

The file `logs/web.log` will then contain a log line similar to:
`Deploy plugin Example Plugin / 0.1-SNAPSHOT
Scanner extensions such as sensors are immediately retrieved and loaded when scanning source code.`

<h4>Installation example ( Docker-based )</h4>

````
version: "3.8"
services:
  sonarqube:
    image: sonarqube:10.7-community
    ports:
      - "9000:9000"
      - "9092:9092"
    depends_on:
      - db
    environment:
      SONAR_JDBC_URL: jdbc:postgresql://db:5432/sonar
      SONAR_JDBC_USERNAME: sonar
      SONAR_JDBC_PASSWORD: sonar
    volumes:
      - sonarqube_conf:/opt/sonarqube/conf
      - sonarqube_data:/opt/sonarqube/data
      - sonarqube_logs:/opt/sonarqube/logs
      - sonarqube_extensions:/opt/sonarqube/extensions
      - sonarqube_bundled-plugins:/opt/sonarqube/lib/bundled-plugins
      - ./sonarqube-magento2-rules-1.0.0.jar:/opt/sonarqube/extensions/plugins/sonarqube-magento2-rules-1.0.0.jar
    networks:
      sonar_network:
  db:
    image: postgres:12
    ports:
      - "5432:5432"
    command: postgres -c 'max_connections=300'
    volumes:
      - postgresql:/var/lib/postgresql
      - postgresql_data:/var/lib/postgresql/data
    environment:
      POSTGRES_DB: sonar
      POSTGRES_USER: sonar
      POSTGRES_PASSWORD: sonar
    networks:
      sonar_network:
    restart: unless-stopped
volumes:
  sonarqube_conf:
  sonarqube_data:
  sonarqube_logs:
  sonarqube_extensions:
  sonarqube_bundled-plugins:
  postgresql:
  postgresql_data:
networks:
  sonar_network:

````

<h2>Available rules</h2>

<h3>CodeSniffer Rules, <a href="https://github.com/magento/magento-coding-standard/blob/develop/Magento2/ruleset.xml">Ref</a></h3>

<h4>Exceptions</h4>
<ul>
    <li>✅ TryProcessSystemResources ( Functions that use system resources should be properly wrapped in try-catch blocks )</li>
</ul>

<h4>PHP</h4>
<ul>
    <li>✅ ReturnValue ( Explicit return types MUST BE declared on functions. )</li>
    <li>✅ LiteralNamespaces ( Avoid Literal Namespace Strings. )</li>
    <li>✅ ArrayAutovivification ( PHP 8.1+ has deprecated the automatic conversion of false to array )</li>
    <li>✅ FinalImplementation</li>
    <li>✔️ Goto ( As part of the standard PHP ruleset, php:S907 )</li>
    <li>✔️ Var ( As part of the standard PHP ruleset, php:S1765 )</li>
</ul>

<h4>SQL</h4>
<ul>
    <li>✅ RawQuery</li>
</ul>

<h4>Performance</h3>
<ul>
    <li>✅ ForeachArrayMerge ( is called PerformanceArrayOperationsInLoop, essentially extended to other array operations )</li>
</ul>

<h4>Classes</h4>
<ul>
    <li>✅ DiscouragedDependencies ( No explicit proxy/interceptor requests in constructors. )</li>
</ul>

<h4>Security</h4>
<ul>
    <li>✅ XssTemplate ( All output that could contain user-supplied data must be properly escaped )</li>
</ul>

<h4>Templates</h4>
<ul>
    <li>✅ ThisInTemplate ( The use of $this in templates is forbidden. Using $helper is discouraged )</li>
    <li>✅ ObjectManager ( Direct use of objectManager is prohibited )</li>
</ul>

<h4>Methods</h4>
<ul>
    <li>✅ DeprecatedModelMethod</li>
</ul>

<h4>Legacy</h4>
<ul>
    <li>✅ InstallUpgrade ( ObsoleteInstallUpgradeScripts, Magento 2 has deprecated install and upgrade scripts )</li>
    <li>✅ ObsoleteConnection</li>
</ul>

<h4>NamingConvention</h4>
<ul>
    <li>✅ InterfaceName ( Interfaces should have names that end with the 'Interface' suffix )</li>
    <li>✅ ReservedWords</li>
</ul>

<h4>CodeAnalysis</h4>
<ul>
    <li>✔️ EmptyBlock ( As part of the standard PHP ruleset, php:S1116 )</li>
</ul>

<h3>Adobe's technical guideline rules, <a href="https://developer.adobe.com/commerce/php/coding-standards/technical-guidelines/">Ref.</a></h3>
<ul>
    <li>✅ FunctionArgumentsShouldNotBeModified ( Function arguments should not be modified ).</li>
    <li>✅ StrictTypesDeclaration ( All new PHP files MUST have strict type mode enabled by starting with declare(strict_types=1);. All updated PHP files SHOULD have strict type mode enabled. PHP interfaces MAY have this declaration. ) </li>
</ul>

<ul>
    <li>✅ ConstructorDependency ( Class constructor can have only dependency assignment operations and/or argument validation operations. No other operations are allowed. )</li>
    <li>✅ EventsInConstructors ( Events MUST NOT be triggered in constructors. )</li>
</ul>
<ul>
    <li>✅ StatelessPlugin ( Plugins MUST be stateless. )</li>
</ul>
<ul>
    <li>✅ NoObjectInstantiationInTemplates ( Templates MUST NOT instantiate objects. All objects MUST be passed from the Block objects. )</li>
</ul>


<h2>Additional</h2>
<p>The plugin is under active development.</p>
<p>
    Before the release of the new version, files are analyzed on a “clean” M2 project, as well as on a number of custom real projects (including a set of different modules and themes) to avoid errors and false-positive.
</p>
<p>If you encounter problems or false-positive during the analysis - please let me know, I will be grateful for more information.

</p>