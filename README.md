This is a Kafka source connector API, which pulls 'current_weather' data from the "https://brightsky.dev/docs/#/operations/getCurrentWeather" API.

How to RUN?
1. Clone the the Respository to your local machine or wherever you want to run your source connector.
2. Use command cd to change your directory to the cloned source connector directory, cd [parent directory]/MyApiSourceConnector
3. Run Maven to buid a jar file from the repository, if you do not have Maven intalled yet, take advise from the Internet, one example could be https://maven.apache.org/install.html.
4. Run the command 'mvn clean package' to build the jar file.
5. Create a directory in your project named 'Jar-plugins', or name it whatever you want to.
6. From the 'target' folder of your project copy the 'MyApiConnector-1.0-SNAPSHOT-jar-with-dependencies' and paste it to the 'Jar-plugins' folder.
7. Create another directory in your project name it "SourceConnectors", or name it whatever you want to.
8. Change to the newly created directory "SourceConnector"
9. Creat the following configuration files  'standalone-connect.properties', source-connector.properties 
10.
