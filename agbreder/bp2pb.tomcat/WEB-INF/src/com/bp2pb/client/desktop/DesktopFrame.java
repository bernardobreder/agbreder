package com.bp2pb.client.desktop;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.metal.MetalLookAndFeel;

import breder.util.swing.StandardDialogs;

import com.bp2pb.client.desktop.node.PageTreeNode;
import com.bp2pb.client.desktop.node.RootTreeNode;

public class DesktopFrame extends JFrame {

	private JTextArea area;
	private JTree tree;

	/**
	 * Construtor
	 */
	public DesktopFrame() {
		this.setTitle("Breder Peep to Peer Browser");
		this.add(this.build());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(800, 600);
		this.setMinimumSize(new Dimension(400, 300));
		this.setLocationRelativeTo(null);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				tree.getSelectionModel().clearSelection();
			}
		});
	}

	/**
	 * Constroi os componentes
	 * 
	 * @return componentes
	 */
	private Component build() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(this.buildTree(), BorderLayout.WEST);
		panel.add(this.buildText(), BorderLayout.CENTER);
		return panel;
	}

	/**
	 * Constroi a caixa de texto
	 * 
	 * @return caixa de texto
	 */
	private Component buildTree() {
		tree = new JTree(new RootTreeNode());
		tree.setRootVisible(false);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				onTreeSelected(e);
			}
		});
		JScrollPane scroll = new JScrollPane(tree);
		scroll.setPreferredSize(new Dimension(250, 0));
		scroll.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		return scroll;
	}

	/**
	 * Constroi a caixa de texto
	 * 
	 * @return caixa de texto
	 */
	private Component buildText() {
		area = new JTextArea();
		area.setTabSize(4);
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		JScrollPane scroll = new JScrollPane(area);
		scroll.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 2));
		return scroll;
	}

	protected void onTreeSelected(TreeSelectionEvent e) {
		if (e.getOldLeadSelectionPath() != null) {
			Object node = e.getOldLeadSelectionPath().getLastPathComponent();
			if (node instanceof PageTreeNode) {
				PageTreeNode pageNode = (PageTreeNode) node;
				String id = pageNode.getId();
				File file = new File("WEB-INF/pag/", id.replace('.', File.separatorChar) + ".txt");
				if (!file.exists()) {
					file.getParentFile().mkdirs();
				}
				try {
					FileOutputStream output = new FileOutputStream(file);
					output.write(area.getText().getBytes("utf-8"));
					output.close();
				} catch (IOException ex) {
					StandardDialogs.showErrorDialog(this, "Erro in Save", ex.getClass().getSimpleName() + ": " + ex.getMessage());
					return;
				}
			}
		}
		if (e.getNewLeadSelectionPath() != null) {
			Object node = e.getNewLeadSelectionPath().getLastPathComponent();
			if (node instanceof PageTreeNode) {
				PageTreeNode pageNode = (PageTreeNode) node;
				String id = pageNode.getId();
				File file = new File("WEB-INF/pag/", id.replace('.', File.separatorChar) + ".txt");
				area.setText("");
				if (file.exists()) {
					try {
						FileInputStream input = new FileInputStream(file);
						ByteArrayOutputStream bytes = new ByteArrayOutputStream((int) file.length());
						for (int n; ((n = input.read()) != -1);) {
							bytes.write((char) n);
						}
						area.setText(new String(bytes.toByteArray(), "utf-8"));
						input.close();
					} catch (IOException ex) {
						StandardDialogs.showErrorDialog(this, "Erro in Load", ex.getClass().getSimpleName() + ": " + ex.getMessage());
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(MetalLookAndFeel.class.getName());
				} catch (Exception e) {
				}
				new DesktopFrame().setVisible(true);
			}
		});
	}

}
