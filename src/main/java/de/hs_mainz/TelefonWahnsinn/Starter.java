package de.hs_mainz.TelefonWahnsinn;

import de.hs_mainz.TelefonWahnsinn.helper.Config;

import java.util.ArrayList;

import de.hs_mainz.TelefonWahnsinn.objects.players.Player;
import de.hs_mainz.TelefonWahnsinn.objects.sensors.SensorDevice;

public class Starter {

	static String filePath = "config/config.xml";
	static boolean pausedBySensorDevice = false;

	public static void main(String[] args) {
		Config config = new Config(filePath);
		Player player = config.getPlayer();
		ArrayList<SensorDevice> sensorList = config.getSensors();
		loopCheck(sensorList, player);
	}

	public static void loopCheck(ArrayList<SensorDevice> sensorList, Player player) {
		while (true) {
			/*
			 * fall 
			 * not pausedBySensorDevice and not player is runnning
			 *   --> do nothing ask nobody
			 * else ( ! ( !pausedBySensorDevice && ! player.isRunning() )
			 *   --> alles andere
			 * !(!a&&!b) <=> (a || b) 
			 */ 
			if (pausedBySensorDevice || player.isRunning()) {

				//is a sensor busy?
				boolean state = getBusyState(sensorList);

				// if a sensor is busy and music is running -> pause
				if (state && player.isRunning()) {
					player.sendPause();
					pausedBySensorDevice = true;
				}

				// if sensors free and we pause before -> turn on
				if (!state && pausedBySensorDevice) {
					player.sendPlay();
					pausedBySensorDevice = false;
				}

				try {
					Thread.currentThread();
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

		}
	}

	// �berpr�ft ob Anrufe existieren
	public static boolean getBusyState(ArrayList<SensorDevice> sensorList) {
		for (SensorDevice sensorDevice : sensorList) {
			if (sensorDevice.isBusy()) {
				System.out.println(sensorDevice.getName() + " is busy");
				return true;
			}
		}
		return false;
	}
}
