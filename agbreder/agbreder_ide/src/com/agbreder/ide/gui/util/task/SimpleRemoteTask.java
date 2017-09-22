package com.agbreder.ide.gui.util.task;

import com.agbreder.ide.gui.util.EventTask;
import com.agbreder.ide.gui.util.EventTask.EventTaskReturn;


/**
 * Implementação de uma tarefa de grande processamento.
 * 
 */
public abstract class SimpleRemoteTask extends SimpleLocalTask {

  /**
   * Indica se a thread será realizada
   * 
   * @return thread realizada
   */
  public boolean accept() {
    return true;
  }

  /**
   * Processamento em Thread separada
   * 
   * @throws Throwable
   */
  public abstract void perform() throws Throwable;

  /**
   * Processamento de erro
   * 
   * @param t
   */
  public void handler(Throwable t) {
    t.printStackTrace();
  }

  /**
   * Final da tarefa
   */
  public void end() {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void start() {
    boolean accept = EventTask.invokeAndWait(new EventTaskReturn<Boolean>() {
      @Override
      public Boolean run() {
        return accept();
      }
    });
    if (accept) {
      new Thread(this.getClass().getSimpleName()) {
        @Override
        public void run() {
          try {
            perform();
            if (!isStop()) {
              EventTask.invokeLater(new Runnable() {
                @Override
                public void run() {
                  updateUI();
                }
              });
            }
          }
          catch (final Throwable t) {
            EventTask.invokeLater(new Runnable() {
              @Override
              public void run() {
                handler(t);
              }
            });
          }
          finally {
            EventTask.invokeLater(new Runnable() {
              @Override
              public void run() {
                end();
              }
            });
          }
        }
      }.start();
    }
    else {
      EventTask.invokeLater(new Runnable() {
        @Override
        public void run() {
          end();
        }
      });
    }
  }

}
