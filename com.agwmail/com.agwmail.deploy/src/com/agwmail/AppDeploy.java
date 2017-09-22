package com.agwmail;

import java.io.File;

import breder.util.deploy.TomcatDeploy;

/**
 * Realiza o deploy
 * 
 * 
 * @author Bernardo Breder
 */
public class AppDeploy extends TomcatDeploy {

  /**
   * {@inheritDoc}
   */
  @Override
  protected String getProjectName() {
    return "agwmail";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected File[] getServerDirs() {
    return new File[] { new File("../breder.util") };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected File getTomcatProject() {
    return new File("../com.agwmail.tomcat");
  }

  /**
   * Inicializador
   * 
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    new AppDeploy().init();
  }

}
