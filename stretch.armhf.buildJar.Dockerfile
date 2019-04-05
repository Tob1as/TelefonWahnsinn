# Pull base image
FROM balenalib/rpi-raspbian:stretch

LABEL org.opencontainers.image.authors="Tobias Hargesheimer <docker@ison.ws>" \
	org.opencontainers.image.title="TelefonWahnsinn (PhoneMadness)" \
	org.opencontainers.image.description="Debian 9 Stretch with PhoneMadness on arm arch. TelefonWahnsinn (=PhoneMadness): A Application written in JAVA monitors various sensors (Telephone -Innovaphone-, Fritz!Box, doorsensor) and switched accordingly the music (player: MPD, XBMC or VLC) to stop/pause or play." \
	org.opencontainers.image.licenses="Apache-2.0" \
	org.opencontainers.image.url="https://hub.docker.com/r/tobi312/rpi-phonemadness" \
	org.opencontainers.image.source="https://github.com/Tob1as/TelefonWahnsinn"

# set to "cross-build-start" if build on x86_64
ARG CROSS_BUILD_START=":"
# set to "cross-build-end" if build on x86_64
ARG CROSS_BUILD_END=":"

RUN [ ${CROSS_BUILD_START} ]

ENV LANG C.UTF-8

# Install dependencies
RUN apt-get update && apt-get install -y \
	git \
	wget \
	openjdk-8-jre \
	openjdk-8-jdk \
	ca-certificates-java \
	--no-install-recommends && \
	rm -rf /var/lib/apt/lists/* && \
	/var/lib/dpkg/info/ca-certificates-java.postinst configure

# Define commonly used JAVA_HOME variable
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-armhf
# Repository 
ENV REPOSITORY https://github.com/Tob1as/TelefonWahnsinn.git

# Build Java Application 
RUN git clone ${REPOSITORY} PhoneMadness/ \
	&& cd PhoneMadness/ \
	&& chmod +x mvnw \
	&& ./mvnw clean package \
	&& cp target/TelefonWahnsinn-jar-with-dependencies.jar /TelefonWahnsinn.jar \
	&& mkdir /config && cp config/config.xml.example /config/config.xml \
	&& cd .. && rm -r PhoneMadness/ 

#RUN [ "cross-build-end" ]

# Define Volumes
VOLUME ["/config","/sys/class/gpio"]

# Define default command
CMD ["java","-jar","/TelefonWahnsinn.jar"]

RUN [ ${CROSS_BUILD_END} ]