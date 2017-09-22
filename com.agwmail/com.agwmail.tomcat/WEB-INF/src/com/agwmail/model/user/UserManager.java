package com.agwmail.model.user;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.agwmail.model.RequestManager;

/**
 * Gerente de usuário
 * 
 * 
 * @author Bernardo Breder
 */
public class UserManager {

  /** Instance */
  private static final UserManager instance = new UserManager();
  /** Contextos */
  private Map<String, UserContext> contexts =
    new Hashtable<String, UserContext>();

  /**
   * Construtor
   */
  private UserManager() {
    new UserManagerThread().start();
  }

  /**
   * Retorna o contexto
   * 
   * @return contexto
   */
  public UserContext getUserContext() {
    String session =
      RequestManager.getInstance().getRequest().getSession(true).getId();
    UserContext userContext = contexts.get(session);
    if (userContext == null) {
      return null;
    }
    userContext.refreshTimer();
    return userContext;
  }

  /**
   * Atribui um contexto
   * 
   * @param context
   */
  public void setUserContext(UserContext context) {
    String session =
      RequestManager.getInstance().getRequest().getSession(true).getId();
    this.contexts.put(session, context);
  }

  /**
   * Remove o contexto
   */
  public void removeUserContext() {
    String session =
      RequestManager.getInstance().getRequest().getSession(true).getId();
    contexts.remove(session);
  }

  /**
   * Retorna
   * 
   * @return instance
   */
  public static UserManager getInstance() {
    return instance;
  }

  /**
   * Thread do usermanager
   * 
   * 
   * @author Bernardo Breder
   */
  private class UserManagerThread extends Thread {

    /**
     * Construtor
     */
    public UserManagerThread() {
      this.setName("UserManagerThread");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
      for (;;) {
        try {
          synchronized (contexts) {
            List<String> list = new ArrayList<String>();
            for (String session : contexts.keySet()) {
              if (contexts.get(session).isTimeout()) {
                list.add(session);
              }
            }
            for (String session : list) {
              contexts.remove(session);
            }
          }
          Thread.sleep(1 * 60 * 60 * 1000);
        }
        catch (Throwable t) {
        }
      }
    }

  }

}
