package com.agbreder.server.model.user;

import java.util.Hashtable;
import java.util.Map;

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
  }

  /**
   * Retorna o contexto
   * 
   * @param session
   * @return contexto
   */
  public UserContext getUserContext(String session) {
    UserContext userContext = contexts.get(session);
    if (userContext == null) {
      return null;
    }
    return userContext;
  }

  /**
   * Atribui um contexto
   * 
   * @param session
   * 
   * @param context
   */
  public void setUserContext(String session, UserContext context) {
    this.contexts.put(session, context);
  }

  /**
   * Remove o contexto
   */
  public void removeUserContext(String session) {
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

}
