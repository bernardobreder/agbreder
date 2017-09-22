package com.agentmy.api.test;

import org.junit.Test;

import com.agwmail.api.AgentMy;

/**
 * Testador simples
 * 
 * 
 * @author Bernardo Breder
 */
public class SimpleTest {

  /**
   * Testador
   */
  @Test
  public void init() {
    AgentMy sw = new AgentMy();
    sw.close();
  }

}
