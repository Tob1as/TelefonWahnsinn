# Pull image to build java file with maven
FROM openjdk:8-jdk-alpine AS java-builder

# Repository settings
#ENV REPOSITORY https://github.com/TobiasH87/PRIVATE.telefonwahnsinn
ENV REPOSITORY_M2_WRAPPER_PROPERTIES https://raw.githubusercontent.com/TobiasH87Docker/rpi-phonemadness/master/maven-wrapper.properties

# build
#RUN git clone ${REPOSITORY} /telefonwahnsinn/
COPY .  /telefonwahnsinn
RUN cd /telefonwahnsinn \
	&& chmod +x mvnw \
	&& cd maven && rm maven-wrapper.properties && wget ${REPOSITORY_M2_WRAPPER_PROPERTIES} -O maven-wrapper.properties && sed -i -e 's/https/http/g' maven-wrapper.properties && cd .. \
	&& ./mvnw clean package

# Pull image to run the application
#FROM resin/raspberry-pi-openjdk:openjdk-8-jdk
FROM resin/raspberry-pi-alpine-openjdk:openjdk-8-jdk

LABEL maintainer="Tobias Hargesheimer <docker@ison.ws>"
LABEL description="TelefonWahnsinn (PhoneMadness): \
A Application written in JAVA monitors various sensors (Telephone -Innovaphone-, Fritz!Box, doorsensor) and \
switched accordingly the music (player: MPD, XBMC or VLC) to stop/pause or play."

#RUN [ "cross-build-start" ]

# Copy application from build image
COPY --from=java-builder /telefonwahnsinn/target/TelefonWahnsinn-jar-with-dependencies.jar /TelefonWahnsinn.jar
COPY --from=java-builder /telefonwahnsinn/config/config.xml.example /config/config.xml

#RUN [ "cross-build-end" ]

# Define Volumes
VOLUME ["/config","/sys/class/gpio"]

# Define default command
CMD ["java","-jar","/TelefonWahnsinn.jar"]
