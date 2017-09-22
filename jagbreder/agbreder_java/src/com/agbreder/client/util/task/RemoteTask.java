package com.agbreder.client.util.task;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Tarefa remota
 * 
 * @author bernardobreder
 */
public abstract class RemoteTask extends SimpleRemoteTask {
	
	/** Lock */
	protected static final ReadWriteLock lock = new ReentrantReadWriteLock();
	
}
