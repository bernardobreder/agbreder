package com.agbreder.api.util;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reader de Json
 * 
 * 
 * @author Bernardo Breder
 */
public class JsonReader {

  /** Inputstream */
  private InputStream input;
  /** Proximo */
  private int next;
  /** Classes */
  private static final Map<String, Class<?>> classes =
    new HashMap<String, Class<?>>();

  static {
    addClass(Exception.class);
    addClass(Throwable.class);
    addClass(Error.class);
  }

  /**
   * Construtor
   * 
   * @param input
   */
  public JsonReader(InputStream input) {
    this.input = input;
    this.next = -1;
  }

  /**
   * Ler o objeto
   * 
   * @return obj
   * @throws IOException
   */
  public Object readObject() throws IOException {
    StringBuilder sb = new StringBuilder();
    Map<String, Object> map = new HashMap<String, Object>();
    int c = this.read();
    if (c == '[') {
      List<Object> list = new ArrayList<Object>();
      if (this.lookahead() != ']') {
        Object value = this.readObject();
        list.add(value);
        while (this.lookahead() == ',') {
          this.read();
          value = this.readObject();
          list.add(value);
        }
      }
      this.read();
      return list;
    }
    else if (c == '{') {
      map.clear();
      if (this.lookahead() != '}') {
        for (;;) {
          String key = this.readObject().toString().trim();
          this.read();
          Object value = this.readObject();
          map.put(key, value);
          if (this.lookahead() == ',') {
            this.read();
          }
          else {
            break;
          }
        }
      }
      this.read();
      String classValue = map.get("class").toString();
      try {
        Class<?> loadClass = classes.get(classValue);
        Object object = loadClass.newInstance();
        for (String key : map.keySet()) {
          if (!key.equals("class")) {
            Field field = FieldUtil.getField(loadClass, key);
            Object value = map.get(key);
            if (field.getType() == Date.class && value.getClass() == Long.class) {
              field.set(object, new Date((Long) value));
            }
            else {
              field.set(object, value);
            }
          }
        }
        return object;
      }
      catch (Exception e) {
        return new Object();
      }
    }
    else if (c >= '0' && c <= '9') {
      sb.delete(0, sb.length());
      boolean flag = false;
      do {
        sb.append((char) c);
        c = this.lookahead();
        flag = (c >= '0' && c <= '9') || c == '.';
        if (flag) {
          c = this.read();
        }
      } while (flag);
      if (sb.length() < Integer.valueOf(Integer.MAX_VALUE).toString().length()) {
        return new Integer(sb.toString());
      }
      else {
        return new Long(sb.toString());
      }
    }
    else if (c == '\"') {
      c = this.read();
      sb.delete(0, sb.length());
      while (c != '\"') {
        sb.append((char) c);
        c = this.read();
      }
      return sb.toString();
    }
    else if (c == 't' && this.lookahead() == 'r') {
      if (this.read() != 'r') {
        throw new RuntimeException();
      }
      if (this.read() != 'u') {
        throw new RuntimeException();
      }
      if (this.read() != 'e') {
        throw new RuntimeException();
      }
      return true;
    }
    else if (c == 'f' && this.lookahead() == 'a') {
      if (this.read() != 'a') {
        throw new RuntimeException();
      }
      if (this.read() != 'l') {
        throw new RuntimeException();
      }
      if (this.read() != 's') {
        throw new RuntimeException();
      }
      if (this.read() != 'e') {
        throw new RuntimeException();
      }
      return false;
    }
    else if (c == 'n' && this.lookahead() == 'u') {
      if (this.read() != 'u') {
        throw new RuntimeException();
      }
      if (this.read() != 'l') {
        throw new RuntimeException();
      }
      if (this.read() != 'l') {
        throw new RuntimeException();
      }
      return null;
    }
    else {
      sb.delete(0, sb.length());
      sb.append((char) c);
      c = lookahead();
      while ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_') {
        c = this.read();
        sb.append((char) c);
        c = lookahead();
      }
      return sb.toString();
    }
  }

  /**
   * Realiza a leitura
   * 
   * @return leitura
   * @throws IOException
   */
  public int read() throws IOException {
    if (next < 0) {
      int read = this.input.read();
      next = this.input.read();
      return read;
    }
    else {
      int read = this.next;
      this.next = input.read();
      return read;
    }
  }

  /**
   * Realiza o lookahead
   * 
   * @return lookahead
   */
  public int lookahead() {
    return this.next;
  }

  /**
   * Adiciona uma classe
   * 
   * @param c
   */
  public static void addClass(Class<?> c) {
    classes.put(c.getSimpleName(), c);
  }

  /**
   * Testador
   * 
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    System.out.println(new JsonReader(new ByteArrayInputStream(
      "[\"a\",2,\"ab\",false,true,{\"a\":1}]".getBytes())).readObject());
    System.out.println(new JsonReader(new FileInputStream("data/a.0"))
      .readObject());
  }
}
