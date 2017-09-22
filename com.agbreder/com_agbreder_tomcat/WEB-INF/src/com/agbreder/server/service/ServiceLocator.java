package com.agbreder.server.service;

import breder.util.sql.HSQL;

import com.agbreder.server.model.mail.InboxMail;
import com.agbreder.server.service.agent.AgentService;
import com.agbreder.server.service.mail.CacheMailService;
import com.agbreder.server.service.mail.MailService;
import com.agbreder.server.service.mail.SqlMailService;
import com.agbreder.server.service.source.SourceService;
import com.agbreder.server.service.user.UserService;

/**
 * Locator de serviço
 * 
 * @author Bernardo Breder
 */
public class ServiceLocator {
	
	static {
		HSQL.setMaxConnection(100);
		HSQL.setAutocommit(true);
		HSQL.setUrl("jdbc:hsqldb:mem:memdb");
	}
	
	/** Serviço */
	public static final UserService user = new UserService();
	
	/** Serviço */
	// public static final MailService mail = new CacheMailService(
	// new SqlMailService());
	public static final InboxMail mail = InboxMail.getInstance();
	
	/** Serviço */
	public static final SourceService source = new SourceService();
	
	/** Serviço */
	public static final AgentService agent = new AgentService();
	
}
