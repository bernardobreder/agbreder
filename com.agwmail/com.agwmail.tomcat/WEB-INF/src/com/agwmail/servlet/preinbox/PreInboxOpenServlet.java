package com.agwmail.servlet.preinbox;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agwmail.model.mail.PreMail;
import com.agwmail.model.user.User;
import com.agwmail.service.ServiceLocator;
import com.agwmail.servlet.core.ObjectOnlineServlet;
import com.rootlang.compiler.RootLangCompiler;

/**
 * Servlet inicial
 * 
 * 
 * @author Bernardo Breder
 */
public class PreInboxOpenServlet extends ObjectOnlineServlet {

  /**
   * {@inheritDoc}
   */
  @Override
  public Object build(HttpServletRequest req, HttpServletResponse resp,
    User user) throws Exception {
    List<PreMail> mails = ServiceLocator.mail.getPreMails(user.getId());
    for (int n = 0; n < mails.size(); n++) {
      PreMail mail = mails.get(n);
      String text = mail.getText();
      int begin = text.indexOf("{{");
      while (begin >= 0) {
        int end = text.indexOf("}}", begin + 2);
        if (end >= 0) {
          String code = text.substring(begin + 2, end);
          ByteArrayOutputStream output = new ByteArrayOutputStream();
          List<InputStream> list = new ArrayList<InputStream>();
          list.add(new ByteArrayInputStream(code.getBytes()));
          RootLangCompiler.compile(output, list);
          String base16 = new String(output.toByteArray());
          text = text.substring(0, begin + 2) + base16 + text.substring(end);
          begin = text.indexOf("{{", end + 2);
        }
      }
      mail.setCode(text);
    }
    return mails;
  }

  /**
   * Converte para vm
   * 
   * @param text
   * @return opcodes
   * @throws Exception
   */
  private static String execute(String text) throws Exception {
    int begin = text.indexOf("{{");
    while (begin >= 0) {
      int end = text.indexOf("}}", begin + 2);
      if (end >= 0) {
        String code = text.substring(begin + 2, end);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        List<InputStream> list = new ArrayList<InputStream>();
        list.add(new ByteArrayInputStream(code.getBytes()));
        RootLangCompiler.compile(output, list);
        String base16 = new String(output.toByteArray());
        text = text.substring(0, begin + 2) + base16 + text.substring(end);
        begin = text.indexOf("{{", end + 2);
      }
    }
    return text;
  }

  public static void main(String[] args) throws Exception {
    System.out.println(execute("1+1 = {{return 1+1}} e {{return 1+1}}"));
  }

}
