package com.agbreder.peer;

import java.util.ArrayList;
import java.util.List;

/**
 * Gerenciador de Peer
 * 
 * @author bernardobreder
 */
public class PeerManager {
	
	/** Instancia unica */
	private static final PeerManager instance = new PeerManager();
	
	private final List<String> peers = new ArrayList<String>();
	
	private PeerManager() {
	}
	
	/**
	 * @return the instance
	 */
	public static PeerManager getInstance() {
		return instance;
	}
	
	/**
	 * Adiciona um novo peer
	 * 
	 * @param address
	 */
	public void connect(String address) {
		this.peers.add(address);
	}
	
	/**
	 * Adiciona um novo peer
	 * 
	 * @param address
	 * @return lista de peer
	 */
	public List<String> gets() {
		return null;
	}
	
	/**
	 * Adiciona um novo peer
	 * 
	 * @param address
	 */
	public void stop(String address) {
		this.peers.remove(address);
	}
	
}
