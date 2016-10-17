package de.hs_mainz.TelefonWahnsinn.helper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.hs_mainz.TelefonWahnsinn.objects.players.MPDInstance;
import de.hs_mainz.TelefonWahnsinn.objects.players.Player;
import de.hs_mainz.TelefonWahnsinn.objects.players.VLC;
import de.hs_mainz.TelefonWahnsinn.objects.players.Xbmc;
import de.hs_mainz.TelefonWahnsinn.objects.sensors.FritzBox;
import de.hs_mainz.TelefonWahnsinn.objects.sensors.GpioSensor;
import de.hs_mainz.TelefonWahnsinn.objects.sensors.SensorDevice;
import de.hs_mainz.TelefonWahnsinn.objects.sensors.Telephone;


public class Config {

	private ArrayList<SensorDevice> sensorList = new ArrayList<SensorDevice>();
	private Player player;
	
	private Node getFirstElementByTagname(Document doc, String tagname){
		return doc.getDocumentElement()
				.getElementsByTagName(tagname).item(0);
	}
	
	public Config(String filePath) {
		File fXmlFile = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			sensorList = setSensors(getFirstElementByTagname(doc, "sensors"));
			player = setPlayer(getFirstElementByTagname(doc, "player"));
		} catch (ParserConfigurationException | SAXException | IOException | NullPointerException | NumberFormatException e) {
			e.printStackTrace();
			System.err.println("Fehler beim Lesen der Configuration");

		}
	}
	
	private Player setPlayer(Node playerNode) {
		Player result;
		Element playerElement = (Element)playerNode; 
		String type = playerElement.getAttribute("type");
		String ip = playerElement.getAttribute("ip");
		String username = playerElement.getAttribute("username");
		String password = playerElement.getAttribute("password");
		int port;
		try {
			port = Integer.parseInt(playerElement.getAttribute("port"));
		} catch (NumberFormatException e) {
			port = 0;
		}
		
		if (type.equalsIgnoreCase("mpd")) {
			result = new MPDInstance(ip, port, password);
		} else if (type.equalsIgnoreCase("xbmc")) {
			result = new Xbmc(ip + ":" + port, username, password);
		} else if (type.equalsIgnoreCase("vlc")) {
			result = new VLC(ip, port, username, password);
		} else {
			System.err.println("player type not found");
			result = null;
		}
		return result;
	}

	private ArrayList<SensorDevice> setSensors(Node sensors) {
		NodeList sensorNodes = sensors.getChildNodes();
		ArrayList<SensorDevice> result = new ArrayList<SensorDevice>();
		for (int i = 0; i < sensorNodes.getLength(); i++) {
			Node n = sensorNodes.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				Element sensor = (Element) n;
				String type = sensor.getAttribute("type");
				String name = sensor.getAttribute("name");
				String ip = sensor.getAttribute("ip");
				String username = sensor.getAttribute("username");
				String password = sensor.getAttribute("password");
				String reverse = sensor.getAttribute("reverse");
				int pin;
				try {
					pin = Integer.parseInt(sensor.getAttribute("pin"));
				} catch (NumberFormatException e){
					pin = 0;
				}
				
				if (type.equalsIgnoreCase("telephone")) {
					result.add(new Telephone(name, ip, username, password));
				} else if (type.equalsIgnoreCase("gpio")) {
					result.add(new GpioSensor(name, pin,reverse!=null&&reverse.equals("true")));
				} else if (type.equalsIgnoreCase("FritzBox")) {
					result.add(new FritzBox(ip, name));
				}
			}
		}
		return result;
	}

	public Player getPlayer() {
		return player;
	}

	public ArrayList<SensorDevice> getSensors() {
		return sensorList;
	}

}
