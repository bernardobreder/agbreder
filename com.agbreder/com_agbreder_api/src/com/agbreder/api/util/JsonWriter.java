package com.agbreder.api.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Escreve para json
 * 
 * 
 * @author Bernardo Breder
 */
public class JsonWriter {

  /** Objeto */
  private Object object;

  /** Cache de campos */
  private static final Map<Class<?>, Field[]> fields =
    new HashMap<Class<?>, Field[]>();

  /**
   * Construtor
   * 
   * @param object
   */
  public JsonWriter(Object object) {
    this.object = object;
  }

  /**
   * Retorna o objeto
   * 
   * @return objeto
   */
  public Object getObject() {
    return this.object;
  }

  /**
   * Conteudo do objeto {@inheritDoc}
   */
  @Override
  public String toString() {
    return this.toString(this.object);
  }

  /**
   * Constroi o conteudo do objeto
   * 
   * @param object
   * @return campo
   */
  private String toString(Object object) {
    if (object == null) {
      return "null";
    }
    if (object instanceof String) {
      String text = object.toString();
      text = text.replace("\\", "\\\\");
      text = text.replace("/", "\\/");
      text = text.replace("\"", "\\\"");
      text = text.replace("\r", "\\r");
      text = text.replace("\n", "\\n");
      text = text.replace("\t", "\\t");
      text = text.replace("\b", "\\b");
      text = text.replace("\f", "\\f");
      return "\"" + text + "\"";
    }
    else if (object instanceof Boolean) {
      return object.toString();
    }
    else if (object instanceof Number) {
      return object.toString();
    }
    else if (object instanceof Date) {
      return Long.toString(((Date) object).getTime());
    }
    else if (object instanceof List<?>) {
      List<?> list = (List<?>) object;
      StringBuilder sb = new StringBuilder();
      sb.append("[");
      for (int n = 0; n < list.size(); n++) {
        Object item = list.get(n);
        sb.append(this.toString(item));
        if (n != list.size() - 1) {
          sb.append(",");
        }
      }
      sb.append("]");
      return sb.toString();
    }
    else {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      Field[] fields = getFields(object.getClass());
      for (Field field : fields) {
        String name = field.getName();
        try {
          Object value = field.get(object);
          if (value != null) {
            if (sb.length() > 1) {
              sb.append(",");
            }
            sb.append("\"" + name + "\"");
            sb.append(":");
            sb.append(this.toString(value));
          }
        }
        catch (Exception e) {
        }
      }
      if (fields.length > 0) {
        sb.append(",");
      }
      sb.append("\"class\":\"" + object.getClass().getSimpleName() + "\"");
      sb.append("}");
      return sb.toString();
    }
  }

  /**
   * Recupera o campo
   * 
   * @param c
   * @return campo
   */
  private static Field[] getFields(Class<?> c) {
    Field[] fields = JsonWriter.fields.get(c);
    if (fields == null) {
      List<Field> list = new ArrayList<Field>();
      while (c != null) {
        for (Field field : c.getDeclaredFields()) {
          String name = field.getName();
          if (!Modifier.isStatic(field.getModifiers())) {
            if (!name.equals("serialVersionUID")) {
              field.setAccessible(true);
              list.add(field);
            }
          }
        }
        c = c.getSuperclass();
      }
      fields = list.toArray(new Field[list.size()]);
      JsonWriter.fields.put(c, fields);
    }
    return fields;
  }

}
