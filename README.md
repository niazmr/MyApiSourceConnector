This is a Java Kafka source connector API, which pulls 'current_weather' data from the "https://brightsky.dev/docs/#/operations/getCurrentWeather" API.
The connector pulls current weather data from the API as JSON records and serializes them to your kafka topic.
To pull weather data, you require no API key.

Prerequisite:
1. A Running cluster of kafka brokers, at least a broker server.


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
    plugin.path=[The path of the jar plugin]
11. In the 'source-connector.properties' include the following lines:
    name=weather-source-connector-station-00433
    connector.class=com.kafka.MyApiConnector.TheSourceConnector
    tasks.max=1
    dwd_station_id=[the name of weather station] // for example 00433, 00427, ... Just provide one of them, if you want to receive weather data from multiple sources, jus creat another property file for example source-connector2.properties and supply the anther weather station name
    polling_interval=60
    kafka.topic=[your kafka topic name]
12. Change to the "SourceConnector" directory
13. Run the command  'connect-standalone.sh standalone-connect.properties sourceConnector.properties'
14. You will receive real time weather data into your kafka topic:
15. The following will be one of the weather response which you will receive:
    {
	"Weather-Info": {
		"source_id": "303711",
		"timestamp": "2024-12-27T22:30:00+01:00",
		"condition": "fog",
		"temperature": 0.4,
		"icon": "fog",
		"visibility": 290,
		"cloud_cover": 100,
		"dew_point": 0.4,
		"solar_10": null,
		"solar_30": null,
		"solar_60": null,
		"precipitation_10": 0,
		"precipitation_30": 0,
		"precipitation_60": 0,
		"pressure_msl": 1033.9,
		"relative_humidity": 100,
		"wind_direction_10": 180,
		"wind_direction_30": 177,
		"wind_direction_60": 175,
		"wind_speed_10": 5.4,
		"wind_speed_30": 5,
		"wind_speed_60": 4.7,
		"sunshine_30": null,
		"sunshine_60": null,
		"epochTimestamp": 0
	},
	"Source-Info": {
		"id": "303711",
		"dwd_station_id": "00433",
		"observation_type": "synop",
		"station_name": "Berlin-Tempelhof",
		"lat": 52.4676,
		"lon": 13.402,
		"wmo_station_id": "10384",
		"height": 47.7,
		"first_record": "2024-12-26T15:30:00+00:00",
		"last_record": "2024-12-27T21:30:00+00:00"
	}
}

