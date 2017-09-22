package com.agbreder.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

import com.agbreder.api.util.Base64;
import com.agbreder.api.util.HttpRequest;
import com.agbreder.api.util.InputStreamUtil;
import com.agbreder.api.util.JsonReader;

/**
 * Estrutura de Sistema
 * 
 * 
 * @author Bernardo Breder
 */
public class AGBreder {

  /** Cliente */
  private DefaultHttpClient client;
  /** Listeners */
  private final List<AGListener> listeners = new ArrayList<AGListener>();
  /** Thread */
  private AGThread thread;
  /** Usuário */
  private String username;
  /** Senha */
  private String password;
  /** Código do usuário */
  private Integer userId;

  static {
    JsonReader.addClass(AGMail.class);
  }

  /**
   * Construtor
   * 
   * @param username
   * @param password
   * @throws AutenticatedException
   * @throws IOException
   * @throws InvalidServiceException
   */
  public AGBreder(String username, String password)
    throws AutenticatedException, IOException, InvalidServiceException {
    this.username = username;
    this.password = password;
    {
      SchemeRegistry schemeRegistry = new SchemeRegistry();
      schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory
        .getSocketFactory()));
      ClientConnectionManager cm =
        new ThreadSafeClientConnManager(schemeRegistry);
      this.client = new DefaultHttpClient(cm);
    }
    login();
  }

  /**
   * Indica se já está autenticado
   * 
   * @return autenticado
   * @throws IOException
   * @throws ServerException
   * @throws InvalidServiceException
   */
  public boolean isLogined() throws IOException, InvalidServiceException {
    try {
      return HttpRequest.get(client, "logined.do", null, null);
    }
    catch (URISyntaxException e) {
      throw new InvalidServiceException();
    }
  }

  /**
   * Envia uma mensagem para uma pessoa
   * 
   * @param to
   * @param text
   * @return código do mail
   * @throws InvalidServiceException
   * @throws IOException
   */
  public int send(String to, String text) throws InvalidServiceException,
    IOException {
    try {
      return HttpRequest.post(client, "send.do",
        new String[] { "to", "subject" }, new Object[] { to, "script" },
        new ByteArrayInputStream(Base64.encodeString(text).getBytes()));
    }
    catch (URISyntaxException e) {
      throw new InvalidServiceException();
    }
  }

  /**
   * Envia uma mensagem para uma pessoa
   * 
   * @param mailId
   * @param text
   * @throws InvalidServiceException
   * @throws IOException
   */
  public void reply(int mailId, String text) throws InvalidServiceException,
    IOException {
    try {
      HttpRequest.post(client, "reply.do", new String[] { "id" },
        new Object[] { mailId }, new ByteArrayInputStream(Base64.encodeString(
          text).getBytes()));
    }
    catch (URISyntaxException e) {
      throw new InvalidServiceException();
    }
  }

  /**
   * Lista as mensagens do usuário
   * 
   * @return mensagens
   * @throws InvalidServiceException
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  public List<AGMail> list() throws InvalidServiceException, IOException {
    try {
      return (List<AGMail>) HttpRequest.get(client, "list.do", null, null);
    }
    catch (URISyntaxException e) {
      throw new InvalidServiceException();
    }
  }

  /**
   * Envia uma mensagem para uma pessoa
   * 
   * @param id
   * @throws InvalidServiceException
   * @throws IOException
   */
  public void remove(int id) throws InvalidServiceException, IOException {
    try {
      HttpRequest.get(client, "remove.do", new String[] { "id" },
        new Object[] { id });
    }
    catch (URISyntaxException e) {
      throw new InvalidServiceException();
    }
  }

  /**
   * Publica uma stream
   * 
   * @param name
   * @param input
   * @throws IOException
   * @throws InvalidServiceException
   */
  public void publish(String name, InputStream input) throws IOException,
    InvalidServiceException {
    String text = new String(InputStreamUtil.getBytes(input), "utf-8");
    try {
      HttpRequest.post(client, "publish.do", new String[] { "name" },
        new Object[] { name }, new ByteArrayInputStream(Base64.encodeString(
          text).getBytes()));
    }
    catch (URISyntaxException e) {
      throw new InvalidServiceException();
    }
  }

  /**
   * Retorna o código do usuário
   * 
   * @return código do usuário
   */
  public int getUserId() {
    return this.userId;
  }

  /**
   * Adiciona uma listener
   * 
   * @param listener
   */
  public void addListener(AGListener listener) {
    synchronized (listeners) {
      if (listener != null) {
        this.listeners.add(listener);
      }
      if (thread == null) {
        thread = new AGThread();
        thread.start();
      }
    }
  }

  /**
   * Finaliza o modelo
   */
  public synchronized void close() {
    try {
      HttpRequest.get(client, "logout.do", null, null);
    }
    catch (Exception e) {
    }
    while (thread != null && !this.thread.isInterrupted()) {
      this.thread.interrupt();
    }
    synchronized (listeners) {
      this.listeners.clear();
    }
    this.thread = null;
    this.client = null;
  }

  /**
   * Efetua o login do usuário
   * 
   * @throws IOException
   * @throws ServerException
   * @throws AutenticatedException
   * @throws InvalidServiceException
   */
  private void login() throws IOException, ServerException,
    AutenticatedException, InvalidServiceException {
    try {
      this.userId =
        HttpRequest.get(client, "login.do", new String[] { "username",
            "password" }, new Object[] { username, password });
      if (userId == null) {
        throw new AutenticatedException(username);
      }
    }
    catch (URISyntaxException e) {
      throw new InvalidServiceException();
    }
  }

  /**
   * Thread de Listeners
   * 
   * 
   * @author Bernardo Breder
   */
  private class AGThread extends Thread {

    /** Timeout */
    private static final int TIMEOUT = 100;

    /**
     * Construtor
     */
    public AGThread() {
      super("AGBrederThread");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
      while (!this.isInterrupted()) {
        try {
          this.online();
          List<AGMail> mails = list();
          for (AGMail mail : mails) {
            this.busy(mail);
          }
          break;
        }
        catch (Throwable t) {
          this.offline();
          try {
            if (!isLogined()) {
              login();
            }
            else {
              this.handler(t);
            }
          }
          catch (Throwable e) {
            this.handler(e);
          }
        }
        finally {
          this.sleep();
        }
      }
      while (!this.isInterrupted()) {
        try {
          if (hasNewMessage()) {
            List<AGMail> mails = list();
            for (AGMail mail : mails) {
              this.busy(mail);
            }
          }
        }
        catch (Throwable t) {
          this.offline();
          try {
            if (!isLogined()) {
              login();
              online();
            }
            else {
              this.handler(t);
            }
          }
          catch (Throwable e) {
            this.handler(e);
          }
        }
        finally {
          this.sleep();
        }
      }
    }

    /**
     * Lista as mensagens do usuário
     * 
     * @return mensagens
     * @throws InvalidServiceException
     * @throws IOException
     */
    private boolean hasNewMessage() throws InvalidServiceException, IOException {
      try {
        return HttpRequest.get(client, "new.do", null, null);
      }
      catch (URISyntaxException e) {
        throw new InvalidServiceException();
      }
    }

    /**
     * Dorme
     */
    private void sleep() {
      try {
        Thread.sleep(TIMEOUT);
      }
      catch (InterruptedException e) {
      }
    }

    /**
     * Notifica uma mensagem
     * 
     * @param mail
     */
    private void busy(AGMail mail) {
      synchronized (listeners) {
        for (AGListener listener : listeners) {
          listener.busy(mail);
        }
      }
    }

    /**
     * Notifica uma mensagem
     */
    private void offline() {
      synchronized (listeners) {
        for (AGListener listener : listeners) {
          listener.offline();
        }
      }
    }

    /**
     * Notifica uma mensagem
     * 
     * @param t
     */
    private void handler(Throwable t) {
      synchronized (listeners) {
        for (AGListener listener : listeners) {
          listener.handler(t);
        }
      }
    }

    /**
     * Notifica uma mensagem
     */
    private void online() {
      synchronized (listeners) {
        for (AGListener listener : listeners) {
          listener.online();
        }
      }
    }
  }

  /**
   * Testador
   * 
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    final AGBreder agb = new AGBreder("bbreder", "bbreder");
    agb.addListener(new AGListener() {
      @Override
      public void busy(AGMail mail) {
        System.out.println(mail);
        try {
          agb.remove(mail.getId());
          agb.reply(mail.getId(), "é");
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }

      @Override
      public void offline() {
      }

      @Override
      public void online() {
      }

      @Override
      public void handler(Throwable t) {
      }
    });
    for (int n = 0; n < 10; n++) {
      agb.send("bbreder", "ae");
    }
    Thread.sleep(1000);
    for (int n = 0; n < 10; n++) {
      agb.send("bbreder", "ae");
    }
    Thread.sleep(1000000);
    agb.close();
  }

}
