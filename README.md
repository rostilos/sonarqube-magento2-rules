# sonarqube-magento2-rules
<p>Set of advanced rules for SonarQube, for Magento 2 CMS<p>
<p><strong>⚠️⚠️⚠️The project is in the concept stage, and is not yet recommended for deployment to real SonarQube Server instances. I will be grateful for ideas and any help in its development⚠️⚠️⚠️</strong></p>

<h2>Installation</h2>
To build your plugin project, execute this command from the project root directory:

`mvn clean package`

The plugin jar file is generated in the project's `target/` directory.

<h2>Deploy</h2>
<h4>"Cold" Deploy</h4>
The standard way to install the plugin for regular users is to copy the jar artifact, from the target/ directory to the extensions/plugins/ directory of your SonarQube Server installation, then start the server. The file logs/web.log will then contain a log line similar to:
Deploy plugin Example Plugin / 0.1-SNAPSHOT
Scanner extensions such as sensors are immediately retrieved and loaded when scanning source code.

<h4>Installation example</h4>

````
version: "3.8"
services:
  sonarqube:
    image: sonarqube:community
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
      - ../sonar-custom-plugin-example-10.x/target:/opt/sonarqube/extensions/plugins
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
All rules will refer to the current Technical guidelines specified by Adobe <a href="https://developer.adobe.com/commerce/php/coding-standards/technical-guidelines/">Technical guidelines
</a>

<ul>
    <li>✅ 1.1. Function arguments SHOULD NOT be modified.</li>
    <li>✅ 1.2. Explicit return types MUST BE declared on functions.</li>
    <li>❌ 1.3. Type hints for scalar arguments SHOULD be used.</li>
    <li>❌ 1.3.1. All new PHP files MUST have strict type mode enabled by starting with declare(strict_types=1);. All updated PHP files SHOULD have strict type mode enabled. PHP interfaces MAY have this declaration.
</ul>
<ul>
    <li>❌  2.1. Object decomposition MUST follow the SOLID principles.</li>
    <li>❌ 2.2. Object instantiation </li>
    <li>❌ 2.2.1. An object MUST be ready for use after instantiation. No additional public initialization methods are allowed.</li>
    <li>❌ 2.2.2. Factories SHOULD be used for object instantiation instead of new keyword. An object SHOULD be replaceable for testing or extensibility purposes. Exception: DTOs. There is no behavior in DTOs, so there is no reason for its replaceability. 
       Tests can create real DTOs for stubs. Data interfaces, Exceptions and Zend_Db_Expr are examples of DTOs.</li>
    <li>2.3. Class constructor can have only dependency assignment operations and/or argument validation operations. No other operations are allowed.</li>
    <li>❌ 2.3.1. Constructor SHOULD throw an exception when validation of an argument has failed.</li>
    <li>✅ 2.3.2. Events MUST NOT be triggered in constructors.</li>
    <li>❌ 2.4. All dependencies MUST be requested by the most generic type that is required by the client object.</li>
    <li>❌ 2.5. Proxies and interceptors MUST NEVER be explicitly requested in constructors.
    <li>❌ 2.6. Inheritance SHOULD NOT be used. Composition SHOULD be used for code reuse.</li>
    <li>❌ 2.7. All non-public properties and methods SHOULD be private.</</li>li>
    <li>❌ 2.8. Abstract classes MUST NOT be marked as public @api.</li>
    <li>❌ 2.9. Service classes (ones that provide behavior but not data, like EventManager) SHOULD NOT have a mutable state.</li>
    <li>❌ 2.10. Only data objects or entities (Product, Category, etc.) MAY have any observable state.</li>
    <li>❌ 2.11. "Setters" SHOULD NOT be used. They are only allowed in Data Transfer Objects.</li>
    <li>❌ 2.12. "Getters" SHOULD NOT change the state of an object.</li>
    <li>❌ 2.13. Static methods SHOULD NOT be used.</li>
    <li>❌ 2.14. Temporal coupling MUST be avoided.</li>
    <li>❌ 2.15. Method chaining in class design MUST be avoided.</li>
    <li>❌ 2.16. Law of Demeter SHOULD be obeyed.</li>
    <li>❌ 2.17. Patterns</li>
    <li>❌ 2.17.1. Proxies SHOULD be used for lazy-loading optional dependencies.</li>
    <li>❌ 2.17.2. Composites SHOULD be used when there is a need to work with a tree as a single object.</li>
    <li>2.17.3. Strategy SHOULD be used when there are multiple algorithms for performing an operation.</li>
</ul>

<ul>
    <li>❌ 3.1. There SHOULD be no circular dependencies between objects.</li>
    <li>❌ 3.2. The app/etc/di.xml file MUST contain only framework-level Dependency Injection (DI) settings.</li>
    <li>❌ 3.3. All modular DI settings (except for Presentation layer configuration) SHOULD be stored in <module_dir>/etc/di.xml.</li>
    <li>❌ 3.4. All modular Presentation layer DI settings SHOULD be stored in <module_dir>/etc/<area_code>/di.xml.</li>
</ul>

<ul>
    <li>❌ 4.1. Around-plugins SHOULD only be used when behavior of an original method is supposed to be substituted in certain scenarios.</li>
    <li>❌ 4.2. Plugins SHOULD NOT be used within own module.</li>
    <li>❌ 4.3. Plugins SHOULD NOT be added to data objects.</li>
    <li>❌ 4.4. Plugins MUST be stateless.</li>
    <li>❌ 4.5. Plugins SHOULD NOT change the state of an intercepted object (Intercepted object is $subject).</li>
</ul>

<ul>
    <li>❌ 5.1. All exceptions that are surfaced to the end user MUST produce error messages in the following format:</li>
    <li>❌ 5.2. Exceptions MUST NOT be handled in the same function where they are thrown.</li>
    <li>❌ 5.3. If a function A calls function B, and function B might throw an exception, this exception MUST be either processed by function A or declared by the @throws annotation in the documentation block of function A.</li>
    <li>❌ 5.4. Exceptions MUST NOT handle message output. It is the processing code that decides how to process an exception.</li>
    <li>❌ 5.5. Business logic (both application and domain) MUST NOT be managed with exceptions. Conditional statements SHOULD be used instead.</li>
    <li>❌ 5.6. The short name of an exception class MUST be clear, meaningful, and state the cause of exception.</li>
    <li>❌ 5.7. Thrown exceptions SHOULD be as specific as possible. The top generic \Exception SHOULD NOT be thrown anywhere.</li>
    <li>❌ 5.8. All direct communications with third-party libraries MUST be wrapped with a try/catch statement.</li>
    <li>❌ 5.9. \Exception SHOULD be caught only in the code that calls third-party libraries, in addition to catching specific exceptions thrown by the library.</li>
    <li>❌ 5.10. \Exception SHOULD NOT be thrown in Front Controller and Action Controllers.</li>
    <li>❌ 5.11. A separate exceptions hierarchy SHOULD be defined on each application layer. It is allowed to throw exceptions that are only defined on the same layer.</li>
    <li>❌ 5.12. If an exception is caught on the application layer that differs from the one where it has been thrown, and it SHOULD be re-thrown, you SHOULD create a new exception instance that is appropriate for the current layer.
            In this case, the original exception must be passed to a new instance with the "previous" argument.</li>
    <li>❌ 5.13. It is not allowed to absorb exceptions with no logging or/and any workaround operation executed.</li>
    <li>❌ 5.14. Any exception SHOULD be logged only in the catch block where it is processed, and SHOULD NOT be re-thrown.</li>
    <li>❌ 5.15. Exceptions SHOULD NOT be caught in a loop. The loop SHOULD be wrapped with a try/catch construct instead.</li>
    <li>❌ 5.16. If a method uses system resources (such as files, sockets, streams, etc.), 
            the code MUST be wrapped with a try block and the corresponding finally block. 
            In the finally sections, all resources SHOULD be properly released.</li>
    <li>❌ 5.17. Exceptions which need to be displayed to the user MUST be sub-types of LocalizedException. 
            Any other types of exceptions MUST be wrapped with LocalizedException before being displayed to the user.</li>
    <li>❌ 5.18. LocalizedExceptions SHOULD be thrown in the presentation layer only.</li>
    <li>❌ 5.19. Each module or component MUST declare its own exceptions. 
        Exceptions declared in other components SHOULD NOT be thrown.</li>
    <li>❌ 5.20. Plugin MUST only throw exceptions declared by the method to which the plugin is added or derived exceptions. 
        Observer MUST only throw exceptions declared by the method that triggers an event or derived exceptions.</li>
</ul>


