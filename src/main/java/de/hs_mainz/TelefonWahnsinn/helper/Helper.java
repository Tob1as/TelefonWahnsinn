package de.hs_mainz.TelefonWahnsinn.helper;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.codec.binary.Base64;

public final class Helper {

	public static String getBase64Login(String loginName, String password){
		String login = loginName + ":" + password;
		return new String(Base64.encodeBase64(login.getBytes()));
	}
	
	public static String readFile(String path, Charset encoding) {
		String result=null;
		try {
			byte[] encoded;
			encoded = Files.readAllBytes(Paths.get(path));
			result = new String(encoded, encoding);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void setBasicAuth(String username, String password, URLConnection conn) {
		String encoded = getBase64Login(username, password);
		conn.setRequestProperty("Authorization", "Basic " + encoded);
	}
}
