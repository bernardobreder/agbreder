package com.bp2pb.deploy;


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
    return "bp2pb";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected File[] getServerDirs() {
    return new File[] {};
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected File getTomcatProject() {
    return new File("../bp2pb.tomcat");
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
