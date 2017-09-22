package com.agbreder.ide.resource;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import breder.util.util.ImageUtil;

/**
 * Armazena todas as imagens
 * 
 * 
 * @author bbreder
 */
public class Resource {

  /** Instancia unica */
  private static final Resource instance = new Resource();

  /** Mapa de imagens */
  private static final Map<String, BufferedImage> images =
    new HashMap<String, BufferedImage>();

  /**
   * Construtor
   */
  private Resource() {
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getPlus() {
    return getImage("plus.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getMinus() {
    return getImage("minus.png");
  }

  /**
   * Retorna a imagem
   * 
   * @param name
   * @return imagem
   */
  public static BufferedImage getImage(String name) {
    BufferedImage image = images.get(name);
    if (image == null) {
      image = ImageUtil.load("com/agbreder/ide/resource/" + name);
      images.put(name, image);
    }
    return image;
  }

  /**
   * Instancia unica
   * 
   * @return owner
   */
  public static Resource getInstance() {
    return instance;
  }

}
