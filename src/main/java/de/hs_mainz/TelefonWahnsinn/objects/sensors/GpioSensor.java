package de.hs_mainz.TelefonWahnsinn.objects.sensors;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class GpioSensor extends FileSystem {

	public GpioSensor(String name, int pin, boolean reverse) {
		super(name, "/sys/class/gpio/gpio"+pin+"/value", StandardCharsets.US_ASCII);
		String freeValue = reverse ? "1" : "0";
		String busyValue = reverse ? "0" : "1";
		super.setFreeValues(new ArrayList<String>(Arrays.asList(freeValue)));
		super.setBusyValues(new ArrayList<String>(Arrays.asList(busyValue)));	
	}

}
