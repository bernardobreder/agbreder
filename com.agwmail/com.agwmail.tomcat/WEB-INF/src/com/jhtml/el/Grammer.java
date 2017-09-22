package com.jhtml.el;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.jhtml.WTag;
import com.jhtml.el.node.ELNode;
import com.jhtml.el.node.StringNode;
import com.jhtml.el.node.SumNode;
import com.jhtml.el.parser.EL;

/**
 * Language
 * 
 * 
 * @author Bernardo Breder
 */
public class Grammer {

  /**
   * Calcula uma expressão
   * 
   * @param code
   * @return valor da expressão
   */
  public static ELNode read(String code) {
    int begin = code.indexOf("${");
    if (begin < 0) {
      return new StringNode(code);
    }
    else if (begin == 0) {
      int end = code.indexOf("}", begin);
      String expr = code.substring(begin + 2, end);
      ELNode left = compile(expr);
      String rightStr = code.substring(end + 1);
      if (rightStr.length() == 0) {
        return left;
      }
      ELNode right = read(rightStr);
      return new SumNode(left, right);
    }
    else {
      ELNode left = new StringNode(code.substring(0, begin));
      ELNode right = read(code.substring(begin));
      return new SumNode(left, right);
    }
  }

  /**
   * Calcula uma expressão
   * 
   * @param code
   * @return valor da expressão
   */
  private static ELNode compile(String code) {
    try {
      EL el = new EL(new ByteArrayInputStream(code.getBytes()));
      List<ELNode> nodes = el.execute();
      if (nodes.size() == 0) {
        return null;
      }
      else {
        return nodes.get(0);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      return new StringNode("(?)");
    }
  }

  /**
   * Constroi a arvore
   * 
   * @param input
   * @return tag
   * @throws IOException
   */
  public static WTag build(InputStream input) throws IOException {
    try {
      SAXReader reader = new SAXReader();
      Document doc = reader.read(input);
      return WTag.build(doc.getRootElement());
    }
    catch (DocumentException e) {
      throw new IOException(e);
    }
  }

}
