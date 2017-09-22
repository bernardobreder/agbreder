package com.agbreder.client.util.task;

import java.util.concurrent.locks.Lock;

/**
 * Tarefa remota de escrita
 * 
 * @author bernardobreder
 */
public abstract class ReadTask extends RemoteTask {
	
	/** Lock */
	protected final Lock read = lock.readLock();
	
	/**
	 * Realiza a ação
	 * 
	 * @throws Throwable
	 */
	public abstract void action() throws Throwable;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void perform() throws Throwable {
		this.read.lock();
		try {
			this.action();
		} finally {
			this.read.unlock();
		}
	}
	
}
