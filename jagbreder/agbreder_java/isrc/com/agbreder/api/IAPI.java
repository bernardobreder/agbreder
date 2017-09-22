package com.agbreder.api;

import com.agbreder.api.app.IApplicationManager;
import com.agbreder.api.client.IClientManager;
import com.agbreder.api.gui.IGuiManager;
import com.agbreder.api.server.IServerManager;
import com.agbreder.api.thread.IThreadManager;

/**
 * Classe que centraliza as APIs
 * 
 * @author bernardobreder
 */
public interface IAPI {
	
	/** Application */
	public static final IApplicationManager APP = null;
	
	/** Application */
	public static final IGuiManager GUI = null;
	
	/** Application */
	public static final IClientManager CLIENT = null;
	
	/** Application */
	public static final IServerManager SERVER = null;
	
	/** Application */
	public static final IThreadManager THREAD = null;
	
}
