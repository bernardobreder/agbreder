package com.agbreder.client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import com.agbreder.client.browser.Browser;

/**
 * Janela principal do navegador
 * 
 * @author bernardobreder
 */
public class BrowserFrame extends JFrame {
	
	/** Instancia unica */
	private static final BrowserFrame instance = new BrowserFrame();
	
	/** Tabbed */
	private JTabbedPane tab;
	
	private JTextField urlField;
	
	private Browser browser;
	
	/**
	 * Construtor
	 */
	private BrowserFrame() {
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.add(this.buildComponent());
		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
	}
	
	private Component buildComponent() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(this.buildNorth(), BorderLayout.NORTH);
		panel.add(this.buildCenter(), BorderLayout.CENTER);
		panel.add(this.buildSouth(), BorderLayout.SOUTH);
		return panel;
	}
	
	private Component buildNorth() {
		JToolBar bar = new JToolBar();
		bar.setFloatable(false);
		{
			JButton c = new JButton("<");
			c.setFocusable(false);
			c.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					onBackAction();
				}
			});
			bar.add(c);
		}
		{
			JButton c = new JButton(">");
			c.setFocusable(false);
			c.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					onNextAction();
				}
			});
			bar.add(c);
		}
		{
			urlField = new JTextField();
			urlField.addKeyListener(new KeyAdapter() {
				/**
				 * {@inheritDoc}
				 */
				@Override
				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						onGoAction();
					}
				}
			});
			bar.add(urlField);
		}
		{
			JButton c = new JButton("Go");
			c.setFocusable(false);
			c.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					onGoAction();
				}
			});
			bar.add(c);
		}
		return bar;
	}
	
	/**
	 * Constroi o painel do meio
	 * 
	 * @return
	 */
	private Component buildCenter() {
		tab = new JTabbedPane();
		JScrollPane scroll = new JScrollPane(this.browser = new Browser());
		scroll.setBorder(BorderFactory.createEmptyBorder());
		tab.addTab("Home", scroll);
		return tab;
	}
	
	/**
	 * Constroi o painel do meio
	 * 
	 * @return
	 */
	private Component buildSouth() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(300, 16));
		return panel;
	}
	
	/**
	 * Ação de carregar uma página
	 */
	protected void onGoAction() {
		String url = urlField.getText().trim();
		new LoadPageTask(url).start();
	}
	
	/**
	 * Ação de Ir para frente
	 */
	protected void onNextAction() {
	}
	
	/**
	 * Ação de Ir para tras
	 */
	protected void onBackAction() {
	}
	
	/**
	 * Altera o componente da aba corrente
	 * 
	 * @param c
	 */
	public void setPage(Component c) {
		this.browser.removeAll();
		this.browser.add(c);
		this.browser.validate();
	}
	
	/**
	 * @return the instance
	 */
	public static BrowserFrame getInstance() {
		return instance;
	}
	
}
