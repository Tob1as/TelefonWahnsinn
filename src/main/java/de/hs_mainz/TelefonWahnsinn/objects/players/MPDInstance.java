package de.hs_mainz.TelefonWahnsinn.objects.players;

import java.net.UnknownHostException;

import org.bff.javampd.MPD;
import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDPlayerException;

public class MPDInstance implements Player {
	private MPD mpd;
	private String server;
	private String password;
	private int port;

	public MPDInstance(String server, int port, String password) {
		this.server = server;
		this.password = password;
		this.port = port;
	}

	public void connect() {
		try {
			mpd = new MPD.Builder().server(server).port(port)
					.password(password).build();
		} catch (MPDConnectionException | UnknownHostException e) {
			System.err.println("Could not connect");
		}
	}

	@Override
	public void sendPause() {
		if (mpd != null) {
			try {
				System.out.println("i want to pause");
				mpd.getPlayer().pause();
			} catch (MPDPlayerException e) {
				e.printStackTrace();
				System.err.println("Could not pause");
			}
		} else {
			connect();
		}
	}

	@Override
	public void sendPlay() {
		if (mpd != null) {
			try {
				mpd.getPlayer().play();
			} catch (MPDPlayerException e) {
				e.printStackTrace();
				System.err.println("Could not play");
			}
		} else {
			connect();
		}
	}

	public boolean isRunning() {
		if (mpd != null) {
			try {
				if(mpd.getPlayer().getStatus().toString().equals("STATUS_PLAYING")){
					return true;
				}
			} catch (MPDPlayerException | NullPointerException e ) {
				e.printStackTrace();
				System.err.println("could not get status from MPDplayer");
			}
		} else {
			connect();
		}
		return false;
	}

	@Override
	public boolean isPlayingStreamed() {
		// TODO Auto-generated method stub
		return false;
	}

}
