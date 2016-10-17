package de.hs_mainz.TelefonWahnsinn.objects.sensors;

import de.hs_mainz.TelefonWahnsinn.helper.FritzThread;

public class FritzBox implements SensorDevice  {
	
	/*
	 * Fritz!Box need active CallMonitor #96*5*
	 */
	
	private boolean busy = false;
	private String name;
	private FritzThread fritzThread;
	private String ip;
	
	public FritzBox(String ip, String name){
		this.name = name;
		this.ip = ip;
	}
	
	public void setBusy(boolean busy) {
		this.busy = busy;
	}

	@Override
	public boolean isBusy() {
		if(fritzThread == null || !fritzThread.isAlive()){
			new FritzThread(ip, 1012, this).start();
		}
		return busy;
	}

	@Override
	public String getName() {
		return name;
	}

}
