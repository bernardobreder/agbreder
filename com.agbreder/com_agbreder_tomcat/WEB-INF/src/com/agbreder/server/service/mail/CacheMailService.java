package com.agbreder.server.service.mail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import breder.util.util.ThreadCacheMap;

import com.agbreder.server.model.AGMail;
import com.agbreder.server.service.AbstractService;

public class CacheMailService extends AbstractService<MailService> implements
	MailService {
	
	/** Gerador de números */
	private static final Random RANDOM = new Random(System.currentTimeMillis());
	
	/** Lista vazia */
	private static final ArrayList<AGMail> EMPTRY_LIST = new ArrayList<AGMail>(0);
	
	/** Indica o máximo para um usuário tratar */
	private static final int MAX_LIST_SIZEF = 10;
	
	/** Cache que indica se tem Elemento na lista */
	private final Map<Integer, Boolean> listCache =
		new ThreadCacheMap<Integer, Boolean>(8 * 1024);
	
	/** Cache que indica se tem Elemento na lista */
	private final Map<Integer, Object> mailReply =
		new Hashtable<Integer, Object>();
	
	/**
	 * Construtor
	 * 
	 * @param next
	 */
	public CacheMailService(MailService next) {
		super(next);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int send(int fromUserId, int toUserId, String subject, String input)
		throws IOException, SQLException {
		int id = this.getNext().send(fromUserId, toUserId, subject, input);
		listCache.remove(toUserId);
		return id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reply(int userId, int mailId, String text) throws IOException,
		SQLException {
		this.getNext().reply(userId, mailId, text);
		this.mailReply.put(mailId, this.mailReply);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AGMail pop(int userId, int mailId) throws IOException, SQLException {
		if (this.mailReply.remove(mailId) == null) {
			return null;
		} else {
			return this.getNext().pop(userId, mailId);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<AGMail> list(int userId) throws IOException, SQLException {
		Boolean cache = listCache.get(userId);
		if (cache == null || cache == true) {
			List<AGMail> list = this.getNext().list(userId);
			listCache.put(userId, list.size() != 0);
			if (list.size() > 0) {
				if (list.size() > MAX_LIST_SIZEF) {
					int index = RANDOM.nextInt(list.size() - MAX_LIST_SIZEF);
					list = list.subList(index, index + MAX_LIST_SIZEF);
				}
			}
			return list;
		}
		return EMPTRY_LIST;
	}
	
}
