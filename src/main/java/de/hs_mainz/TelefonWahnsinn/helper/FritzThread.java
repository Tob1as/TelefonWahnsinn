package de.hs_mainz.TelefonWahnsinn.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import de.hs_mainz.TelefonWahnsinn.objects.sensors.FritzBox;

public class FritzThread extends Thread {
	private String host;
	private int portNum;
	private FritzBox fritzBox;
	
	public FritzThread(String host, int portNum, FritzBox fritzBox){
		this.host = host;
		this.portNum = portNum;
		this.fritzBox = fritzBox;
	}
	
	public void run(){
		try {
			Socket s = new Socket(host, portNum);
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String line = null;
			while((line = in.readLine()) != null) {
				if (line.contains("RING") || line.contains("CALL")){
					fritzBox.setBusy(true);
				} else if (line.contains("DISCONNECT")){
					fritzBox.setBusy(false);
				}
			}
			s.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
