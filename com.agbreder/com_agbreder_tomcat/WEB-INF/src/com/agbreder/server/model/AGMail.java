package com.agbreder.server.model;

import java.util.Date;

/**
 * PreMail
 * 
 * 
 * @author Bernardo Breder
 */
public class AGMail {

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
  private final String input;
  /** Text */
  private final String output;

  /**
   * Construtor
   */
  public AGMail() {
    this(-1, null, -1, -1, null, null, null);
  }

  /**
   * Construtor
   * 
   * @param id
   * @param parentId
   * @param subject
   * @param fromUserId
   * @param toUserId
   * @param date
   * @param input
   */
  public AGMail(int id, String subject, int fromUserId, int toUserId,
    Date date, String input, String output) {
    super();
    this.id = id;
    this.subject = subject;
    this.fromUserId = fromUserId;
    this.toUserId = toUserId;
    this.date = date;
    this.input = input;
    this.output = output;
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
  public String getInput() {
    return input;
  }

  /**
   * @return the output
   */
  public String getOutput() {
    return output;
  }

}
