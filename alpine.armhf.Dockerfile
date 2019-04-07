# Pull image to build java file with maven
#FROM balenalib/raspberry-pi-alpine-openjdk:8-jdk-latest AS java-builder
FROM openjdk:8-jdk-alpine AS java-builder

ENV LANG C.UTF-8

# build
COPY .  /telefonwahnsinn
RUN cd /telefonwahnsinn \
	&& chmod +x mvnw \
	&& ./mvnw clean package

# Pull image to run the application
#FROM balenalib/raspberry-pi-openjdk:8-jre-latest
FROM balenalib/raspberry-pi-alpine-openjdk:8-jre-latest

LABEL org.opencontainers.image.authors="Tobias Hargesheimer <docker@ison.ws>" \
	org.opencontainers.image.title="TelefonWahnsinn (PhoneMadness)" \
	org.opencontainers.image.description="AlpineLinux with PhoneMadness on arm arch. TelefonWahnsinn (=PhoneMadness): A Application written in JAVA monitors various sensors (Telephone -Innovaphone-, Fritz!Box, doorsensor) and switched accordingly the music (player: MPD, XBMC or VLC) to stop/pause or play." \
	org.opencontainers.image.licenses="Apache-2.0" \
	org.opencontainers.image.url="https://hub.docker.com/r/tobi312/rpi-phonemadness" \
	org.opencontainers.image.source="https://github.com/Tob1as/TelefonWahnsinn"

ENV LANG C.UTF-8

# Copy application from build image
COPY --from=java-builder /telefonwahnsinn/target/TelefonWahnsinn-jar-with-dependencies.jar /TelefonWahnsinn.jar
COPY --from=java-builder /telefonwahnsinn/config/config.xml.example /config/config.xml

# Define Volumes
VOLUME ["/config","/sys/class/gpio"]

# Define default command
CMD ["java","-jar","/TelefonWahnsinn.jar"]
