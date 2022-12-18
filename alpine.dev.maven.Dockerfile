FROM maven:3-eclipse-temurin-8 AS java-builder

ARG PHONEMADNESS_VERSION
ENV LANG C.UTF-8
ENV MAVEN_OPTS "-Dhttps.protocols=TLSv1.2 -Dmaven.repo.local=/root/.m2/repository -Dorg.slf4j.simpleLogger.showDateTime=true -Djava.awt.headless=true"
ENV MAVEN_CLI_OPTS "--batch-mode --errors --fail-at-end --show-version --no-transfer-progress -DinstallAtEnd=true -DdeployAtEnd=true"
#ENV MAVEN_CLI_OPTS "--batch-mode"

# build
COPY .  /telefonwahnsinn
WORKDIR /telefonwahnsinn
RUN mvn $MAVEN_CLI_OPTS package --file pom.xml -DskipTests

FROM openjdk:8-jre-alpine

ARG PHONEMADNESS_VERSION
ARG VCS_REF
ARG BUILD_DATE
ENV LANG C.UTF-8

LABEL org.opencontainers.image.authors="Tobias Hargesheimer <docker@ison.ws>" \
    org.opencontainers.image.title="TelefonWahnsinn (PhoneMadness)" \
    org.opencontainers.image.description="TelefonWahnsinn (=PhoneMadness): A Application written in JAVA monitors various sensors (Telephone -Innovaphone-, Fritz!Box, doorsensor) and switched accordingly the music (player: MPD, XBMC or VLC) to stop/pause or play." \
    org.opencontainers.image.licenses="WTFPL" \
    org.opencontainers.image.version="${PHONEMADNESS_VERSION}" \
    org.opencontainers.image.created="${BUILD_DATE}" \
    org.opencontainers.image.revision="${VCS_REF}" \
    org.opencontainers.image.url="https://hub.docker.com/r/tobi312/rpi-phonemadness" \
    org.opencontainers.image.source="https://github.com/Tob1as/TelefonWahnsinn"

# Copy application from build image
COPY --from=java-builder /telefonwahnsinn/target/TelefonWahnsinn-jar-with-dependencies.jar /TelefonWahnsinn.jar
COPY --from=java-builder /telefonwahnsinn/config/config.xml.example /config/config.xml

# Define Volumes
VOLUME ["/config","/sys/class/gpio"]

# Define default command
CMD ["java","-jar","/TelefonWahnsinn.jar"]