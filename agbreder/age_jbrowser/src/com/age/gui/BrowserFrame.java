package com.age.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class BrowserFrame extends JFrame {

	private JPanel panel;

	private JTextField urlText;

	private LoadAppTask task;

	public BrowserFrame() {
		this.setTitle("Agent Environment Browser");
		this.add(this.buildContentPane());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(800, 600);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setLocationRelativeTo(null);
	}

	private Component buildContentPane() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.add(this.buildNorthPane(), BorderLayout.NORTH);
		panel.add(this.buildCenterPane(), BorderLayout.CENTER);
		return panel;
	}

	private Component buildCenterPane() {
		panel = new JPanel(new BorderLayout());
		panel.add(new JPanel());
		JScrollPane scroll = new JScrollPane();
		scroll.setBorder(BorderFactory.createEmptyBorder());
		return panel;
	}

	private Component buildNorthPane() {
		JToolBar bar = new JToolBar();
		bar.setFloatable(false);
		{
			urlText = new JTextField();
			urlText.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					onUrlKeyReleased(e);
				}
			});
			bar.add(urlText);
		}
		return bar;
	}

	protected void onUrlKeyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			String url = urlText.getText().trim();
			if (url.length() > 0) {
				if (task != null) {
					task.cancel();
				}
				task = new LoadAppTask(url, panel);
				task.start();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(MetalLookAndFeel.class.getName());
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new BrowserFrame().setVisible(true);
			}
		});
	}
}
