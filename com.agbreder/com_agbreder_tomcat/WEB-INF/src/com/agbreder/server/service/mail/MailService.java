package com.agbreder.server.service.mail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.agbreder.server.model.AGMail;

/**
 * Interface de serviço de mensagem
 * 
 * @author bernardobreder
 * 
 */
public interface MailService {

  /**
   * Envia um email
   * 
   * @param fromUserId
   * @param toUserId
   * @param subject
   * @param input
   * @return id
   * @throws IOException
   * @throws SQLException
   */
  public abstract int send(int fromUserId, int toUserId, String subject,
    String input) throws IOException, SQLException;

  /**
   * Responde um mail
   * 
   * @param userId
   * @param mailId
   * @param text
   * @throws IOException
   * @throws SQLException
   */
  public abstract void reply(int userId, int mailId, String text)
    throws IOException, SQLException;

  /**
   * Retorna todos os email do inbox
   * 
   * @param userId
   * @param mailId
   * @return mails
   * @throws IOException
   * @throws SQLException
   */
  public abstract AGMail pop(int userId, int mailId) throws IOException,
    SQLException;

  /**
   * Retorna todos os email do inbox
   * 
   * @param userId
   * @return mails
   * @throws IOException
   * @throws SQLException
   */
  public abstract List<AGMail> list(int userId) throws IOException,
    SQLException;

}