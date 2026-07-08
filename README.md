# Kafka BrightSky Source Connector

A Kafka Source Connector that pulls real-time weather data from the [BrightSky API](https://brightsky.dev/). It fetches `current_weather` data and serializes it into a Kafka topic as JSON records.

## Features
- Fetches current weather data (temperature, condition, wind speed, etc.)
- Supports querying by latitude/longitude or DWD station ID.
- Incremental data fetching based on record timestamps to avoid duplicates.
- JSON output with a structured schema.
- No API key required for basic usage.

## Prerequisites
- **Java 11** or higher
- **Maven 3.6+**
- A running **Apache Kafka** cluster (or single broker)

## Building the Connector
To build the connector JAR with all dependencies:
```bash
mvn clean package
```
The resulting JAR will be located at:
`target/MyApiConnector-1.0-SNAPSHOT-jar-with-dependencies.jar`

## Running Tests
To execute the unit test suite:
```bash
mvn test
```

## Configuration
The connector supports the following configuration properties:

| Property | Description | Importance | Default |
|----------|-------------|------------|---------|
| `source_id` | Unique identifier for this source partition (used for offsets) | HIGH | - |
| `kafka.topic` | The Kafka topic to publish weather data to | HIGH | - |
| `latitude` | Latitude for weather data lookup | LOW | - |
| `longitude` | Longitude for weather data lookup | LOW | - |
| `dwd_station_id` | DWD (Deutscher Wetterdienst) Station ID | LOW | - |
| `wmo_station_id` | WMO (World Meteorological Organization) Station ID | LOW | - |
| `polling_interval` | Polling interval in seconds | MEDIUM | 60 |
| `disable.offset.check`| If true, skips checking existing offsets (useful for debugging) | LOW | false |

Note: You should provide either `latitude` and `longitude`, OR `dwd_station_id`.

## Deployment (Standalone Mode)
1. Copy the JAR from `target/` to your Kafka Connect plugins directory.
2. Create a `connect-standalone.properties` file:
   ```properties
   bootstrap.servers=localhost:9092
   key.converter=org.apache.kafka.connect.storage.StringConverter
   value.converter=org.apache.kafka.connect.json.JsonConverter
   value.converter.schemas.enable=true
   offset.storage.file.filename=/tmp/connect.offsets
   plugin.path=/path/to/your/plugins
   ```
3. Create a `weather-source.properties` file:
   ```properties
   name=brightsky-weather-source
   connector.class=com.kafka.MyApiConnector.ApiSourceConnector
   tasks.max=1
   source_id=berlin-weather
   kafka.topic=weather_data
   latitude=52.5200
   longitude=13.4050
   polling_interval=60
   ```
4. Run the connector:
   ```bash
   connect-standalone.sh connect-standalone.properties weather-source.properties
   ```

## Output Format
Each record published to Kafka contains two main sections: `Weather-Info` and `Source-Info`.

Example JSON (simplified):
```json
{
  "Weather-Info": {
    "source_id": "303711",
    "timestamp": "2024-12-27T22:30:00+01:00",
    "condition": "fog",
    "temperature": 0.4,
    "relative_humidity": 100,
    "wind_speed_10": 5.4,
    "epochTimestamp": 1735335000
  },
  "Source-Info": {
    "id": "303711",
    "station_name": "Berlin-Tempelhof",
    "lat": 52.4676,
    "lon": 13.402
  }
}
```

