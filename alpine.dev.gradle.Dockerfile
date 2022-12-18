FROM gradle:7.6-jdk8-jammy AS java-builder

ARG PHONEMADNESS_VERSION
ENV LANG C.UTF-8
ENV GRADLE_OPTS "-Dorg.gradle.daemon=false -Dorg.gradle.welcome=never -Dmaven.repo.local=/home/gradle/.m2/repository"

# build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle init -V; \
    echo '\
\n\
jar {\n\
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE\n\ 
    manifest {\n\
        attributes "Main-Class": "de.hs_mainz.TelefonWahnsinn.Starter"\n\
    }\n\
    baseName = project.name + "-jar-with-dependencies"\n\
    from {\n\
        configurations.runtimeClasspath.collect { it.isDirectory() ? it : zipTree(it) }\n\
    }\n\
}\n\
' >> /home/gradle/src/build.gradle ; \
    more /home/gradle/src/build.gradle
RUN gradle build

RUN ls -lah /home/gradle/src/build/libs/

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
COPY --from=java-builder /home/gradle/src/build/libs/*.jar /TelefonWahnsinn.jar
COPY --from=java-builder /home/gradle/src/config/config.xml.example /config/config.xml

# Define Volumes
VOLUME ["/config","/sys/class/gpio"]

# Define default command
CMD ["java","-jar","/TelefonWahnsinn.jar"]