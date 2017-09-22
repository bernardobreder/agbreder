package com.agbreder.server.model.mail;

public class Mail {
	
	/** Id */
	private Integer id;
	
	/** User From */
	private Integer fromUserId;
	
	/** User To */
	private Integer toUserId;
	
	/** Subject */
	private String subject;
	
	/** Input */
	private String input;
	
	/** Output */
	private String output;
	
	/** Sended */
	private boolean sended;
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * @param id
	 *        the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * @return the fromUserId
	 */
	public Integer getFromUserId() {
		return fromUserId;
	}
	
	/**
	 * @param fromUserId
	 *        the fromUserId to set
	 */
	public void setFromUserId(Integer fromUserId) {
		this.fromUserId = fromUserId;
	}
	
	/**
	 * @return the toUserId
	 */
	public Integer getToUserId() {
		return toUserId;
	}
	
	/**
	 * @param toUserId
	 *        the toUserId to set
	 */
	public void setToUserId(Integer toUserId) {
		this.toUserId = toUserId;
	}
	
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	
	/**
	 * @param subject
	 *        the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	/**
	 * @return the input
	 */
	public String getInput() {
		return input;
	}
	
	/**
	 * @param input
	 *        the input to set
	 */
	public void setInput(String input) {
		this.input = input;
	}
	
	/**
	 * @return the output
	 */
	public String getOutput() {
		return output;
	}
	
	/**
	 * @param output
	 *        the output to set
	 */
	public void setOutput(String output) {
		this.output = output;
	}
	
	/**
	 * @return the sended
	 */
	public boolean isSended() {
		return sended;
	}
	
	/**
	 * @param sended
	 *        the sended to set
	 */
	public void setSended(boolean sended) {
		this.sended = sended;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mail other = (Mail) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Mail [id=" + id + ", fromUserId=" + fromUserId + ", toUserId="
			+ toUserId + ", subject=" + subject + "]";
	}
	
}
