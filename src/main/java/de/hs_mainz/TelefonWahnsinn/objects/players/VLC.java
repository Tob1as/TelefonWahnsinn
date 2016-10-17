package de.hs_mainz.TelefonWahnsinn.objects.players;

import de.hs_mainz.TelefonWahnsinn.helper.Helper;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VLC implements Player {
	private String server;
	private int port;
	private String username;
	private String password;

	private static String ERRORTAG = "[vlc error]"; 
	private static String PLAY = "pl_play";
	private static String PAUSE = "pl_pause";
	private static String STOP = "pl_stop";

	public VLC(String server, int port, String username, String password) {
		this.server = "http://" + server;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	@Override
	public void sendPause() {
		if (this.isPlayingStreamed()){
			sendCommand(STOP);
		} else {
			sendCommand(PAUSE);
		}
	}

	@Override
	public void sendPlay() {
		sendCommand(PLAY);
	}

	@Override
	public boolean isRunning() {
		String state = getElementValue("state");
		boolean result = state!=null&&state.equals("playing");
		System.out.println("am i playing?:" +result);
		return result;
	}
	
	@Override
	public boolean isPlayingStreamed(){
		Document doc = sendGetRequest("/requests/playlist.xml");
		try {
			Node currentPlayingItem = getNodesFromXpath(doc, "//leaf[@current=\"current\"]").item(0);
			String currentPlayingUrl = getAttributevalueFromNode(currentPlayingItem, "uri");
			return currentPlayingUrl.startsWith("http://") || currentPlayingUrl.startsWith("https://") || currentPlayingUrl.startsWith("rtp://") || currentPlayingUrl.startsWith("mms://") || currentPlayingUrl.startsWith("rtsp://");
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.err.println(ERRORTAG+"error getting current playing item");
		}
		return false;
	}

	private Document parseXML(InputStream stream) throws Exception {
		DocumentBuilderFactory objDocumentBuilderFactory = null;
		DocumentBuilder objDocumentBuilder = null;
		Document doc = null;
		objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
		objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();
		doc = objDocumentBuilder.parse(stream);
		return doc;
	}

	private Document sendGetRequest(String requestURL) {
		Document result = null;
		try {
			URL url = new URL(this.server + ":" + this.port	+ requestURL);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(5000);
			Helper.setBasicAuth(this.username, this.password, conn);
			Document doc = parseXML(conn.getInputStream());
			result = doc;
		} catch (Exception e) {
			System.err.println(ERRORTAG+"couldn't get shit from "+requestURL);
		}
		return result;
	}
	
	private NodeList getNodesFromXpath(Document doc, String query){
		if (doc!=null&&query!=null){
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr;
			try {
				expr = xpath.compile(query);
				return (NodeList) (expr.evaluate(doc, XPathConstants.NODESET));
			} catch (XPathExpressionException e) {
				System.err.println(ERRORTAG+"couldn't evaluate xpath "+query);
			}
		}
		return null;
	}
	
	private String getAttributevalueFromNode(Node node, String attributeName){
		try {
			return node.getAttributes().getNamedItem(attributeName).getTextContent();
		} catch (NullPointerException e){
			System.err.println(ERRORTAG+"couldn't retrieve Value from "+attributeName);
		}
		return null;
	}
	
	private Document sendCommand(String command) {
		return sendGetRequest("/requests/status.xml?command=" + command);
	}

	private Document getStatus() {
		return sendGetRequest("/requests/status.xml");
	}

	private String getElementValue(String elementName) {
		try {
			return ((Element) getStatus().getDocumentElement()
					.getElementsByTagName(elementName).item(0)).getFirstChild()
					.getNodeValue();
		} catch (NullPointerException e) {
			return null;
		}
	}

}
