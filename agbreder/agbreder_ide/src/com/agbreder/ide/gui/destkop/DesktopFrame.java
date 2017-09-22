package com.agbreder.ide.gui.destkop;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import breder.util.swing.BFrame;

import com.agbreder.ide.gui.builder.agb.file.AGBFileBuilderFrame;
import com.agbreder.ide.gui.builder.agb.folder.AGBFolderBuilderFrame;
import com.agbreder.ide.gui.builder.agb.project.AGBProjectBuilderFrame;
import com.agbreder.ide.gui.console.IDEConsole;
import com.agbreder.ide.gui.explorer.ExplorerTree;
import com.agbreder.ide.model.io.IFile;
import com.agbreder.ide.model.io.IFileEvent;
import com.agbreder.ide.model.io.event.FileAdapter;
import com.agbreder.ide.resource.Resource;

/**
 * Classe de janela de desktop
 * 
 * @author bernardobreder
 */
public class DesktopFrame extends BFrame {
	
	/** Project Builder Button */
	private JButton projectBuilderButton;
	
	/** Package Builder Button */
	private JButton packageBuilderButton;
	
	/** Class Builder Button */
	private JButton classBuilderButton;
	
	/** Arvore de Explorer */
	protected ExplorerTree explorerTree;
	
	/** Tab de Editor */
	protected JTabbedPane editorTab;
	
	/** Arquivos Abertos */
	protected final List<FileOpened> files = new ArrayList<FileOpened>();
	
	/** Console */
	protected IDEConsole console;
	
	/**
	 * Construtor
	 */
	protected DesktopFrame() {
		super(null);
		this.add(this.build());
		this.setJMenuBar(this.buildMenu());
		this.setTitle("Agent Breder Ide");
		this.setSize(new Dimension(1024, 768));
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setLocationRelativeTo(null);
	}
	
	/**
	 * Dispara todos os eventos
	 */
	protected void fireEvents() {
		onExplorerSelected(null);
	}
	
	/**
	 * Constroi o menu
	 * 
	 * @return menu
	 */
	private JMenuBar buildMenu() {
		JMenuBar bar = new JMenuBar();
		bar.add(this.buildFileMenu());
		bar.add(this.buildEditMenu());
		bar.add(this.buildNavigateMenu());
		bar.add(this.buildProjectMenu());
		bar.add(this.buildRunMenu());
		bar.add(this.buildHelpMenu());
		return bar;
	}
	
	/**
	 * Constroi o menu
	 * 
	 * @return menu
	 */
	private JMenu buildFileMenu() {
		JMenu menu = new JMenu("File");
		menu.setMnemonic('F');
		{
			JMenuItem c =
				new JMenuItem("New", new ImageIcon(Resource.getInstance().getNew()));
			c.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
			menu.add(c);
		}
		{
			JMenuItem c =
				new JMenuItem("Open", new ImageIcon(Resource.getInstance().getNew()));
			c.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
			menu.add(c);
		}
		menu.addSeparator();
		{
			JMenuItem c =
				new JMenuItem("Save", new ImageIcon(Resource.getInstance().getSave()));
			c.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
			c.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					IDesktop.DEFAULT.saveEditor();
				}
			});
			menu.add(c);
		}
		{
			JMenuItem c =
				new JMenuItem("Save All", new ImageIcon(Resource.getInstance()
					.getSaveAll()));
			c.setAccelerator(KeyStroke.getKeyStroke("ctrl shift S"));
			c.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					IDesktop.DEFAULT.saveAllEditors();
				}
			});
			menu.add(c);
		}
		menu.addSeparator();
		{
			JMenuItem c =
				new JMenuItem("Close", new ImageIcon(Resource.getInstance().getClose()));
			c.setAccelerator(KeyStroke.getKeyStroke("ctrl W"));
			c.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					IDesktop.DEFAULT.closeEditor();
				}
			});
			menu.add(c);
		}
		{
			JMenuItem c =
				new JMenuItem("Close All", new ImageIcon(Resource.getInstance()
					.getCloseAll()));
			c.setAccelerator(KeyStroke.getKeyStroke("ctrl shift W"));
			c.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					IDesktop.DEFAULT.closeAllEditors();
				}
			});
			menu.add(c);
		}
		menu.addSeparator();
		{
			JMenuItem c =
				new JMenuItem("Quit", new ImageIcon(Resource.getInstance().getNew()));
			c.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					IDesktop.DEFAULT.close();
				}
			});
			c.setAccelerator(KeyStroke.getKeyStroke("ctrl Q"));
			menu.add(c);
		}
		return menu;
	}
	
	/**
	 * Constroi o menu
	 * 
	 * @return menu
	 */
	private JMenu buildEditMenu() {
		JMenu menu = new JMenu("Edit");
		menu.setMnemonic('E');
		{
			JMenuItem c =
				new JMenuItem("Undo", new ImageIcon(Resource.getInstance().getUndo()));
			c.setAccelerator(KeyStroke.getKeyStroke("ctrl Z"));
			menu.add(c);
		}
		{
			JMenuItem c =
				new JMenuItem("Redo", new ImageIcon(Resource.getInstance().getRedo()));
			c.setAccelerator(KeyStroke.getKeyStroke("ctrl Y"));
			menu.add(c);
		}
		menu.addSeparator();
		{
			JMenuItem c =
				new JMenuItem("Cut", new ImageIcon(Resource.getInstance().getCut()));
			c.setAccelerator(KeyStroke.getKeyStroke("ctrl X"));
			menu.add(c);
		}
		{
			JMenuItem c =
				new JMenuItem("Copy", new ImageIcon(Resource.getInstance().getCopy()));
			c.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
			menu.add(c);
		}
		{
			JMenuItem c =
				new JMenuItem("Paste", new ImageIcon(Resource.getInstance().getPaste()));
			c.setAccelerator(KeyStroke.getKeyStroke("ctrl V"));
			menu.add(c);
		}
		menu.addSeparator();
		{
			JMenuItem c =
				new JMenuItem("Select All", new ImageIcon(Resource.getInstance()
					.getSelectAll()));
			c.setAccelerator(KeyStroke.getKeyStroke("ctrl A"));
			menu.add(c);
		}
		{
			JMenuItem c =
				new JMenuItem("Delete", new ImageIcon(Resource.getInstance()
					.getDelete()));
			c.setAccelerator(KeyStroke.getKeyStroke("ctrl D"));
			menu.add(c);
		}
		menu.addSeparator();
		{
			JMenuItem c =
				new JMenuItem("Find", new ImageIcon(Resource.getInstance().getFind()));
			c.setAccelerator(KeyStroke.getKeyStroke("ctrl F"));
			menu.add(c);
		}
		{
			JMenuItem c =
				new JMenuItem("Replace", new ImageIcon(Resource.getInstance()
					.getReplace()));
//			c.setAccelerator(KeyStroke.getKeyStroke("ctrl R"));
			menu.add(c);
		}
		return menu;
	}
	
	/**
	 * Constroi o menu
	 * 
	 * @return menu
	 */
	private JMenu buildNavigateMenu() {
		JMenu menu = new JMenu("Navigate");
		menu.setMnemonic('N');
		{
			JMenuItem c =
				new JMenuItem("Goto...",
					new ImageIcon(Resource.getInstance().getGoto()));
			c.setAccelerator(KeyStroke.getKeyStroke("ctrl L"));
			menu.add(c);
		}
		{
			JMenuItem c =
				new JMenuItem("Open", new ImageIcon(Resource.getInstance().getOpen()));
			c.setAccelerator(KeyStroke.getKeyStroke("F3"));
			menu.add(c);
		}
		{
			JMenuItem c =
				new JMenuItem("Hierarchy", new ImageIcon(Resource.getInstance()
					.getHierarchy()));
//			c.setAccelerator(KeyStroke.getKeyStroke("ctrl R"));
			menu.add(c);
		}
		{
			JMenuItem c =
				new JMenuItem("Call Hierarchy", new ImageIcon(Resource.getInstance()
					.getCallHierarchy()));
//			c.setAccelerator(KeyStroke.getKeyStroke("ctrl R"));
			menu.add(c);
		}
		menu.addSeparator();
		{
			JMenuItem c =
				new JMenuItem("Open Type...", new ImageIcon(Resource.getInstance()
					.getOpenType()));
			c.setAccelerator(KeyStroke.getKeyStroke("ctrl shift T"));
			menu.add(c);
		}
		{
			JMenuItem c =
				new JMenuItem("Open Resource...", new ImageIcon(Resource.getInstance()
					.getOpenResource()));
			c.setAccelerator(KeyStroke.getKeyStroke("ctrl shift R"));
			menu.add(c);
		}
		menu.addSeparator();
		{
			JMenuItem c =
				new JMenuItem("Back", new ImageIcon(Resource.getInstance().getBack()));
			c.setAccelerator(KeyStroke.getKeyStroke("ctrl ["));
			menu.add(c);
		}
		{
			JMenuItem c =
				new JMenuItem("Forward",
					new ImageIcon(Resource.getInstance().getNext()));
			c.setAccelerator(KeyStroke.getKeyStroke("ctrl ]"));
			menu.add(c);
		}
		return menu;
	}
	
	/**
	 * Constroi o menu
	 * 
	 * @return menu
	 */
	private JMenu buildProjectMenu() {
		JMenu menu = new JMenu("Project");
		menu.setMnemonic('P');
		{
			JMenuItem c =
				new JMenuItem("Open Project", new ImageIcon(Resource.getInstance()
					.getOpenProject()));
			menu.add(c);
		}
		{
			JMenuItem c =
				new JMenuItem("Close Project", new ImageIcon(Resource.getInstance()
					.getCloseProject()));
			menu.add(c);
		}
		menu.addSeparator();
		{
			JMenuItem c =
				new JMenuItem("Build All", new ImageIcon(Resource.getInstance()
					.getBuildAll()));
			c.setAccelerator(KeyStroke.getKeyStroke("ctrl B"));
			menu.add(c);
		}
		{
			JMenuItem c =
				new JMenuItem("Build Project", new ImageIcon(Resource.getInstance()
					.getBuildProject()));
			menu.add(c);
		}
		{
			JMenuItem c =
				new JMenuItem("Clean...", new ImageIcon(Resource.getInstance()
					.getClear()));
			menu.add(c);
		}
		{
			JMenuItem c =
				new JMenuItem("Build Automatically", new ImageIcon(Resource
					.getInstance().getBuildAuto()));
			menu.add(c);
		}
		return menu;
	}
	
	/**
	 * Constroi o menu
	 * 
	 * @return menu
	 */
	private JMenu buildRunMenu() {
		JMenu menu = new JMenu("Run");
		menu.setMnemonic('R');
		{
			JMenuItem c =
				new JMenuItem("Debug Project", new ImageIcon(Resource.getInstance()
					.getDebug()));
			c.setAccelerator(KeyStroke.getKeyStroke("ctrl E"));
			this.installPlayEnabled(c);
			menu.add(c);
		}
		{
			final JMenuItem c =
				new JMenuItem("Play Project", new ImageIcon(Resource.getInstance()
					.getPlay()));
			c.setAccelerator(KeyStroke.getKeyStroke("ctrl R"));
			c.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					IDesktop.DEFAULT.saveAllEditors();
					IDesktop.DEFAULT.runProject();
				}
			});
			this.installPlayEnabled(c);
			menu.add(c);
		}
		menu.addSeparator();
		{
			JMenuItem c =
				new JMenuItem("Pause Process", new ImageIcon(Resource.getInstance()
					.getPause()));
			c.setAccelerator(KeyStroke.getKeyStroke("ctrl T"));
			this.installPlayEnabled(c);
			menu.add(c);
		}
		{
			JMenuItem c =
				new JMenuItem("Continue Process", new ImageIcon(Resource.getInstance()
					.getPlay()));
			// c.setAccelerator(KeyStroke.getKeyStroke("ctrl R"));
			this.installPlayEnabled(c);
			menu.add(c);
		}
		{
			JMenuItem c =
				new JMenuItem("Step Next Process", new ImageIcon(Resource.getInstance()
					.getNext()));
			// c.setAccelerator(KeyStroke.getKeyStroke("ctrl R"));
			this.installPlayEnabled(c);
			menu.add(c);
		}
		{
			JMenuItem c =
				new JMenuItem("Step In Process", new ImageIcon(Resource.getInstance()
					.getNextIn()));
			// c.setAccelerator(KeyStroke.getKeyStroke("ctrl R"));
			this.installPlayEnabled(c);
			menu.add(c);
		}
		{
			JMenuItem c =
				new JMenuItem("Step Out Process", new ImageIcon(Resource.getInstance()
					.getNextOut()));
			// c.setAccelerator(KeyStroke.getKeyStroke("ctrl R"));
			this.installPlayEnabled(c);
			menu.add(c);
		}
		{
			JMenuItem c =
				new JMenuItem("Stop Process", new ImageIcon(Resource.getInstance()
					.getStop()));
			c.setAccelerator(KeyStroke.getKeyStroke("ctrl Y"));
			this.installPlayEnabled(c);
			menu.add(c);
		}
		menu.addSeparator();
		{
			JMenuItem c =
				new JMenuItem("Toggle Breakpoint", new ImageIcon(Resource.getInstance()
					.getBreakpoint()));
			c.setAccelerator(KeyStroke.getKeyStroke("ctrl shift B"));
			menu.add(c);
		}
		{
			JMenuItem c =
				new JMenuItem("Skip Breakpoints", new ImageIcon(Resource.getInstance()
					.getSkipBreakpoint()));
			menu.add(c);
		}
		{
			JMenuItem c =
				new JMenuItem("Remove All Breakpoints", new ImageIcon(Resource
					.getInstance().getRemoveAllBreakpoint()));
			menu.add(c);
		}
		return menu;
	}
	
	/**
	 * Instala o listener
	 * 
	 * @param c
	 */
	private void installPlayEnabled(final AbstractButton c) {
		IFileEvent.DEFAULT.addListener(new FileAdapter() {
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void deletedFile(IFile file) {
				c.setEnabled(!(file.getName().equals("binary.agbc") && file.getProject()
					.equals(IDesktop.DEFAULT.getSelectedProject())));
			}
			
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void createdFile(IFile file) {
				c.setEnabled((file.getName().equals("binary.agbc") && file.getProject()
					.equals(IDesktop.DEFAULT.getSelectedProject())));
			}
		});
	}
	
	/**
	 * Constroi o menu
	 * 
	 * @return menu
	 */
	private JMenu buildHelpMenu() {
		JMenu menu = new JMenu("Help");
		menu.setMnemonic('H');
		{
			JMenuItem c =
				new JMenuItem("Welcome", new ImageIcon(Resource.getInstance()
					.getWelcome()));
			menu.add(c);
		}
		{
			JMenuItem c =
				new JMenuItem("Update", new ImageIcon(Resource.getInstance()
					.getUpdate()));
			c.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					IDesktop.DEFAULT.updateIde();
				}
			});
			menu.add(c);
		}
		{
			JMenuItem c =
				new JMenuItem("About", new ImageIcon(Resource.getInstance().getAbout()));
			menu.add(c);
		}
		return menu;
	}
	
	/**
	 * Constroi o componente
	 * 
	 * @return componente
	 */
	private Component build() {
		JPanel panel = new JPanel(new BorderLayout(2, 2));
		panel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		panel.add(this.buildTool(), BorderLayout.NORTH);
		panel.add(this.buildCenter(), BorderLayout.CENTER);
		panel.add(this.buildLeft(), BorderLayout.WEST);
		panel.add(this.buildRight(), BorderLayout.EAST);
		return panel;
	}
	
	/**
	 * Constroi o componente
	 * 
	 * @return componente
	 */
	private Component buildTool() {
		JToolBar bar = new JToolBar();
		bar.setFloatable(false);
		{
			JButton c = new JButton(new ImageIcon(Resource.getInstance().getNew()));
			c.setFocusable(false);
			bar.add(c);
		}
		{
			JButton c = new JButton(new ImageIcon(Resource.getInstance().getSave()));
			c.setFocusable(false);
			c.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					IDesktop.DEFAULT.saveEditor();
				}
			});
			bar.add(c);
		}
		{
			JButton c =
				new JButton(new ImageIcon(Resource.getInstance().getSaveAll()));
			c.setFocusable(false);
			c.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					IDesktop.DEFAULT.saveAllEditors();
				}
			});
			bar.add(c);
		}
		{
			JButton c = new JButton(new ImageIcon(Resource.getInstance().getClose()));
			c.setFocusable(false);
			c.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					IDesktop.DEFAULT.closeEditor();
				}
			});
			bar.add(c);
		}
		{
			JButton c =
				new JButton(new ImageIcon(Resource.getInstance().getCloseAll()));
			c.setFocusable(false);
			c.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					IDesktop.DEFAULT.closeAllEditors();
				}
			});
			bar.add(c);
		}
		bar.addSeparator();
		{
			JButton c = new JButton(new ImageIcon(Resource.getInstance().getDebug()));
			c.setFocusable(false);
			this.installPlayEnabled(c);
			bar.add(c);
		}
		{
			JButton c = new JButton(new ImageIcon(Resource.getInstance().getPlay()));
			c.setFocusable(false);
			c.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					IDesktop.DEFAULT.runProject();
				}
			});
			this.installPlayEnabled(c);
			bar.add(c);
		}
		{
			JButton c = new JButton(new ImageIcon(Resource.getInstance().getPause()));
			c.setFocusable(false);
			this.installPlayEnabled(c);
			bar.add(c);
		}
		{
			JButton c = new JButton(new ImageIcon(Resource.getInstance().getStop()));
			c.setFocusable(false);
			this.installPlayEnabled(c);
			bar.add(c);
		}
		bar.addSeparator();
		{
			projectBuilderButton =
				new JButton(new ImageIcon(Resource.getInstance().getProject()));
			projectBuilderButton.setFocusable(false);
			projectBuilderButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					onAgbProjectBuilder();
				}
			});
			bar.add(projectBuilderButton);
		}
		{
			packageBuilderButton =
				new JButton(new ImageIcon(Resource.getInstance().getPackage()));
			packageBuilderButton.setFocusable(false);
			packageBuilderButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					onAgbPackageBuilder();
				}
			});
			bar.add(packageBuilderButton);
		}
		{
			classBuilderButton =
				new JButton(new ImageIcon(Resource.getInstance().getClassImage()));
			classBuilderButton.setFocusable(false);
			classBuilderButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					onAgbClassBuilder();
				}
			});
			bar.add(classBuilderButton);
		}
		bar.addSeparator();
		{
			JButton c = new JButton(new ImageIcon(Resource.getInstance().getBack()));
			c.setFocusable(false);
			bar.add(c);
		}
		{
			JButton c = new JButton(new ImageIcon(Resource.getInstance().getNext()));
			c.setFocusable(false);
			bar.add(c);
		}
		bar.addSeparator();
		return bar;
	}
	
	/**
	 * Ação de criar uma classe
	 */
	protected void onAgbProjectBuilder() {
		new AGBProjectBuilderFrame(this).setVisible(true);
	}
	
	/**
	 * Ação de criar uma classe
	 */
	protected void onAgbPackageBuilder() {
		new AGBFolderBuilderFrame(this).setVisible(true);
	}
	
	/**
	 * Ação de criar uma classe
	 */
	protected void onAgbClassBuilder() {
		new AGBFileBuilderFrame(this).setVisible(true);
	}
	
	/**
	 * Ação de seleção no explorer
	 * 
	 * @param e
	 */
	protected void onExplorerSelected(TreeSelectionEvent e) {
		packageBuilderButton.setEnabled(AGBFolderBuilderFrame.accept());
		classBuilderButton.setEnabled(AGBFileBuilderFrame.accept());
	}
	
	/**
	 * Constroi o componente
	 * 
	 * @return componente
	 */
	private Component buildLeft() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(300, 300));
		{
			JTabbedPane pane = new JTabbedPane();
			pane.setFocusable(false);
			explorerTree = new ExplorerTree();
			explorerTree.getSelectionModel().addTreeSelectionListener(
				new TreeSelectionListener() {
					@Override
					public void valueChanged(TreeSelectionEvent e) {
						onExplorerSelected(e);
					}
				});
			pane.addTab("Explorer", new ImageIcon(Resource.getInstance()
				.getExplorer()), new JScrollPane(explorerTree));
			panel.add(pane);
		}
		return panel;
	}
	
	/**
	 * Constroi o componente
	 * 
	 * @return componente
	 */
	private Component buildCenter() {
		editorTab = new JTabbedPane();
		editorTab.setFocusable(false);
		return editorTab;
	}
	
	/**
	 * Constroi o componente
	 * 
	 * @return componente
	 */
	private Component buildRight() {
		JPanel panel = new JPanel(new GridLayout(2, 1));
		panel.setPreferredSize(new Dimension(300, 300));
		{
			JTabbedPane pane = new JTabbedPane();
			pane.setFocusable(false);
			panel.add(pane);
		}
		{
			JTabbedPane pane = new JTabbedPane();
			pane.setFocusable(false);
			pane.addTab("Console", this.console = new IDEConsole());
			panel.add(pane);
		}
		return panel;
	}
	
	/**
	 * Retorna
	 * 
	 * @return explorerTree
	 */
	public ExplorerTree getExplorerTree() {
		return explorerTree;
	}
	
	/**
	 * Classe que armazena o arquivo aberto
	 * 
	 * @author bernardobreder
	 */
	protected static class FileOpened {
		
		private IFile file;
		
		private String content;
		
		/**
		 * Construtor
		 * 
		 * @param file
		 * @param content
		 */
		public FileOpened(IFile file, String content) {
			super();
			this.file = file;
			this.content = content;
		}
		
		/**
		 * @return the file
		 */
		public IFile getFile() {
			return file;
		}
		
		/**
		 * @param file
		 *        the file to set
		 */
		public void setFile(IFile file) {
			this.file = file;
		}
		
		/**
		 * @return the content
		 */
		public String getContent() {
			return content;
		}
		
		/**
		 * @param content
		 *        the content to set
		 */
		public void setContent(String content) {
			this.content = content;
		}
		
	}
	
}
