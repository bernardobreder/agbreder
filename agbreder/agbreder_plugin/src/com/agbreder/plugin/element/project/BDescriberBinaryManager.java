package com.agbreder.plugin.element.project;

import java.util.HashMap;
import java.util.Map;

import com.agbreder.plugin.builder.BDescriberBinary;

public class BDescriberBinaryManager {

	private static final BDescriberBinaryManager instance = new BDescriberBinaryManager();

	private Map<String, BDescriberBinary> describers = new HashMap<String, BDescriberBinary>();

	private BDescriberBinaryManager() {
	}

	public synchronized void put(String project, BDescriberBinary describer) {
		this.describers.put(project, describer);
	}

	public synchronized BDescriberBinary get(String project) {
		return this.describers.get(project);
	}

	public synchronized void clear() {
		this.describers.clear();
	}

	public static BDescriberBinaryManager getInstance() {
		return instance;
	}

}
