# OSGi Bundle and Deploy Sample

This sample demonstrates how to create and deploy a CICS bundle that contains an OSGi bundle using the [CICS Bundle Maven Plugin](https://github.com/IBM/cics-bundle-maven). 

This project contains a basic Hello-World OSGi bundle, built using Maven, and a second project containing the Pom file that creates and deploys the CICS bundle.

## Prerequisites

### Have your system programmer create your BUNDLE definition in CSD

Your system programmer should create a BUNDLE definition in CSD and tell you the CSD group and BUNDLE definition name they have used.
The BUNDLEDIR of the BUNDLE definition your system programmer creates should be set as follows: `<bundles-directory>/<bundle_id>_<bundle_version>`.  So for this sample, if your system programmer configured `bundles-directory` as `/u/someuser/bundles/`, the BUNDLEDIR would be `/u/someuser/bundles/demo-bundle_1.0.0`.


## Using the Sample

1. Clone the repository

2. Navigate to the `demo-osgi` directory, build and install it

    ```sh 
    cd demo-osgi
    mvn clean verify install
    ```

3. Navigate to the `demo-bundle` directory

    ```sh
    cd ../demo-bundle
    ```

4. Populate the fields in the configuration sections of the `Pom.xml` file, specifying the JVM Server name, URL, port, credentials, cicsplex, region, and the bundle definition and csdgroup the system programmer previously created.

    ```xml
    <configuration>
        <defaultjvmserver>{JVM server name}</defaultjvmserver>
    </configuration>
    ```

    ```xml
    <configuration>
        <url>http{s}://{host}:{port}</url>
        <username>{username}</username>
        <password>{password}</password>
        <bunddef>{bundle definition name}</bunddef>
        <csdgroup>{csd group name}</csdgroup>
        <cicsplex>{cics plex name}</cicsplex>
        <region>{region name}</region>
    </configuration>
    ```

5. Run the maven command to build and deploy the CICS bundle

    ```sh
    mvn clean verify
    ```

6. The following success message should be displayed

    ```sh
    [INFO] Deploying bundle to http(s)://{host}:{port} into region {plex name}/{region name}
    [INFO] Bundle deployed
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    ```

## Running the Installed Bundle with a Program / Transaction
To run the bundle with a CICS Program, you must have kept the `<CICS-MainClass>` tag in the demo-osgi bundle pom.xml file.
    
1. Create a Program definition with the Program Type as Java. The Service name for this bundle is `example.HelloWorld` (specified in the CICS-MainClass tag), and the JVM Server is the name specified in the demo-bundle pom file configuration.
    
2. Install the program definition

3. Create a transaction definition, giving it the name of the program created in step 1.

4. Install the transaction

5. Running the transaction will result in "Hello World!" being printed out to the JVM Server's `messages.log`.

    ```txt
    SystemOut                        O Hello World!
    ```

## Troubleshooting

- `unable to find valid certification path to requested target`
    - Uncomment the `<insecure>true</insecure>` line in the demo-bundle pom.xml file to disable TLS/SSL certificate checking.

For further troubleshooting, see the [Main troubleshooting page](https://github.com/IBM/cics-bundle-maven#troubleshooting).