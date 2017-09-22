package com.agbreder.api.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe utilitária para campos via reflection
 * 
 * 
 * @author Bernardo Breder
 */
public class FieldUtil {

  /** Mapa de campos */
  private static final Map<Class<?>, Map<String, Field>> fields =
    new HashMap<Class<?>, Map<String, Field>>();

  /**
   * Retorna o campo de uma classe
   * 
   * @param c
   * @param name
   * @return campo
   */
  public static Field getField(Class<?> c, String name) {
    return getMapFields(c).get(name);
  }

  /**
   * Retorna campos de uma classe
   * 
   * @param c
   * @return campos
   */
  public static Collection<Field> getFields(Class<?> c) {
    return getMapFields(c).values();
  }

  /**
   * Retorna o mapa de campos
   * 
   * @param clazz
   * @return mapa de campos
   */
  private static Map<String, Field> getMapFields(Class<?> clazz) {
    Map<String, Field> fields = FieldUtil.fields.get(clazz);
    if (fields == null) {
      fields = new HashMap<String, Field>();
      Class<?> c = clazz;
      while (c != null) {
        for (Field field : c.getDeclaredFields()) {
          String fieldname = field.getName();
          if (!Modifier.isStatic(field.getModifiers())) {
            if (!fieldname.equals("serialVersionUID")) {
              field.setAccessible(true);
              fields.put(field.getName(), field);
            }
          }
        }
        c = c.getSuperclass();
      }
      FieldUtil.fields.put(clazz, fields);
    }
    return fields;
  }

  /**
   * Retorna os não campos de um modificador
   * 
   * @param clazz
   * @param modifier
   * @return campos
   */
  public static Field[] getNotFields(Class<?> clazz, int modifier) {
    List<Field> list = new ArrayList<Field>();
    Class<?> c = clazz;
    while (c != Object.class) {
      Field[] fields = c.getDeclaredFields();
      for (Field field : fields) {
        if ((field.getModifiers() & modifier) == 0) {
          field.setAccessible(true);
          list.add(field);
        }
      }
      c = c.getSuperclass();
    }
    return list.toArray(new Field[0]);
  }

  /**
   * Retorna todos os campos de uma classe
   * 
   * @param clazz
   * @return campos
   */
  public static Field[] getAllFields(Class<?> clazz) {
    List<Field> list = new ArrayList<Field>();
    Class<?> c = clazz;
    while (c != Object.class) {
      Field[] fields = c.getDeclaredFields();
      for (Field field : fields) {
        field.setAccessible(true);
        list.add(field);
      }
      c = c.getSuperclass();
    }
    return list.toArray(new Field[0]);
  }

}
