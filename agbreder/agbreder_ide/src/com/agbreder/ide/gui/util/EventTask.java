package com.agbreder.ide.gui.util;

import java.awt.EventQueue;

import javax.swing.SwingUtilities;

/**
 * Trata eventos de swing
 * 
 * @author Bernardo Breder
 */
public class EventTask {
	
	/**
	 * Invoca na thread do swing um evento
	 * 
	 * @param r
	 */
	public static void invokeLater(Runnable r) {
		if (EventQueue.isDispatchThread()) {
			r.run();
		} else {
			EventTask.invokeInQueue(r);
		}
	}
	
	/**
	 * Invoca na thread do swing um evento
	 * 
	 * @param r
	 */
	public static void invokeInQueue(Runnable r) {
		SwingUtilities.invokeLater(r);
	}
	
	/**
	 * Invoca na thread do swing um evento
	 * 
	 * @param r
	 */
	public static void invokeAndWait(Runnable r) {
		if (EventQueue.isDispatchThread()) {
			r.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(r);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * Invoca na thread do swing um evento
	 * 
	 * @param r
	 * @return retorno da tarefa
	 */
	@SuppressWarnings("unchecked")
	public static <E> E invokeAndWait(final EventTaskReturn<E> r) {
		if (EventQueue.isDispatchThread()) {
			return r.run();
		} else {
			try {
				final Object[] result = new Object[1];
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						result[0] = r.run();
					}
				});
				return (E) result[0];
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * Retorno de uma tarefa
	 * 
	 * @author Bernardo Breder
	 * @param <E>
	 */
	public static interface EventTaskReturn<E> {
		
		/**
		 * Evento a ser executado
		 * 
		 * @return retorno do evento
		 */
		public E run();
		
	}
	
}
