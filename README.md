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
9. Creat the following configuration files  'standalone-connect.properties', 'source-connector.properties' 
10. In the 'standalone-connect.properties' file include the following lines:
    bootstrap.servers=localhost:9092,localhost:9093,localhost:9094
    key.converter=org.apache.kafka.connect.storage.StringConverter
    key.converter.schemas.enable=false
    value.converter=org.apache.kafka.connect.json.JsonConverter
    value.converter.schemas.enable=false
    offset.storage.file.filename=/tmp/connect.offsets
    offset.flush.interval.ms=10000
    plugin.path=[The pathe of the jar plugin]
11. In the 'source-connector.properties' include the following lines:
    name=weather-source-connector-station-00433
    connector.class=com.kafka.MyApiConnector.TheSourceConnector
    tasks.max=1
    dwd_station_id=[the name of weather station] // for example 00433, 00427, ... Just provide one of them, if you want to receive weather data from multiple sources, jus creat another property file for example source-connector2.properties and supply the anther weather station name
    polling_interval=60
    kafka.topic=[your kafka topic name]
12. Change to the "SourceConnector" directory
13. Run the command  'connect-standalone.sh standalone-connect.properties sourceConnector.properties'
14. You will receive real time weather data into your kafka topic

