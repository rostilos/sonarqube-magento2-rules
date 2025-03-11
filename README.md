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
    <li>✅ 1.3.1. All new PHP files MUST have strict type mode enabled by starting with declare(strict_types=1);. All updated PHP files SHOULD have strict type mode enabled. PHP interfaces MAY have this declaration.
</ul>
<ul>
    <li>✅ 2.3. Class constructor can have only dependency assignment operations and/or argument validation operations. No other operations are allowed.</li>
    <li>️⚠️ 2.3.1. Constructor SHOULD throw an exception when validation of an argument has failed. <i>( I don't see a way to implement this check correctly, but Throw is allowed in constructors under 2.3 )</i> </li>
    <li>✅ 2.3.2. Events MUST NOT be triggered in constructors.</li>
</ul>
<ul>
    <li>✅ 15.3.1. Sanitize input; escape output. <i>(only escape check)</i></li>
</ul>


