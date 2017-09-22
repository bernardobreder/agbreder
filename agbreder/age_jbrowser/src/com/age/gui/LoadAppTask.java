package com.age.gui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.age.agent.Agent;

/**
 * Carrega uma aplicação, fazendo um acesso remoto a algum agente na qual irá
 * responder
 * 
 * @author bernardobreder
 * 
 */
public class LoadAppTask extends RemoteTask {

	/** Url a ser requisitado */
	private final String url;
	/** Painel a ser modificado */
	private final JPanel panel;
	private String text;

	/**
	 * Construtor
	 * 
	 * @param url
	 * @param panel
	 */
	public LoadAppTask(String url, JPanel panel) {
		this.url = url;
		this.panel = panel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void perform() throws Throwable {
		byte[] requestPage = Agent.getInstance().getClient().requestPage(url);
		if (requestPage == null) {
			throw new IllegalArgumentException(url);
		}
		this.text = new String(requestPage, "utf-8");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateUI() {
		panel.remove(0);
		panel.add(new JLabel(text));
		panel.validate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handler(Throwable e) {
		panel.remove(0);
		panel.add(new JLabel(e.getClass().getSimpleName() + ": " + e.getMessage()));
		panel.validate();
	}

}
