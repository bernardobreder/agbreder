package com.agbreder.ide.plugin.agb.editor;

import com.agbreder.ide.gui.editor.UIEditorManager;
import com.agbreder.ide.plugin.IMenuItem;
import com.agbreder.ide.plugin.IPlugin;
import com.agbreder.ide.plugin.IView;

public class AgbPlugin implements IPlugin {

  /**
   * Construtor
   */
  public AgbPlugin() {
    super();
    UIEditorManager.getInstance().addEditor(".agb", new AgbEditor());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return "Agb Editor";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IMenuItem[] getMenuItens() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IView[] getViews() {
    return null;
  }

}
