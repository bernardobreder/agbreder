package com.agbreder.ide.resource;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import breder.util.util.ImageUtil;

/**
 * Armazena todas as imagens
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
  public BufferedImage getWelcome() {
    return getImage("welcome.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getUpdate() {
    return getImage("update.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getAbout() {
    return getImage("about.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getBreakpoint() {
    return getImage("breakpoint.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getSkipBreakpoint() {
    return getImage("skipbreakpoint.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getRemoveAllBreakpoint() {
    return getImage("removeallbreakpoint.gif");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getBuildAll() {
    return getImage("buildall.gif");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getBuildProject() {
    return getImage("buildproject.gif");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getBuildAuto() {
    return getImage("buildauto.gif");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getCloseProject() {
    return getImage("closeproject.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getOpenProject() {
    return getImage("openproject.gif");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getGoto() {
    return getImage("goto.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getOpen() {
    return getImage("open.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getHierarchy() {
    return getImage("hierarchy.gif");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getCallHierarchy() {
    return getImage("callhierarchy.gif");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getOpenType() {
    return getImage("opentype.gif");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getOpenResource() {
    return getImage("openfile.gif");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getSelectAll() {
    return getImage("selectall.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getDelete() {
    return getImage("delete.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getFind() {
    return getImage("find.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getReplace() {
    return getImage("replace.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getUndo() {
    return getImage("undo.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getRedo() {
    return getImage("redo.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getCopy() {
    return getImage("copy.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getCut() {
    return getImage("cut.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getPaste() {
    return getImage("paste.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getClear() {
    return getImage("clear.png");
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
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getClassImage() {
    return getImage("class.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getBack() {
    return getImage("back.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getNext() {
    return getImage("next.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getNextIn() {
    return getImage("next_in.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getNextOut() {
    return getImage("next_out.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getPackage() {
    return getImage("package.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getProject() {
    return getImage("project.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getSave() {
    return getImage("save.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getSaveAll() {
    return getImage("saveas.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getClose() {
    return getImage("close.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getCloseAll() {
    return getImage("closeall.gif");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getDebug() {
    return getImage("debug.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getPlay() {
    return getImage("play.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getPause() {
    return getImage("pause.gif");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getStop() {
    return getImage("stop.gif");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getNew() {
    return getImage("new.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getExplorer() {
    return getImage("explorer.png");
  }

  /**
   * Imagem de validação
   * 
   * @return image
   */
  public BufferedImage getFile() {
    return getImage("file.png");
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
