package com.agbreder.ide.gui.builder.agb.file;

import breder.util.swing.BFrame;

import com.agbreder.ide.gui.util.task.WriteTask;
import com.agbreder.ide.model.io.IFolder;

/**
 * Ação de criar uma classe
 * 
 * @author bernardobreder
 */
public class AGBFileBuilderAction extends WriteTask {
	
	/** Janela */
	private BFrame frame;
	
	/** Classe */
	private String className;
	
	/** Package */
	private final IFolder packageFolder;
	
	private final IFolder sourceFolder;
	
	/**
	 * Construtor
	 * 
	 * @param frame
	 * @param sourceFolder
	 * @param packageFolder
	 * @param className
	 */
	public AGBFileBuilderAction(BFrame frame, IFolder sourceFolder,
		IFolder packageFolder, String className) {
		this.frame = frame;
		this.sourceFolder = sourceFolder;
		this.packageFolder = packageFolder;
		this.className = className;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void action() throws Throwable {
		if (packageFolder != null) {
			packageFolder.mkdirs();
			packageFolder.createFile(className + ".agb");
		} else {
			sourceFolder.createFile(className + ".agb");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateUI() {
		frame.close();
	}
	
}
