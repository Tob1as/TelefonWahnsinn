# Pull base image
FROM resin/rpi-raspbian:latest

MAINTAINER Tobias Hargesheimer <docker@ison.ws>

RUN [ "cross-build-start" ]

# Install dependencies
RUN apt-get update && apt-get install -y \
    openjdk-8-jre \
    --no-install-recommends && \
    rm -rf /var/lib/apt/lists/*

# Define commonly used JAVA_HOME variable
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-armhf

ADD target/TelefonWahnsinn-jar-with-dependencies.jar /TelefonWahnsinn.jar
COPY config /config

RUN [ "cross-build-end" ]

VOLUME ["/config","/sys/class/gpio"]

# Define default command
CMD ["java","-jar","/TelefonWahnsinn.jar"]
