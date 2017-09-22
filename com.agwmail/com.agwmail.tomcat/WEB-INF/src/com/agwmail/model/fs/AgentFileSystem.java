package com.agwmail.model.fs;

import java.util.Collections;
import java.util.Comparator;

/**
 * Sistema de arquivo para Agente
 * 
 * 
 * @author Bernardo Breder
 */
public class AgentFileSystem extends AgentFolder {

  /** Root */
  public static final AgentFolder ROOT = new AgentFileSystem();

  /**
   * Construtor
   */
  public AgentFileSystem() {
    super(1, null, "");
    this.children.add(new AgentProcessSet(this));
    this.children.add(new AgentFunctionSet(this));
    Collections.sort(this.children, new Comparator<AgentResource>() {
      @Override
      public int compare(AgentResource o1, AgentResource o2) {
        return o1.getName().compareTo(o2.getName());
      }
    });
  }
}
