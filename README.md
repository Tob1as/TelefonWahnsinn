# TelefonWahnsinn (PhoneMadness)

### What is PhoneMadness (TelefonWahnsinn) ?

DE: *"Eine in JAVA geschriebene Anwendung überwacht verschiedene Sensoren (Telefon -Innovaphone-, Fritz!Box, Türsensor) und schaltet dementsprechend die Musik (Player: MPD, XBMC oder VLC) auf stop/pause oder play."*

EN: *"A Application written in JAVA monitors various sensors (Telephone -Innovaphone-, Fritz!Box, doorsensor) and switched accordingly the music (player: MPD, XBMC or VLC) to stop / pause or play."*  

We/I used it in our office. ;)  

### Supported tags and respective `Dockerfile` links
-	[`latest` (*Dockerfile*)](https://github.com/Tob1as/TelefonWahnsinn/blob/master/alpine.armhf.Dockerfile)

### This Image on
* [DockerHub](https://hub.docker.com/r/tobi312/rpi-phonemadness)
* [GitHub](https://github.com/Tob1as/TelefonWahnsinn)

### Developer
* Felix W.
* [Tobias H.](https://github.com/Tob1as)


### used libraries : 
*  commons-codec [Apache Commons Codec](http://commons.apache.org/proper/commons-codec/download_codec.cgi) or [mvnrepository](https://mvnrepository.com/artifact/commons-codec/commons-codec)
*  guice-no-aop [GitHub: Google Guice](https://github.com/google/guice/) or [mvnrepository](https://mvnrepository.com/artifact/com.google.inject/guice)
*  javampd [GitHub: JavaMPD](https://github.com/finnyb/javampd) or [mvnrepository](https://mvnrepository.com/artifact/net.thejavashop/javampd)
*  javax.inject [Google Code: atinject](https://code.google.com/p/atinject/) or [mvnrepository](https://mvnrepository.com/artifact/javax.inject/javax.inject)
*  jsoup [jsoup](https://jsoup.org/) or [mvnrepository](https://mvnrepository.com/artifact/org.jsoup/jsoup)
*  org.json [GitHub: JSON-java](https://github.com/stleary/JSON-java) or [mvnrepository](https://mvnrepository.com/artifact/org.json/json)
*  slf4j-api [SLF4J](http://www.slf4j.org/) or [mvnrepository](https://mvnrepository.com/artifact/org.slf4j/slf4j-api)
*  slf4j-nop [SLF4J](http://www.slf4j.org/) or [mvnrepository](https://mvnrepository.com/artifact/org.slf4j/slf4j-nop)


### Setup / Usage 

#### local

##### Requirements

* Network connection between the devices
* installed *Java 8* and set environment variables JAVA_HOME

##### Usage

* Download the latest release of *TelefonWahnsinn-jar-with-dependencies.jar* in a folder or build your own jar-file with Eclipse and Maven 
* create a *config* folder, in this folder download the configuration file called *config.xml.example* and rename it in config.xml. Example content:
  ```xml
	<wahnsinnsConfig>
		<sensors>
			<sensor type="telephone" name="Phone1" ip="10.112.1.1" username="user" password="password" ></sensor>
			<sensor type="telephone" name="Phone2" ip="10.112.1.2" username="user" password="password" ></sensor>
			<sensor type="telephone" name="Phone myself" ip="10.112.1.3" username="user" password="password" ></sensor>
			<!--<sensor type="fritzbox" ip="192.168.178.1" name="FritzBox"></sensor>-->
			<sensor type="gpio" name="Doorsensor" pin="4" ></sensor>
		</sensors>
		<player type="mpd" ip="localhost" port="6600" password="password"></player>
		<!--
		<player type="xbmc" ip="localhost" port="6600" username="user" password="password"></player>
		<player type="vlc" ip="localhost" port="8080" username="" password="password"></player>
		-->
	</wahnsinnsConfig>
  ```
  
	* Make your settings
	* Note: If you want use the doorsensor, then use a Raspberry Pi to run the JAR-File and *magnetic contact* (DE: *Magnetkontakt/Reedkontakt*) e.g on GPIO Pin 4.
* start it with: ``` java -jar TelefonWahnsinn-jar-with-dependencies.jar ```

RaspberryPi GPIO Notice:
* ``` sudo nano /etc/rc.local ```
* copy next lines before: exit 0 and then reboot
* ``` echo 4 > /sys/class/gpio/export ```
* ``` echo in > /sys/class/gpio/gpio4/direction ```

Fritz!Box Notice:
* Fritz!Box need active CallMonitor ```#96*5*``` (Port: 1012) [or see here](https://www.janrufmonitor.de/ueberwachung-freischalten/).

#### Docker (e.g. on RaspberryPi)

##### Requirements

* Network connection between the devices
* installed *Docker* ``` sudo curl -sSL https://get.docker.com | sh ``` and then ``` sudo usermod -aG docker pi ```

##### Usage

* ``` docker pull tobi312/rpi-phonemadness:latest ```
* ``` mkdir -p /home/pi/.config/telefonwahnsinn/config && wget -O /home/pi/.config/telefonwahnsinn/config/config.xml https://raw.githubusercontent.com/Tob1as/TelefonWahnsinn/master/config/config.xml.example ```
* Make your settings: ``` nano /home/pi/.config/telefonwahnsinn/config/config.xml ```
* ``` docker run --name telefonwahnsinn -d -v /home/pi/.config/telefonwahnsinn/config:/config:ro -v /sys/class/gpio:/sys/class/gpio:ro tobi312/rpi-phonemadness:latest ```


#### Have FUN!
