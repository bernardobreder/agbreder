package com.agbreder.client.util.task;

import java.util.concurrent.locks.Lock;

/**
 * Tarefa remota de escrita
 * 
 * @author bernardobreder
 */
public abstract class WriteTask extends RemoteTask {
	
	/** Lock */
	protected final Lock write = lock.writeLock();
	
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
		this.write.lock();
		try {
			this.action();
		} finally {
			this.write.unlock();
		}
	}
	
}
