package com.agwmail.model.mail;

import java.util.Date;

/**
 * PreMail
 * 
 * 
 * @author Bernardo Breder
 */
public class Mail {

  /** Id */
  private final int id;
  /** Subject */
  private final String subject;
  /** From User */
  private final int fromUserId;
  /** From User */
  private final String fromUserName;
  /** To User */
  private final int toUserId;
  /** To User */
  private final String toUserName;
  /** Data */
  private final Date date;
  /** Text */
  private final String text;

  /**
   * Construtor
   * 
   * @param id
   * @param subject
   * @param fromUserId
   * @param fromUserName
   * @param toUserId
   * @param toUserName
   * @param date
   * @param text
   */
  public Mail(int id, String subject, int fromUserId, String fromUserName,
    int toUserId, String toUserName, Date date, String text) {
    super();
    this.id = id;
    this.subject = subject;
    this.fromUserId = fromUserId;
    this.fromUserName = fromUserName;
    this.toUserId = toUserId;
    this.toUserName = toUserName;
    this.date = date;
    this.text = text;
  }

  /**
   * Retorna
   * 
   * @return id
   */
  public int getId() {
    return id;
  }

  /**
   * Retorna
   * 
   * @return subject
   */
  public String getSubject() {
    return subject;
  }

  /**
   * Retorna
   * 
   * @return fromUserId
   */
  public int getFromUserId() {
    return fromUserId;
  }

  /**
   * Retorna
   * 
   * @return toUserId
   */
  public int getToUserId() {
    return toUserId;
  }

  /**
   * Retorna
   * 
   * @return date
   */
  public Date getDate() {
    return date;
  }

  /**
   * Retorna
   * 
   * @return text
   */
  public String getText() {
    return text;
  }

  /**
   * Retorna
   * 
   * @return fromUserName
   */
  public String getFromUserName() {
    return fromUserName;
  }

  /**
   * Retorna
   * 
   * @return toUserName
   */
  public String getToUserName() {
    return toUserName;
  }

}
