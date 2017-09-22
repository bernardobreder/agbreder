package com.agbreder.vm;

import static com.agbreder.vm.AGBVm.GP_ARRAY;
import static com.agbreder.vm.AGBVm.GP_BOOL;
import static com.agbreder.vm.AGBVm.GP_DEF;
import static com.agbreder.vm.AGBVm.GP_JUMP;
import static com.agbreder.vm.AGBVm.GP_LOAD;
import static com.agbreder.vm.AGBVm.GP_NUM;
import static com.agbreder.vm.AGBVm.GP_STACK;
import static com.agbreder.vm.AGBVm.GP_STR;
import static com.agbreder.vm.AGBVm.GP_VM;
import static com.agbreder.vm.AGBVm.OP_ARRAY_GET;
import static com.agbreder.vm.AGBVm.OP_ARRAY_LEN;
import static com.agbreder.vm.AGBVm.OP_ARRAY_NEW;
import static com.agbreder.vm.AGBVm.OP_ARRAY_SET;
import static com.agbreder.vm.AGBVm.OP_BOOL_AND;
import static com.agbreder.vm.AGBVm.OP_BOOL_EQUAL;
import static com.agbreder.vm.AGBVm.OP_BOOL_NEQUAL;
import static com.agbreder.vm.AGBVm.OP_BOOL_NOT;
import static com.agbreder.vm.AGBVm.OP_BOOL_OR;
import static com.agbreder.vm.AGBVm.OP_DEF_CAST;
import static com.agbreder.vm.AGBVm.OP_DEF_CLASS;
import static com.agbreder.vm.AGBVm.OP_DEF_NUM;
import static com.agbreder.vm.AGBVm.OP_DEF_NUM_LEN;
import static com.agbreder.vm.AGBVm.OP_DEF_STR;
import static com.agbreder.vm.AGBVm.OP_DEF_STR_LEN;
import static com.agbreder.vm.AGBVm.OP_JUMP_CALL;
import static com.agbreder.vm.AGBVm.OP_JUMP_FALSE;
import static com.agbreder.vm.AGBVm.OP_JUMP_INT;
import static com.agbreder.vm.AGBVm.OP_JUMP_RETURN;
import static com.agbreder.vm.AGBVm.OP_JUMP_STACK;
import static com.agbreder.vm.AGBVm.OP_JUMP_TRUE;
import static com.agbreder.vm.AGBVm.OP_LOAD_FALSE;
import static com.agbreder.vm.AGBVm.OP_LOAD_NUM;
import static com.agbreder.vm.AGBVm.OP_LOAD_STR;
import static com.agbreder.vm.AGBVm.OP_LOAD_TRUE;
import static com.agbreder.vm.AGBVm.OP_NUM_DIV;
import static com.agbreder.vm.AGBVm.OP_NUM_EGT;
import static com.agbreder.vm.AGBVm.OP_NUM_ELT;
import static com.agbreder.vm.AGBVm.OP_NUM_EQUAL;
import static com.agbreder.vm.AGBVm.OP_NUM_GT;
import static com.agbreder.vm.AGBVm.OP_NUM_LT;
import static com.agbreder.vm.AGBVm.OP_NUM_MUL;
import static com.agbreder.vm.AGBVm.OP_NUM_NEG;
import static com.agbreder.vm.AGBVm.OP_NUM_NEQUAL;
import static com.agbreder.vm.AGBVm.OP_NUM_SUB;
import static com.agbreder.vm.AGBVm.OP_NUM_SUM;
import static com.agbreder.vm.AGBVm.OP_STACK_INT;
import static com.agbreder.vm.AGBVm.OP_STACK_LOAD;
import static com.agbreder.vm.AGBVm.OP_STACK_POP;
import static com.agbreder.vm.AGBVm.OP_STACK_PUSH;
import static com.agbreder.vm.AGBVm.OP_STACK_STORE;
import static com.agbreder.vm.AGBVm.OP_STR_EGT;
import static com.agbreder.vm.AGBVm.OP_STR_ELT;
import static com.agbreder.vm.AGBVm.OP_STR_EQUAL;
import static com.agbreder.vm.AGBVm.OP_STR_GT;
import static com.agbreder.vm.AGBVm.OP_STR_LEN;
import static com.agbreder.vm.AGBVm.OP_STR_LT;
import static com.agbreder.vm.AGBVm.OP_STR_NEQUAL;
import static com.agbreder.vm.AGBVm.OP_STR_SUM;
import static com.agbreder.vm.AGBVm.OP_VM_GC;
import static com.agbreder.vm.AGBVm.OP_VM_HALF;

/**
 * Classe da linguagem Root Lang
 * 
 * 
 * @author Bernardo Breder
 */
public class AGBVmList {

  /** PC anterior */
  private int last = 0;
  /** Pc */
  private int pc = 0;

  /**
   * Executa na linguagem root lang
   * 
   * @param base16
   */
  public void loop(String base16) {
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
    base16 = null;
    StringBuilder sb = new StringBuilder();
    int ir = 0;
    char c1, c2, c3, c4;
    for (; pc < irs.length;) {
      switch (irs[pc++]) {
        case GP_VM: {
          switch (irs[pc++]) {
            case OP_VM_HALF:
              print("vm", "half");
              break;
            case OP_VM_GC:
              print("vm", "gc");
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
              print("stack", "push", irs[pc++]);
              break;
            case OP_STACK_POP:
              print("stack", "pop", irs[pc++]);
              break;
            case OP_STACK_LOAD:
              print("stack", "load", irs[pc++]);
              break;
            case OP_STACK_STORE:
              print("stack", "store", irs[pc++]);
              break;
            case OP_STACK_INT:
              print("stack", "int", irs[pc++]);
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
              print("def", "num", sb.toString());
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
              print("def", "str", sb.toString());
              break;
            case OP_DEF_CLASS:
              break;
            case OP_DEF_CAST:
              break;
            case OP_DEF_NUM_LEN:
              print("def", "num_len", irs[pc++]);
              break;
            case OP_DEF_STR_LEN:
              print("def", "str_len", irs[pc++]);
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
              print("load", "true");
              break;
            }
            case OP_LOAD_FALSE: {
              print("load", "false");
              break;
            }
            case OP_LOAD_NUM: {
              print("load", "num", irs[pc++]);
              break;
            }
            case OP_LOAD_STR: {
              print("load", "num", irs[pc++]);
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
              print("jump", "stack");
              break;
            }
            case OP_JUMP_INT: {
              print("jump", "int", irs[pc++]);
              break;
            }
            case OP_JUMP_TRUE: {
              print("jump", "true", irs[pc++]);
              break;
            }
            case OP_JUMP_FALSE: {
              print("jump", "false", irs[pc++]);
              break;
            }
            case OP_JUMP_CALL: {
              print("jump", "call", irs[pc++]);
              break;
            }
            case OP_JUMP_RETURN: {
              print("jump", "ret", irs[pc++]);
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
              print("bool", "or");
              break;
            }
            case OP_BOOL_AND: {
              print("bool", "and");
              break;
            }
            case OP_BOOL_NOT: {
              print("bool", "not");
              break;
            }
            case OP_BOOL_EQUAL: {
              print("bool", "equal");
              break;
            }
            case OP_BOOL_NEQUAL: {
              print("bool", "nequal");
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
              print("num", "neg");
              break;
            }
            case OP_NUM_SUM: {
              print("num", "sum");
              break;
            }
            case OP_NUM_SUB: {
              print("num", "sub");
              break;
            }
            case OP_NUM_MUL: {
              print("num", "mul");
              break;
            }
            case OP_NUM_DIV: {
              print("num", "div");
              break;
            }
            case OP_NUM_EQUAL: {
              print("num", "equal");
              break;
            }
            case OP_NUM_NEQUAL: {
              print("num", "nequal");
              break;
            }
            case OP_NUM_GT: {
              print("num", "gt");
              break;
            }
            case OP_NUM_EGT: {
              print("num", "egt");
              break;
            }
            case OP_NUM_LT: {
              print("num", "lt");
              break;
            }
            case OP_NUM_ELT: {
              print("num", "elt");
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
              print("str", "len");
              break;
            }
            case OP_STR_SUM: {
              print("str", "sum");
              break;
            }
            case OP_STR_EQUAL: {
              print("str", "equal");
              break;
            }
            case OP_STR_NEQUAL: {
              print("str", "nequal");
              break;
            }
            case OP_STR_GT: {
              print("str", "gt");
              break;
            }
            case OP_STR_EGT: {
              print("str", "egt");
              break;
            }
            case OP_STR_LT: {
              print("str", "lt");
              break;
            }
            case OP_STR_ELT: {
              print("str", "elt");
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
              print("array", "new");
              break;
            }
            case OP_ARRAY_LEN: {
              print("array", "len");
              break;
            }
            case OP_ARRAY_GET: {
              print("array", "get");
              break;
            }
            case OP_ARRAY_SET: {
              print("array", "set");
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
   * Imprime
   * 
   * @param group
   * @param opcode
   */
  private void print(String group, String opcode) {
    print(group, opcode, null);
  }

  /**
   * Imprime
   * 
   * @param group
   * @param opcode
   * @param data
   */
  private void print(String group, String opcode, Object data) {
    if (data != null) {
      System.out.println(String.format("[%d] %s.%s %s", last, group
        .toLowerCase(), opcode.toLowerCase(), data.toString()));
    }
    else {
      System.out.println(String.format("[%d] %s.%s", last, group.toLowerCase(),
        opcode.toLowerCase()));
    }
    last = pc;
  }

}
