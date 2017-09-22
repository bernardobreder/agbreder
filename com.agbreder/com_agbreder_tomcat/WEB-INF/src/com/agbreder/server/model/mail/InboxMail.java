package com.agbreder.server.model.mail;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 * Caixa de mensagem de um Usuário
 * 
 * @author bernardobreder
 */
public class InboxMail {
	
	/** Sequence */
	private int sequence;
	
	private Integer[] userIds = new Integer[1024];
	
	/** Dados dos Mails */
	private Map<Integer, Mail> mails = new HashMap<Integer, Mail>();
	
	/** Mails de um Usuário */
	private Map<Integer, Queue<Mail>> users = new HashMap<Integer, Queue<Mail>>();
	
	private static final InboxMail instance = new InboxMail();
	
	/**
	 * Construtor
	 */
	private InboxMail() {
	}
	
	/**
	 * Envia e espera a resposta
	 * 
	 * @param fromUserId
	 * @param toUserId
	 * @param subject
	 * @param input
	 * @return resposta
	 * @throws InterruptedException
	 */
	public String sendAndWait(int fromUserId, int toUserId, String subject,
		String input, int timeout) throws InterruptedException {
		Mail mail = this.send(fromUserId, toUserId, subject, input);
		long timer = System.currentTimeMillis();
		while (mail.getOutput() == null) {
			synchronized (mail) {
				mail.wait(100);
			}
			if (System.currentTimeMillis() - timer > timeout) {
				this.reply(toUserId, mail.getId(), "Timeout");
			}
		}
		return mail.getOutput();
	}
	
	/**
	 * Lista e espera caso não tenha nada
	 * 
	 * @param userId
	 * @param timeout
	 * @return mails
	 * @throws InterruptedException
	 */
	public Mail popAndWait(int userId, int timeout) throws InterruptedException {
		long timer = System.currentTimeMillis();
		Mail mail = this.pop(userId);
		if (mail != null) {
			return mail;
		}
		Integer userIdObject = this.getUserId(userId);
		while (mail == null && (System.currentTimeMillis() - timer) < timeout) {
			synchronized (userIdObject) {
				userIdObject.wait(1000);
			}
			mail = this.pop(userId);
		}
		return mail;
	}
	
	/**
	 * Envia uma mensagem
	 * 
	 * @param fromUserId
	 * @param toUserId
	 * @param subject
	 * @param input
	 * @return
	 */
	public synchronized Mail send(int fromUserId, int toUserId, String subject,
		String input) {
		if (fromUserId < 0 || toUserId < 0 || subject == null || input == null) {
			throw new IllegalArgumentException();
		}
		Integer toUserIdObject = this.getUserId(toUserId);
		Mail mail = new Mail();
		mail.setId(++sequence);
		mail.setFromUserId(fromUserId);
		mail.setToUserId(toUserIdObject);
		mail.setSubject(subject);
		mail.setInput(input);
		this.mails.put(mail.getId(), mail);
		Queue<Mail> list = this.users.get(toUserIdObject);
		System.out.println(users);
		if (list == null) {
			this.users.put(toUserIdObject, list = new ArrayDeque<Mail>(1024));
			System.out.println(users);
		}
		System.out.flush();
		list.add(mail);
		synchronized (toUserIdObject) {
			toUserIdObject.notify();
		}
		return mail;
	}
	
	/**
	 * Responde uma mensagem
	 * 
	 * @param mailId
	 * @param text
	 */
	public synchronized void reply(int userId, int mailId, String text) {
		if (mailId < 1 || text == null) {
			throw new IllegalArgumentException();
		}
		Mail mail = this.mails.remove(mailId);
		if (mail != null) {
			if (mail.getToUserId() == userId) {
				mail.setOutput(text);
				synchronized (mail) {
					mail.notify();
				}
			}
		}
	}
	
	/**
	 * Desempilha uma requisição
	 * 
	 * @param userId
	 * @return mail
	 */
	public synchronized Mail pop(int userId) {
		if (userId < 0) {
			throw new IllegalArgumentException();
		}
		Queue<Mail> list = users.get(userId);
		System.out.println(users);
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.remove();
	}
	
	/**
	 * Retorna o objecto de identificador de usuário
	 * 
	 * @param userId
	 * @return
	 */
	public Integer getUserId(int userId) {
		if (userId < 0) {
			throw new IllegalArgumentException("" + userId);
		}
		if (userId >= this.userIds.length) {
			Integer[] array = new Integer[userId + 1];
			System.arraycopy(this.userIds, 0, array, 0, this.userIds.length);
			this.userIds = array;
		}
		Integer object = this.userIds[userId];
		if (object == null) {
			return this.userIds[userId] = userId;
		} else {
			return object;
		}
	}
	
	/**
	 * @return the instance
	 */
	public static InboxMail getInstance() {
		return instance;
	}
	
}
