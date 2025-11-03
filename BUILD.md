# BetterWhitelist - Build Instructions

## Building the Plugin

This plugin requires internet access to download the Paper API dependency from Maven repositories.

### Prerequisites

- Java 21 or higher
- Maven 3.6 or higher
- Internet connection (to download Paper API)

### Build Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/2g4y1/better-whitelist.git
   cd better-whitelist
   ```

2. Build with Maven:
   ```bash
   mvn clean package
   ```

3. The compiled plugin JAR will be located at:
   ```
   target/BetterWhitelist-1.0.0.jar
   ```

### Installation

1. Copy the `BetterWhitelist-1.0.0.jar` file to your Paper server's `plugins` folder
2. Restart your server
3. Configure the plugin by editing `plugins/BetterWhitelist/config.yml`

### Troubleshooting

If the build fails due to network issues:
- Ensure you have a stable internet connection
- Try running `mvn clean package -U` to force update dependencies
- Check that the Paper repository is accessible: https://repo.papermc.io/repository/maven-public/

## Development Build

If building in a restricted network environment, you may need to manually install the Paper API:

```bash
# Download Paper API jar from https://papermc.io/downloads
# Then install it to your local Maven repository
mvn install:install-file \
  -Dfile=paper-api-1.21.3-R0.1-SNAPSHOT.jar \
  -DgroupId=org.spigotmc \
  -DartifactId=spigot-api \
  -Dversion=1.21.3-R0.1-SNAPSHOT \
  -Dpackaging=jar
```
