import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;

import com.agbreder.client.BrowserFrame;
import com.agbreder.client.util.task.EventTask;
import com.agbreder.service.server.SlaveServerThread;

/**
 * Classe inicializado
 * 
 * @author bernardobreder
 */
public class Main {

  /**
   * Inicializa
   * 
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    EventTask.invokeLater(new Runnable() {
      @Override
      public void run() {
        try {
          UIManager.setLookAndFeel(MetalLookAndFeel.class.getName());
        }
        catch (Exception e) {
        }
        BrowserFrame.getInstance().setVisible(true);
        final SlaveServerThread server = new SlaveServerThread();
        BrowserFrame.getInstance().addWindowListener(new WindowAdapter() {

          @Override
          public void windowOpened(WindowEvent e) {
            server.start();
          }

          @Override
          public void windowClosed(WindowEvent e) {
            server.close();
          }

        });
      }
    });
  }
}
