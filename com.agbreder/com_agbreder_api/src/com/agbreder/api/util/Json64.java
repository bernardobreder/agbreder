package com.agbreder.api.util;

import java.io.IOException;

/**
 * Json de JavaScript
 * 
 * 
 * @author Bernardo Breder
 */
public class Json64 {

  /**
   * Encode to Json
   * 
   * @param value
   * @return json
   */
  public static String encode(Object value) {
    return Base64.encodeString(new JsonWriter(value).toString());
  }

  /**
   * Encode to Json
   * 
   * @param base64
   * @return json
   * @throws IOException
   */
  public static Object decode(String base64) throws IOException {
    return new JsonReader(new StringInputStream(Base64.decodeString(base64)))
      .readObject();
  }

}
