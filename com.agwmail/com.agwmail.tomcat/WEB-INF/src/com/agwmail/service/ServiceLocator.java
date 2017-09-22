package com.agwmail.service;

import com.agwmail.service.agent.AgentService;
import com.agwmail.service.mail.MailService;
import com.agwmail.service.user.UserService;

/**
 * Locator de serviço
 * 
 * 
 * @author Bernardo Breder
 */
public class ServiceLocator {

  /** Serviço */
  public static final UserService user = new UserService();
  /** Serviço */
  public static final MailService mail = new MailService();
  /** Serviço */
  public static final AgentService agent = new AgentService();

}
