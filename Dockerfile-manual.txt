# Pull base image
FROM resin/raspberrypi2-debian:latest
MAINTAINER Tobias Hargesheimer tobias.hargesheimer@hs-mainz.de

# Install dependencies
RUN apt-get update && apt-get install -y \
    openjdk-8-jre \
    --no-install-recommends && \
    rm -rf /var/lib/apt/lists/*

# Define commonly used JAVA_HOME variable
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-armhf

ADD target/TelefonWahnsinn-jar-with-dependencies.jar /TelefonWahnsinn.jar
COPY config /config
VOLUME ["/config","/sys/class/gpio"]

# Define default command
CMD ["java","-jar","/TelefonWahnsinn.jar"]
