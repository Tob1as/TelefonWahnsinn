package de.hs_mainz.TelefonWahnsinn.objects;

import de.hs_mainz.TelefonWahnsinn.objects.sensors.Telephone;

import org.jsoup.nodes.Element;

public class Call {
	
	private String state; // calling; alerting; connected;
	private int id;
	private long startTime;
	private Element partnerElement;
	
	public Call(Element call, Telephone phone){
		this.state = call.attr("state");
		this.id = Integer.parseInt(call.attr("id"));
		this.startTime =  System.currentTimeMillis() / 1000;
		this.partnerElement =  call.select("userB").first();
	}

	public String getState() {
		return state;
	}

	public int getId() {
		return id;
	}
	
	public long getDuration(){
		return ( System.currentTimeMillis() / 1000 ) - this.startTime;
	}
	
	public Element getPartnerElement() {
		return partnerElement;
	}

	public void setPartnerElement(Element partnerElement) {
		this.partnerElement = partnerElement;
	}

	public String getPartnersNumber(){
		return this.partnerElement.attr("e164");
	}
	
}
