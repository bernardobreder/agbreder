package com.agbreder;

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
    return "agb";
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
    return new File("../agbreder_tomcat");
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
