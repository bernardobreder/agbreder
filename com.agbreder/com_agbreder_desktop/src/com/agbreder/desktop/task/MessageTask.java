package com.agbreder.desktop.task;

import com.agbreder.api.AGMail;
import com.agbreder.desktop.gui.trayicon.AGBTrayIcon;
import com.agbreder.desktop.model.AGBrederLocator;
import com.agbreder.vm.AGBVm;

/**
 * Tarefa de Mensagem
 * 
 * 
 * @author Bernardo Breder
 */
public class MessageTask extends Thread {

  /** Mail */
  private AGMail mail;

  /**
   * Construtor
   * 
   * @param mail
   */
  public MessageTask(AGMail mail) {
    this.mail = mail;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void run() {
    System.out.println(mail);
    try {
      AGBTrayIcon.busy();
      AGBrederLocator.instance.reply(mail.getId(), this.action());
      AGBrederLocator.instance.remove(mail.getId());
    }
    catch (Throwable e) {
      e.printStackTrace();
    }
    finally {
      AGBTrayIcon.unbusy();
    }
  }

  /**
   * Executa a requisição
   * 
   * @return pagina
   * @throws Exception
   */
  private String action() throws Exception {
    String text = this.mail.getText();
    Object result = AGBVm.loop(text);
    if (result == null) {
      return "";
    }
    else {
      return result.toString();
    }
  }

}
