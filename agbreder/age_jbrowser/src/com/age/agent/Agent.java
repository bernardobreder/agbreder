package com.age.agent;

import com.age.AgeClient;

public class Agent {

	private static final Agent instance = new Agent();

	private AgeClient client = new AgeClient();

	/**
	 * @return the client
	 */
	public AgeClient getClient() {
		return client;
	}

	/**
	 * @param client
	 *            the client to set
	 */
	public void setClient(AgeClient client) {
		this.client = client;
	}

	/**
	 * @return the instance
	 */
	public static Agent getInstance() {
		return instance;
	}

}
