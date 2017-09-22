package com.agbreder.vm;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe da linguagem Root Lang
 * 
 * 
 * @author Bernardo Breder
 */
public class AGBVm {

  /** Grupo de Instrução */
  public static final byte GP_VM = 1;
  /** Grupo de Instrução */
  public static final byte GP_STACK = 2;
  /** Grupo de Instrução */
  public static final byte GP_DEF = 3;
  /** Grupo de Instrução */
  public static final byte GP_LOAD = 4;
  /** Grupo de Instrução */
  public static final byte GP_JUMP = 5;
  /** Grupo de Instrução */
  public static final byte GP_BOOL = 6;
  /** Grupo de Instrução */
  public static final byte GP_INT = 7;
  /** Grupo de Instrução */
  public static final byte GP_NUM = 8;
  /** Grupo de Instrução */
  public static final byte GP_STR = 9;
  /** Grupo de Instrução */
  public static final byte GP_ARRAY = 10;
  /** Grupo de Instrução */
  public static final byte GP_USER = 32;

  /** Instrução */
  public static final byte OP_VM_HALF = 1;
  /** Instrução */
  public static final byte OP_VM_GC = 2;
  /** Instrução */
  public static final byte OP_STACK_PUSH = 1;
  /** Instrução */
  public static final byte OP_STACK_POP = 2;
  /** Instrução */
  public static final byte OP_STACK_LOAD = 3;
  /** Instrução */
  public static final byte OP_STACK_STORE = 4;
  /** Instrução */
  public static final byte OP_STACK_INT = 5;
  /** Instrução */
  public static final byte OP_DEF_NUM = 2;
  /** Instrução */
  public static final byte OP_DEF_STR = 3;
  /** Instrução */
  public static final byte OP_DEF_CLASS = 5;
  /** Instrução */
  public static final byte OP_DEF_CAST = 6;
  /** Instrução */
  public static final byte OP_DEF_NUM_LEN = 8;
  /** Instrução */
  public static final byte OP_DEF_STR_LEN = 9;
  /** Instrução */
  public static final byte OP_DEF_CLASS_LEN = 10;
  /** Instrução */
  public static final byte OP_DEF_CAST_LEN = 11;
  /** Instrução */
  public static final byte OP_LOAD_TRUE = 1;
  /** Instrução */
  public static final byte OP_LOAD_FALSE = 2;
  /** Instrução */
  public static final byte OP_LOAD_NUM = 4;
  /** Instrução */
  public static final byte OP_LOAD_STR = 5;
  /** Instrução */
  public static final byte OP_LOAD_NULL = 6;
  /** Instrução */
  public static final byte OP_JUMP_STACK = 1;
  /** Instrução */
  public static final byte OP_JUMP_INT = 2;
  /** Instrução */
  public static final byte OP_JUMP_TRUE = 3;
  /** Instrução */
  public static final byte OP_JUMP_FALSE = 4;
  /** Instrução */
  public static final byte OP_JUMP_CALL = 5;
  /** Instrução */
  public static final byte OP_JUMP_RETURN = 6;
  /** Instrução */
  public static final byte OP_BOOL_NOT = 1;
  /** Instrução */
  public static final byte OP_BOOL_OR = 2;
  /** Instrução */
  public static final byte OP_BOOL_AND = 3;
  /** Instrução */
  public static final byte OP_BOOL_EQUAL = 4;
  /** Instrução */
  public static final byte OP_BOOL_NEQUAL = 5;
  /** Instrução */
  public static final byte OP_BOOL_TO_STR = 6;
  /** Instrução */
  public static final byte OP_NUM_NEG = 1;
  /** Instrução */
  public static final byte OP_NUM_SUM = 2;
  /** Instrução */
  public static final byte OP_NUM_SUB = 3;
  /** Instrução */
  public static final byte OP_NUM_MUL = 4;
  /** Instrução */
  public static final byte OP_NUM_DIV = 5;
  /** Instrução */
  public static final byte OP_NUM_EQUAL = 6;
  /** Instrução */
  public static final byte OP_NUM_NEQUAL = 7;
  /** Instrução */
  public static final byte OP_NUM_GT = 8;
  /** Instrução */
  public static final byte OP_NUM_EGT = 9;
  /** Instrução */
  public static final byte OP_NUM_LT = 10;
  /** Instrução */
  public static final byte OP_NUM_ELT = 11;
  /** Instrução */
  public static final byte OP_NUM_TO_STR = 12;
  /** Instrução */
  public static final byte OP_STR_LEN = 1;
  /** Instrução */
  public static final byte OP_STR_SUM = 2;
  /** Instrução */
  public static final byte OP_STR_EQUAL = 3;
  /** Instrução */
  public static final byte OP_STR_NEQUAL = 4;
  /** Instrução */
  public static final byte OP_STR_GT = 5;
  /** Instrução */
  public static final byte OP_STR_EGT = 6;
  /** Instrução */
  public static final byte OP_STR_LT = 7;
  /** Instrução */
  public static final byte OP_STR_ELT = 8;
  /** Instrução */
  public static final byte OP_ARRAY_NEW = 1;
  /** Instrução */
  public static final byte OP_ARRAY_LEN = 2;
  /** Instrução */
  public static final byte OP_ARRAY_GET = 3;
  /** Instrução */
  public static final byte OP_ARRAY_SET = 4;

  /** O tamanho máxima da pilha de objeto */
  public static final int MAX_OBJECT_STACK = 32;

  /**
   * Executa na linguagem root lang
   * 
   * @param base16
   * @return valor da pilha
   */
  public static Object loop(String base16) {
    System.out.println(base16);
    int[] irs = new int[base16.length() / 8];
    for (int n = 0, m = 0; n < base16.length(); m++) {
      irs[m] =
        ((base16.charAt(n++) - 'a') << 28) + ((base16.charAt(n++) - 'a') << 24)
          + ((base16.charAt(n++) - 'a') << 20)
          + ((base16.charAt(n++) - 'a') << 16)
          + ((base16.charAt(n++) - 'a') << 12)
          + ((base16.charAt(n++) - 'a') << 8)
          + ((base16.charAt(n++) - 'a') << 4) + (base16.charAt(n++) - 'a');
    }
    StringBuilder sb = new StringBuilder();
    List<Double> numPool = null;
    List<String> strPool = null;
    List<Integer> sir = new ArrayList<Integer>();
    int ir = 0, pc = 0;
    char c1, c2, c3, c4;
    Object[] os = new Object[MAX_OBJECT_STACK];
    int ios = -1;
    for (;;) {
      switch (irs[pc++]) {
        case GP_VM: {
          switch (irs[pc++]) {
            case OP_VM_HALF:
              if (ios < 0) {
                return null;
              }
              else {
                return os[ios];
              }
            case OP_VM_GC:
              System.gc();
              break;
            default: {
              throw new IllegalArgumentException("wrong opcode");
            }
          }
          break;
        }
        case GP_STACK: {
          switch (irs[pc++]) {
            case OP_STACK_PUSH:
              ios += irs[pc++];
              break;
            case OP_STACK_POP:
              ios -= irs[pc++];
              break;
            case OP_STACK_LOAD:
              os[ios + 1] = os[ios - irs[pc++]];
              ios++;
              break;
            case OP_STACK_STORE:
              os[ios - irs[pc++]] = os[ios];
              ios--;
              break;
            case OP_STACK_INT:
              ios++;
              os[ios] = irs[pc++];
              break;
            default: {
              throw new IllegalArgumentException("wrong opcode");
            }
          }
          break;
        }
        case GP_DEF: {
          switch (irs[pc++]) {
            case OP_DEF_NUM:
              pc++;
              pc++;
              sb.delete(0, sb.length());
              do {
                ir = irs[pc++];
                c1 = (char) ((ir & 0xFF000000) >> 24);
                c2 = (char) ((ir & 0xFF0000) >> 16);
                c3 = (char) ((ir & 0xFF00) >> 8);
                c4 = (char) (ir & 0xFF);
                if (c1 != 0) {
                  sb.append(c1);
                }
                if (c2 != 0) {
                  sb.append(c2);
                }
                if (c3 != 0) {
                  sb.append(c3);
                }
                if (c4 != 0) {
                  sb.append(c4);
                }
              } while (c4 != 0);
              numPool.add(new Double(sb.toString()));
              break;
            case OP_DEF_STR:
              pc++;
              sb.delete(0, sb.length());
              do {
                ir = irs[pc++];
                c1 = (char) ((ir & 0xFF000000) >> 24);
                c2 = (char) ((ir & 0xFF0000) >> 16);
                c3 = (char) ((ir & 0xFF00) >> 8);
                c4 = (char) (ir & 0xFF);
                if (c1 != 0) {
                  sb.append(c1);
                }
                if (c2 != 0) {
                  sb.append(c2);
                }
                if (c3 != 0) {
                  sb.append(c3);
                }
                if (c4 != 0) {
                  sb.append(c4);
                }
              } while (c4 != 0);
              strPool.add(sb.toString());
              break;
            case OP_DEF_CLASS:
              break;
            case OP_DEF_CAST:
              break;
            case OP_DEF_NUM_LEN:
              numPool = new ArrayList<Double>(irs[pc++]);
              break;
            case OP_DEF_STR_LEN:
              strPool = new ArrayList<String>(irs[pc++]);
              break;
            default: {
              throw new IllegalArgumentException("wrong opcode");
            }
          }
          break;
        }
        case GP_LOAD: {
          switch (irs[pc++]) {
            case OP_LOAD_TRUE: {
              ios++;
              os[ios] = Boolean.TRUE;
              break;
            }
            case OP_LOAD_FALSE: {
              ios++;
              os[ios] = Boolean.FALSE;
              break;
            }
            case OP_LOAD_NUM: {
              ios++;
              os[ios] = numPool.get(irs[pc++]);
              break;
            }
            case OP_LOAD_STR: {
              ios++;
              os[ios] = strPool.get(irs[pc++]);
              break;
            }
            case OP_LOAD_NULL: {
              ios++;
              os[ios] = null;
              break;
            }
            default: {
              throw new IllegalArgumentException("wrong opcode");
            }
          }
          break;
        }
        case GP_JUMP: {
          switch (irs[pc++]) {
            case OP_JUMP_STACK: {
              break;
            }
            case OP_JUMP_INT: {
              pc = irs[pc++];
              break;
            }
            case OP_JUMP_TRUE: {
              if (os[ios] == Boolean.TRUE) {
                pc = irs[pc];
              }
              else {
                pc++;
              }
              ios--;
              break;
            }
            case OP_JUMP_FALSE: {
              if (os[ios] == Boolean.FALSE) {
                pc = irs[pc];
              }
              else {
                pc++;
              }
              ios--;
              break;
            }
            case OP_JUMP_CALL: {
              sir.add(pc + 1);
              pc = irs[pc++];
              break;
            }
            case OP_JUMP_RETURN: {
              if (sir.size() == 0) {
                if (ios < 0) {
                  return null;
                }
                else {
                  return os[0];
                }
              }
              ios -= irs[pc++];
              pc = sir.remove(sir.size() - 1);
              break;
            }
            default: {
              throw new IllegalArgumentException("wrong opcode");
            }
          }
          break;
        }
        case GP_BOOL: {
          switch (irs[pc++]) {
            case OP_BOOL_OR: {
              ios--;
              os[ios] = ((Boolean) os[ios]) || ((Boolean) os[ios + 1]);
              break;
            }
            case OP_BOOL_AND: {
              ios--;
              os[ios] = ((Boolean) os[ios]) && ((Boolean) os[ios + 1]);
              break;
            }
            case OP_BOOL_NOT: {
              os[ios] = !((Boolean) os[ios]);
              break;
            }
            case OP_BOOL_EQUAL: {
              ios--;
              os[ios] = ((Boolean) os[ios]) == ((Boolean) os[ios + 1]);
              break;
            }
            case OP_BOOL_NEQUAL: {
              ios--;
              os[ios] = ((Boolean) os[ios]) == ((Boolean) os[ios + 1]);
              break;
            }
            case OP_BOOL_TO_STR: {
              os[ios] = ((Boolean) os[ios]).toString();
              break;
            }
            default: {
              throw new IllegalArgumentException("wrong opcode");
            }
          }
          break;
        }
        case GP_NUM: {
          switch (irs[pc++]) {
            case OP_NUM_NEG: {
              os[ios] = ((Double) os[ios]) * -1;
              break;
            }
            case OP_NUM_SUM: {
              ios--;
              os[ios] = ((Double) os[ios]) + ((Double) os[ios + 1]);
              break;
            }
            case OP_NUM_SUB: {
              ios--;
              os[ios] = ((Double) os[ios]) - ((Double) os[ios + 1]);
              break;
            }
            case OP_NUM_MUL: {
              ios--;
              os[ios] = ((Double) os[ios]) * ((Double) os[ios + 1]);
              break;
            }
            case OP_NUM_DIV: {
              ios--;
              os[ios] = ((Double) os[ios]) / ((Double) os[ios + 1]);
              break;
            }
            case OP_NUM_EQUAL: {
              ios--;
              os[ios] = ((Double) os[ios]).equals((os[ios + 1]));
              break;
            }
            case OP_NUM_NEQUAL: {
              ios--;
              os[ios] = !((Double) os[ios]).equals((os[ios + 1]));
              break;
            }
            case OP_NUM_GT: {
              ios--;
              os[ios] =
                ((Double) os[ios]).compareTo(((Double) os[ios + 1])) > 0;
              break;
            }
            case OP_NUM_EGT: {
              ios--;
              os[ios] =
                ((Double) os[ios]).compareTo(((Double) os[ios + 1])) >= 0;
              break;
            }
            case OP_NUM_LT: {
              ios--;
              os[ios] =
                ((Double) os[ios]).compareTo(((Double) os[ios + 1])) < 0;
              break;
            }
            case OP_NUM_ELT: {
              ios--;
              os[ios] =
                ((Double) os[ios]).compareTo(((Double) os[ios + 1])) <= 0;
              break;
            }
            case OP_NUM_TO_STR: {
              if (((Double) os[ios]).doubleValue() == ((Double) os[ios])
                .intValue()) {
                os[ios] = ((Integer) ((Double) os[ios]).intValue()).toString();
              }
              else {
                os[ios] = ((Double) os[ios]).toString();
              }
              break;
            }
            default: {
              throw new IllegalArgumentException("wrong opcode");
            }
          }
          break;
        }
        case GP_STR: {
          switch (irs[pc++]) {
            case OP_STR_LEN: {
              os[ios] = ((String) os[ios]).length();
              break;
            }
            case OP_STR_SUM: {
              ios--;
              os[ios] = ((String) os[ios]) + (os[ios + 1]);
              break;
            }
            case OP_STR_EQUAL: {
              ios--;
              os[ios] = ((String) os[ios]).equals((os[ios + 1]));
              break;
            }
            case OP_STR_NEQUAL: {
              ios--;
              os[ios] = !((String) os[ios]).equals((os[ios + 1]));
              break;
            }
            case OP_STR_GT: {
              ios--;
              os[ios] =
                ((String) os[ios]).compareTo(((String) os[ios + 1])) > 0;
              break;
            }
            case OP_STR_EGT: {
              ios--;
              os[ios] =
                ((String) os[ios]).compareTo(((String) os[ios + 1])) >= 0;
              break;
            }
            case OP_STR_LT: {
              ios--;
              os[ios] =
                ((String) os[ios]).compareTo(((String) os[ios + 1])) < 0;
              break;
            }
            case OP_STR_ELT: {
              ios--;
              os[ios] =
                ((String) os[ios]).compareTo(((String) os[ios + 1])) <= 0;
              break;
            }
            default: {
              throw new IllegalArgumentException("wrong opcode");
            }
          }
          break;
        }
        case GP_ARRAY: {
          switch (irs[pc++]) {
            case OP_ARRAY_NEW: {
              os[ios] = new Object[((Double) os[ios]).intValue()];
              break;
            }
            case OP_ARRAY_LEN: {
              os[ios] = Long.valueOf(((Object[]) os[ios]).length);
              break;
            }
            case OP_ARRAY_GET: {
              ios--;
              os[ios] =
                ((Object[]) os[ios])[((Double) os[ios + 1]).intValue() - 1];
              break;
            }
            case OP_ARRAY_SET: {
              ios -= 3;
              ((Object[]) os[ios + 2])[((Double) os[ios + 3]).intValue() - 1] =
                os[ios + 1];
              break;
            }
            default: {
              throw new IllegalArgumentException("wrong opcode");
            }
          }
          break;
        }
        case GP_USER: {
          switch (irs[pc++]) {
            case 0: {
              break;
            }
            default: {
              throw new IllegalArgumentException("wrong opcode");
            }
          }
          break;
        }
        default: {
          throw new IllegalArgumentException("wrong opcode");
        }
      }
    }
  }

  /**
   * Testador
   * 
   * @param args
   */
  public static void main(String[] args) {
    System.out
      .println(loop("aaaaaaadaaaaaaaiaaaaaaaaaaaaaaadaaaaaaajaaaaaaabaaaaaaadaaaaaaadaaaaaaabaaaaaaojaaaaaaaaaaaaaaacaaaaaaabaaaaaaabaaaaaaaeaaaaaaafaaaaaaaaaaaaaaacaaaaaaaeaaaaaaabaaaaaaafaaaaaaagaaaaaaaaaaaaaaabaaaaaaab"));
  }

}
