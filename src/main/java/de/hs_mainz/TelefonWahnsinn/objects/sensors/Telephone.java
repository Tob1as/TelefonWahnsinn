package de.hs_mainz.TelefonWahnsinn.objects.sensors;

import de.hs_mainz.TelefonWahnsinn.helper.Helper;

import java.io.IOException;
import java.util.HashMap;

import de.hs_mainz.TelefonWahnsinn.objects.Call;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Telephone implements SensorDevice {
	
	String name;
	String ip;
	String username;
	String password;
	HashMap<Integer, Call> callList;
	
	public Telephone(String name, String ip, String username, String password){
		this.name = name;
		this.ip = ip;
		this.username = username;
		this.password = password;
	}
	
	public HashMap<Integer, Call> getCalls(){
		HashMap<Integer, Call> tempCallList = new HashMap<Integer, Call>();
		Document xmlList = this.getModCmdData("cmd=xml-calls");
		if (xmlList==null) {
			return tempCallList;
		}
		Elements calls = xmlList.select("call");
		for (Element call : calls) {
			Call tempCall = new Call(call, this);
			int tempId = tempCall.getId();
			if (callList != null && callList.containsKey(tempId)){
				callList.get(tempId).setPartnerElement(tempCall.getPartnerElement());
				tempCallList.put(tempId, callList.get(tempId));
			} else {
				tempCallList.put(tempId, tempCall);
			}
		}
		this.callList = tempCallList;
		return this.callList;	    
	}
	
	private Document getModCmdData(String query){
		Document document = null;
		try {
			document = Jsoup
					.connect("http://"+this.ip+"/PHONE/APP/mod_cmd.xml?"+query)
					.header("Authorization", "Basic " + Helper.getBase64Login(this.username, this.password))
					.get();
		} catch (IOException e) {
			System.err.println("Couldn't get shit from telephone "+this.name);
		}
		return document;
	}

	public String getName() {
		return name;
	}

	public String getIp() {
		return ip;
	}

	@Override
	public boolean isBusy() {
		return this.getCalls().size() > 0;
	}
	
}
