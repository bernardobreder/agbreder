package com.agbreder.api;

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
  /** Id */
  private final Integer parentId;
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

  /**
   * Construtor
   */
  public AGMail() {
    this(-1, null, null, -1, -1, null, null);
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
   * @param text
   */
  public AGMail(int id, Integer parentId, String subject, int fromUserId,
    int toUserId, Date date, String text) {
    super();
    this.id = id;
    this.parentId = parentId;
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
   * @return parentId
   */
  public Integer getParentId() {
    return parentId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "AGMail [id=" + id + ", parentId=" + parentId + ", subject="
      + subject + ", fromUserId=" + fromUserId + ", toUserId=" + toUserId
      + ", text=" + text + "]";
  }

}
