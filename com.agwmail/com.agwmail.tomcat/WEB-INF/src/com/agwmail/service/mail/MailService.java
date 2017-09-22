package com.agwmail.service.mail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import breder.util.sql.DB;
import breder.util.sql.SqlTupla;

import com.agwmail.model.mail.InboxMail;
import com.agwmail.model.mail.Mail;
import com.agwmail.model.mail.PreMail;
import com.agwmail.service.IService;

/**
 * Serviço de Mensagem
 * 
 * 
 * @author Bernardo Breder
 */
public class MailService implements IService {

  /**
   * Envia um email
   * 
   * @param fromUserId
   * @param toUserId
   * @param subject
   * @param text
   * @return id
   * @throws IOException
   * @throws SQLException
   */
  public int sendPreInbox(int fromUserId, int toUserId, String subject,
    String text) throws IOException, SQLException {
    DB.getInstance()
      .write(
        "INSERT INTO agwmail$preinbox (subject, from_user_id, to_user_id, date, text) VALUES (?, ?, ?, ?, ?)",
        subject, fromUserId, toUserId, new Date(), text);
    return DB.getInstance().getSequence("agwmail$preinbox");
  }

  /**
   * Envia um email
   * 
   * @param mailId
   * @param fromUserId
   * @param fromUserName
   * @param toUserId
   * @param toUserName
   * @param subject
   * @return id
   * @throws IOException
   * @throws SQLException
   */
  public int sendInbox(int mailId, int fromUserId, String fromUserName,
    int toUserId, String toUserName, String subject) throws IOException,
    SQLException {
    DB.getInstance()
      .write(
        "INSERT INTO agwmail$inbox (subject, mail_id, from_user_id, from_user_name, to_user_id, to_user_name, date) VALUES (?, ?, ?, ?, ?, ?, ?)",
        subject, mailId, fromUserId, fromUserName, toUserId, toUserName,
        new Date());
    return DB.getInstance().getSequence("agwmail$inbox");
  }

  /**
   * Envia um email
   * 
   * @param fromUserId
   * @param fromUserName
   * @param toUserId
   * @param toUserName
   * @param subject
   * @param text
   * @return id
   * @throws IOException
   * @throws SQLException
   */
  public int sendInbox(int fromUserId, String fromUserName, int toUserId,
    String toUserName, String subject, String text) throws IOException,
    SQLException {
    int mailId =
      this.sendMail(fromUserId, fromUserName, toUserId, toUserName, subject,
        text);
    return this.sendInbox(mailId, fromUserId, fromUserName, toUserId,
      toUserName, subject);
  }

  /**
   * Envia um email
   * 
   * @param fromUserId
   * @param fromUserName
   * @param toUserId
   * @param toUserName
   * @param subject
   * @param text
   * @return id
   * @throws IOException
   * @throws SQLException
   */
  public int sendMail(int fromUserId, String fromUserName, int toUserId,
    String toUserName, String subject, String text) throws IOException,
    SQLException {
    DB.getInstance()
      .write(
        "INSERT INTO agwmail$mail "
          + "(subject, from_user_id, from_user_name, to_user_id, to_user_name, date, text) VALUES (?, ?, ?, ?, ?, ?, ?)",
        subject, fromUserId, fromUserName, toUserId, toUserName, new Date(),
        text);
    return DB.getInstance().getSequence("agwmail$mail");
  }

  /**
   * Remove um registro
   * 
   * @param userId
   * @param id
   * @throws IOException
   * @throws SQLException
   */
  public void removePreMail(int userId, int id) throws IOException,
    SQLException {
    DB.getInstance().write(
      "DELETE FROM agwmail$preinbox WHERE from_user_id = ? and id = ?", userId,
      id);
  }

  /**
   * Remove um registro
   * 
   * @param id
   * @throws IOException
   * @throws SQLException
   */
  public void removeMail(int id) throws IOException, SQLException {
    DB.getInstance().write("DELETE FROM agwmail$mail WHERE id = ?", id);
  }

  /**
   * Remove um registro
   * 
   * @param id
   * @throws IOException
   * @throws SQLException
   */
  public void removeInboxMail(int id) throws IOException, SQLException {
    DB.getInstance().write("DELETE FROM agwmail$inbox WHERE id = ?", id);
  }

  /**
   * Envia um email
   * 
   * @param userId
   * @return mails
   * @throws IOException
   * @throws SQLException
   */
  public List<PreMail> getPreMails(int userId) throws IOException, SQLException {
    List<SqlTupla> rows =
      DB.getInstance().readTupla(
        "SELECT id, subject, from_user_id, to_user_id, date, text "
          + "FROM agwmail$preinbox where from_user_id = ?", userId);
    List<PreMail> list = new ArrayList<PreMail>(rows.size());
    for (int n = 0; n < rows.size(); n++) {
      SqlTupla row = rows.get(n);
      PreMail item = buildPreMail(row);
      list.add(item);
    }
    return list;
  }

  /**
   * Retorna todos os email do inbox
   * 
   * @param userId
   * @return mails
   * @throws IOException
   * @throws SQLException
   */
  public List<InboxMail> getInboxs(int userId) throws IOException, SQLException {
    List<SqlTupla> rows =
      DB.getInstance()
        .readTupla(
          "SELECT id, subject, from_user_id, from_user_name, to_user_id, to_user_name, date, mail_id "
            + "FROM agwmail$inbox where from_user_id = ?", userId);
    List<InboxMail> list = new ArrayList<InboxMail>(rows.size());
    for (int n = 0; n < rows.size(); n++) {
      SqlTupla row = rows.get(n);
      InboxMail item = buildInbox(row);
      list.add(item);
    }
    return list;
  }

  /**
   * Retorna todos os email do inbox
   * 
   * @param userId
   * @param mailId
   * @return mails
   * @throws IOException
   * @throws SQLException
   */
  public Mail getMail(int userId, int mailId) throws IOException, SQLException {
    List<SqlTupla> rows =
      DB.getInstance()
        .readTupla(
          "SELECT id, subject, from_user_id, from_user_name, to_user_id, to_user_name, date, text "
            + "FROM agwmail$mail where from_user_id = ? and id = ?", userId,
          mailId);
    if (rows.size() == 0) {
      return null;
    }
    return buildMail(rows.get(0));
  }

  /**
   * Retorna todos os email do inbox
   * 
   * @param userId
   * @param id
   * @return mails
   * @throws IOException
   * @throws SQLException
   */
  public PreMail getPreInboxMail(int userId, int id) throws IOException,
    SQLException {
    List<SqlTupla> rows =
      DB.getInstance().readTupla(
        "SELECT id, subject, from_user_id, to_user_id, date, text "
          + "FROM agwmail$preinbox where from_user_id = ? and id = ?", userId,
        id);
    if (rows.size() == 0) {
      return null;
    }
    return buildPreMail(rows.get(0));
  }

  /**
   * Constroi um PreMail
   * 
   * @param row
   * @return premail
   */
  private PreMail buildPreMail(SqlTupla row) {
    int id = row.getInt();
    String subject = row.getString();
    int fromUserId = row.getInt();
    int toUserId = row.getInt();
    Date date = row.getDate();
    String text = row.getString();
    return new PreMail(id, subject, fromUserId, toUserId, date, text);
  }

  /**
   * Constroi um PreMail
   * 
   * @param row
   * @return premail
   */
  private InboxMail buildInbox(SqlTupla row) {
    int id = row.getInt();
    String subject = row.getString();
    int fromUserId = row.getInt();
    String fromUserName = row.getString();
    int toUserId = row.getInt();
    String toUserName = row.getString();
    Date date = row.getDate();
    int mailId = row.getInt();
    return new InboxMail(id, subject, fromUserId, fromUserName, toUserId,
      toUserName, date, mailId);
  }

  /**
   * Constroi um PreMail
   * 
   * @param row
   * @return premail
   */
  private Mail buildMail(SqlTupla row) {
    int id = row.getInt();
    String subject = row.getString();
    int fromUserId = row.getInt();
    String fromUserName = row.getString();
    int toUserId = row.getInt();
    String toUserName = row.getString();
    Date date = row.getDate();
    String text = row.getString();
    return new Mail(id, subject, fromUserId, fromUserName, toUserId,
      toUserName, date, text);
  }

}
