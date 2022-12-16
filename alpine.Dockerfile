FROM openjdk:8-jre-alpine

ARG PHONEMADNESS_VERSION
ARG VCS_REF
ARG BUILD_DATE

LABEL org.opencontainers.image.authors="Tobias Hargesheimer <docker@ison.ws>" \
    org.opencontainers.image.title="TelefonWahnsinn (PhoneMadness)" \
    org.opencontainers.image.description="TelefonWahnsinn (=PhoneMadness): A Application written in JAVA monitors various sensors (Telephone -Innovaphone-, Fritz!Box, doorsensor) and switched accordingly the music (player: MPD, XBMC or VLC) to stop/pause or play." \
    org.opencontainers.image.licenses="WTFPL" \
    org.opencontainers.image.version="${PHONEMADNESS_VERSION}" \
    org.opencontainers.image.created="${BUILD_DATE}" \
    org.opencontainers.image.revision="${VCS_REF}" \
    org.opencontainers.image.url="https://hub.docker.com/r/tobi312/rpi-phonemadness" \
    org.opencontainers.image.source="https://github.com/Tob1as/TelefonWahnsinn"

ENV LANG C.UTF-8

# Copy application
COPY target/TelefonWahnsinn-jar-with-dependencies.jar /TelefonWahnsinn.jar
COPY config/config.xml.example /config/config.xml

# Define Volumes
VOLUME ["/config","/sys/class/gpio"]

# Define default command
CMD ["java","-jar","/TelefonWahnsinn.jar"]
