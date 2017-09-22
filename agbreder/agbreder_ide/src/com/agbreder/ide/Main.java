package com.agbreder.ide;

import java.io.IOException;
import java.net.MalformedURLException;

import breder.util.lookandfeel.LookAndFeel;
import breder.util.task.EventTask;

import com.agbreder.ide.gui.destkop.DesktopAction;

/**
 * Inicializa a IDE
 * 
 * @author Bernardo Breder
 */
public class Main {
	
	/**
	 * Inicializador
	 * 
	 * @param args
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws IllegalStateException
	 * @throws NullPointerException
	 */
	public static void main(String[] args) throws NullPointerException,
		IllegalStateException, MalformedURLException, IOException {
		EventTask.invokeLater(new Runnable() {
			@Override
			public void run() {
				LookAndFeel.getInstance().installMetal();
				DesktopAction.getInstance().setVisible(true);
			}
		});
	}
	
}
