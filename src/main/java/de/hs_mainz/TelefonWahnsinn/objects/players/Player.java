package de.hs_mainz.TelefonWahnsinn.objects.players;

public interface Player {
	public void sendPause();
	public void sendPlay();
	public boolean isRunning();
	public boolean isPlayingStreamed();
}
