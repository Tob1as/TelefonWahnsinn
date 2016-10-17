package de.hs_mainz.TelefonWahnsinn.objects.players;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import de.hs_mainz.TelefonWahnsinn.helper.Helper;

public class Xbmc implements Player {

	String ip, username, password;
	boolean running=true;
	
	public Xbmc(String ip, String username, String password){
		this.ip = ip;
		this.username = username;
		this.password = password;
	}
	
	public int sendPlayPause(String type){
		JSONObject returnedJObject= new JSONObject();
		JSONObject paramsObj=new JSONObject();
		try {
			paramsObj.put("playerid", type);
			returnedJObject.put("jsonrpc", "2.0");	
			returnedJObject.put("method", "Player.PlayPause");
			returnedJObject.put("params", paramsObj);
			returnedJObject.put("id", "1");
			System.out.println(returnedJObject.toString());
			return sendJson(returnedJObject, true);
		} catch(JSONException e) {
			//System.out.println(e);
        }
		return -1;
	}
	
	public void sendPlay(){
		int result;
		do {
			result = this.sendPlayPause("0");
		} while (result == 0 );
		this.running = true;
	}

	public void sendPause(){
		int result;
		do {
			result = this.sendPlayPause("0");
		} while (result == 1 );
		this.running = false;
	}
	
	public int sendJson(JSONObject sendData, boolean getSpeed){
		int result = -1;
			String base64login = Helper.getBase64Login(this.username, this.password);
			String urlString = "http://"+this.ip+"/jsonrpc?awx";
			try {
				URL url = new URL(urlString);
				URLConnection conn;
				conn = url.openConnection();
				conn.setConnectTimeout(5000);
				conn.setRequestProperty("Authorization", "Basic " + base64login);
				conn.setRequestProperty("Content-Type", "application/json");
				conn.setDoOutput(true);
				conn.setDoInput(true);
				OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
				wr.write("{\"jsonrpc\":\"2.0\", \"method\":\"Player.PlayPause\", \"params\":{\"playerid\":0}, \"id\":\"1\"}");
				wr.flush(); 
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream())); 
				String line; 
				
				while ((line = rd.readLine()) != null) {
					if (getSpeed){
						try {
							JSONObject tempJsonObject = new JSONObject(line);
							String speed = tempJsonObject.getJSONObject("result").get("speed").toString();
							System.out.println("found speed:"+speed);
							result = Integer.parseInt(speed);
						} catch (JSONException e) {
							System.err.println("couldn't parse JSON from line");
						}						
					}

				    System.out.println(line); 
				} 
				wr.close(); 
				rd.close(); 
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			return result;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public boolean isPlayingStreamed() {
		// TODO Auto-generated method stub
		return false;
	}
}
