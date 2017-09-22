package com.agbreder.compiler.util;


/**
 * Codificador base64
 * 
 * 
 * @author Tecgraf
 */
public abstract class Base64 {

  /**
   * 
   * Codifica bytes
   * 
   * @param bytes
   * @return string
   */
  public static String encode(byte[] bytes) {
    return new String(encode(bytes, 0, bytes.length));
  }

  /** Mapping table from 6-bit nibbles to Base64 characters. **/
  private static final char[] map1 = new char[64];

  /** Mapping table from Base64 characters to 6-bit nibbles. */
  private static final byte[] map2 = new byte[128];

  /**
   * Inicializa as constantes
   */
  static {
    {
      int i = 0;
      for (char c = 'A'; c <= 'Z'; c++) {
        map1[i++] = c;
      }
      for (char c = 'a'; c <= 'z'; c++) {
        map1[i++] = c;
      }
      for (char c = '0'; c <= '9'; c++) {
        map1[i++] = c;
      }
      map1[i++] = '+';
      map1[i++] = '/';
    }
    {
      for (int i = 0; i < map2.length; i++) {
        map2[i] = -1;
      }
      for (int i = 0; i < 64; i++) {
        map2[map1[i]] = (byte) i;
      }
    }
  }

  /**
   * Encodes a byte array into Base64 format. No blanks or line breaks are
   * inserted in the output.
   * 
   * @param in An array containing the data bytes to be encoded.
   * @param iOff Offset of the first byte in <code>in</code> to be processed.
   * @param iLen Number of bytes to process in <code>in</code>, starting at
   *        <code>iOff</code>.
   * @return A character array containing the Base64 encoded data.
   */
  private static char[] encode(byte[] in, int iOff, int iLen) {
    int oDataLen = (iLen * 4 + 2) / 3; // output length without padding
    int oLen = ((iLen + 2) / 3) * 4; // output length including padding
    char[] out = new char[oLen];
    int ip = iOff;
    int iEnd = iOff + iLen;
    int op = 0;
    while (ip < iEnd) {
      int i0 = in[ip++] & 0xff;
      int i1 = ip < iEnd ? in[ip++] & 0xff : 0;
      int i2 = ip < iEnd ? in[ip++] & 0xff : 0;
      int o0 = i0 >>> 2;
      int o1 = ((i0 & 3) << 4) | (i1 >>> 4);
      int o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
      int o3 = i2 & 0x3F;
      out[op++] = map1[o0];
      out[op++] = map1[o1];
      out[op] = op < oDataLen ? map1[o2] : '=';
      op++;
      out[op] = op < oDataLen ? map1[o3] : '=';
      op++;
    }
    return out;
  }

}
