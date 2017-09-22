package com.agwmail.model.mail;

import java.util.Date;

/**
 * PreMail
 * 
 * 
 * @author Bernardo Breder
 */
public class PreMail {

  /** Id */
  private final int id;
  /** Subject */
  private final String subject;
  /** From User */
  private final int fromUserId;
  /** To User */
  private final int toUserId;
  /** Data */
  private final Date date;
  /** Text */
  private final String text;
  /** Text */
  private String code;

  /**
   * Construtor
   * 
   * @param id
   * @param subject
   * @param fromUserId
   * @param toUserId
   * @param date
   * @param text
   */
  public PreMail(int id, String subject, int fromUserId, int toUserId,
    Date date, String text) {
    super();
    this.id = id;
    this.subject = subject;
    this.fromUserId = fromUserId;
    this.toUserId = toUserId;
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
   * @return code
   */
  public String getCode() {
    return code;
  }

  /**
   * @param code
   */
  public void setCode(String code) {
    this.code = code;
  }

}
