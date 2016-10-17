package de.hs_mainz.TelefonWahnsinn.objects.sensors;

import de.hs_mainz.TelefonWahnsinn.helper.Helper;

import java.nio.charset.Charset;
import java.util.ArrayList;

public class FileSystem implements SensorDevice {

	private String path;
	private Charset standartCharset;
	private ArrayList<String> freeValues;
	private ArrayList<String> busyValues;
	private String name;

	public FileSystem(String name, String path, Charset standartCharset) {
		this.name = name;
		this.path = path;
		this.standartCharset = standartCharset;
	}
	
	public FileSystem(String name, String path, ArrayList<String> freeValues,
			ArrayList<String> busyValues, Charset standartCharset) {
		this(name,path,standartCharset);
		this.setFreeValues(freeValues);
		this.setBusyValues(busyValues);
	}

	@Override
	public boolean isBusy() {
		String filecontent = Helper.readFile(path, standartCharset);
		if (filecontent == null) {
			System.err.println("couldnt read file");
			return false;
		}
		filecontent = filecontent.replaceAll("\\r|\\n", " ").split(" ")[0];
		if (freeValues.contains(filecontent)) {
			return false;
		} else if (busyValues.contains(filecontent)) {
			return true;
		} else {
			System.err.println("Invalid value '"+filecontent+"'");
			return true;
		}
	}

	@Override
	public String getName() {
		return name;
	}
	
	public void setFreeValues(ArrayList<String> arrayList) {
		this.freeValues = arrayList;
	}

	public void setBusyValues(ArrayList<String> busyValues) {
		this.busyValues = busyValues;
	}
}
