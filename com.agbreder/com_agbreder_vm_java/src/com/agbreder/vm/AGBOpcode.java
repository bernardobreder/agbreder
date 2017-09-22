package com.agbreder.vm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Criador de bytecode
 * 
 * 
 * @author Bernardo Breder
 */
public class AGBOpcode {

  /** Tamanho */
  private int size;
  /** Saída de arquivo */
  private OutputStream output;

  /**
   * Construtor
   */
  public AGBOpcode() {
    this(new ByteArrayOutputStream());
  }

  /**
   * Construtor
   * 
   * @param output
   */
  public AGBOpcode(OutputStream output) {
    this.output = output;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opVmHalf() throws IOException {
    this.append(op(AGBVm.GP_VM, AGBVm.OP_VM_HALF));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opVmGc() throws IOException {
    this.append(op(AGBVm.GP_VM, AGBVm.OP_VM_GC));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @param count
   * @return this
   * @throws IOException
   */
  public AGBOpcode opStackPush(int count) throws IOException {
    if (count > 0) {
      this.append(op(AGBVm.GP_STACK, AGBVm.OP_STACK_PUSH, count));
    }
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @param count
   * @return this
   * @throws IOException
   */
  public AGBOpcode opStackPop(int count) throws IOException {
    if (count > 0) {
      this.append(op(AGBVm.GP_STACK, AGBVm.OP_STACK_POP, count));
    }
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @param index
   * @return this
   * @throws IOException
   */
  public AGBOpcode opStackLoad(int index) throws IOException {
    this.append(op(AGBVm.GP_STACK, AGBVm.OP_STACK_LOAD, index));
    return this;
  }

  /**
   * Adiciona uma Instrução de Store. O indice 0 indica que irá armazenar o
   * elemento do topo da pilha no elemento do topo da pilha. Assim, esse indice
   * é redundante.
   * 
   * @param index
   * @return this
   * @throws IOException
   */
  public AGBOpcode opStackStore(int index) throws IOException {
    this.append(op(AGBVm.GP_STACK, AGBVm.OP_STACK_STORE, index));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @param index
   * @return this
   * @throws IOException
   */
  public AGBOpcode opStackInt(int index) throws IOException {
    this.append(op(AGBVm.GP_STACK, AGBVm.OP_STACK_INT, index));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @param value
   * @return this
   * @throws IOException
   */
  public AGBOpcode opDefNum(double value) throws IOException {
    String text = Double.valueOf(value).toString();
    this.append(op(AGBVm.GP_DEF, AGBVm.OP_DEF_NUM, text.length())
      + op(text.indexOf('.') - 1) + str(text));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @param value
   * @return this
   * @throws IOException
   */
  public AGBOpcode opDefStr(String value) throws IOException {
    this
      .append(op(AGBVm.GP_DEF, AGBVm.OP_DEF_STR, value.length()) + str(value));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @param count
   * @return this
   * @throws IOException
   */
  public AGBOpcode opDefNumInit(int count) throws IOException {
    this.append(op(AGBVm.GP_DEF, AGBVm.OP_DEF_NUM_LEN, count));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @param count
   * @return this
   * @throws IOException
   */
  public AGBOpcode opDefStrInit(int count) throws IOException {
    this.append(op(AGBVm.GP_DEF, AGBVm.OP_DEF_STR_LEN, count));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opLoadTrue() throws IOException {
    this.append(op(AGBVm.GP_LOAD, AGBVm.OP_LOAD_TRUE));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opLoadNull() throws IOException {
    this.append(op(AGBVm.GP_LOAD, AGBVm.OP_LOAD_NULL));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opLoadFalse() throws IOException {
    this.append(op(AGBVm.GP_LOAD, AGBVm.OP_LOAD_FALSE));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @param index
   * @return this
   * @throws IOException
   */
  public AGBOpcode opLoadNum(int index) throws IOException {
    this.append(op(AGBVm.GP_LOAD, AGBVm.OP_LOAD_NUM, index));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @param index
   * @return this
   * @throws IOException
   */
  public AGBOpcode opLoadStr(int index) throws IOException {
    this.append(op(AGBVm.GP_LOAD, AGBVm.OP_LOAD_STR, index));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opJumpStack() throws IOException {
    this.append(op(AGBVm.GP_JUMP, AGBVm.OP_JUMP_STACK));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @param index
   * @return this
   * @throws IOException
   */
  public AGBOpcode opJumpInt(int index) throws IOException {
    this.append(op(AGBVm.GP_JUMP, AGBVm.OP_JUMP_INT, index));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @param index
   * @return this
   * @throws IOException
   */
  public AGBOpcode opJumpCall(int index) throws IOException {
    this.append(op(AGBVm.GP_JUMP, AGBVm.OP_JUMP_CALL, index));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @param index
   * @return this
   * @throws IOException
   */
  public AGBOpcode opJumpTrue(int index) throws IOException {
    this.append(op(AGBVm.GP_JUMP, AGBVm.OP_JUMP_TRUE, index));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @param index
   * @return this
   * @throws IOException
   */
  public AGBOpcode opJumpFalse(int index) throws IOException {
    this.append(op(AGBVm.GP_JUMP, AGBVm.OP_JUMP_FALSE, index));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @param count número de objetos que sairão da pilha
   * @return this
   * @throws IOException
   */
  public AGBOpcode opJumpReturn(int count) throws IOException {
    this.append(op(AGBVm.GP_JUMP, AGBVm.OP_JUMP_RETURN, count));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opBoolNot() throws IOException {
    this.append(op(AGBVm.GP_BOOL, AGBVm.OP_BOOL_NOT));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opBoolOr() throws IOException {
    this.append(op(AGBVm.GP_BOOL, AGBVm.OP_BOOL_OR));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opBoolAnd() throws IOException {
    this.append(op(AGBVm.GP_BOOL, AGBVm.OP_BOOL_AND));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opBoolToStr() throws IOException {
    this.append(op(AGBVm.GP_BOOL, AGBVm.OP_BOOL_TO_STR));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opNumNeg() throws IOException {
    this.append(op(AGBVm.GP_NUM, AGBVm.OP_NUM_NEG));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opNumSum() throws IOException {
    this.append(op(AGBVm.GP_NUM, AGBVm.OP_NUM_SUM));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opNumSub() throws IOException {
    this.append(op(AGBVm.GP_NUM, AGBVm.OP_NUM_SUB));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opNumMul() throws IOException {
    this.append(op(AGBVm.GP_NUM, AGBVm.OP_NUM_MUL));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opNumDiv() throws IOException {
    this.append(op(AGBVm.GP_NUM, AGBVm.OP_NUM_DIV));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opNumEqual() throws IOException {
    this.append(op(AGBVm.GP_NUM, AGBVm.OP_NUM_EQUAL));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opNumNotEqual() throws IOException {
    this.append(op(AGBVm.GP_NUM, AGBVm.OP_NUM_NEQUAL));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opNumGreaterThan() throws IOException {
    this.append(op(AGBVm.GP_NUM, AGBVm.OP_NUM_GT));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opNumEqualGreaterThan() throws IOException {
    this.append(op(AGBVm.GP_NUM, AGBVm.OP_NUM_EGT));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opNumLowerThan() throws IOException {
    this.append(op(AGBVm.GP_NUM, AGBVm.OP_NUM_LT));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opNumEqualLowerThan() throws IOException {
    this.append(op(AGBVm.GP_NUM, AGBVm.OP_NUM_ELT));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opNumToStr() throws IOException {
    this.append(op(AGBVm.GP_NUM, AGBVm.OP_NUM_TO_STR));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opStrLen() throws IOException {
    this.append(op(AGBVm.GP_STR, AGBVm.OP_STR_LEN));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opStrSum() throws IOException {
    this.append(op(AGBVm.GP_STR, AGBVm.OP_STR_SUM));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opStrEqual() throws IOException {
    this.append(op(AGBVm.GP_STR, AGBVm.OP_STR_EQUAL));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opBoolEqual() throws IOException {
    this.append(op(AGBVm.GP_BOOL, AGBVm.OP_BOOL_EQUAL));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opBoolNotEqual() throws IOException {
    this.append(op(AGBVm.GP_BOOL, AGBVm.OP_BOOL_NEQUAL));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opStrNotEqual() throws IOException {
    this.append(op(AGBVm.GP_STR, AGBVm.OP_STR_NEQUAL));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opStrGreaterThan() throws IOException {
    this.append(op(AGBVm.GP_STR, AGBVm.OP_STR_GT));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opStrEqualGreaterThan() throws IOException {
    this.append(op(AGBVm.GP_STR, AGBVm.OP_STR_EGT));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opStrLowerThan() throws IOException {
    this.append(op(AGBVm.GP_STR, AGBVm.OP_STR_LT));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opStrEqualLowerThan() throws IOException {
    this.append(op(AGBVm.GP_STR, AGBVm.OP_STR_ELT));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opArrayNew() throws IOException {
    this.append(op(AGBVm.GP_ARRAY, AGBVm.OP_ARRAY_NEW));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opArrayLen() throws IOException {
    this.append(op(AGBVm.GP_ARRAY, AGBVm.OP_ARRAY_LEN));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opArrayGet() throws IOException {
    this.append(op(AGBVm.GP_ARRAY, AGBVm.OP_ARRAY_GET));
    return this;
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return this
   * @throws IOException
   */
  public AGBOpcode opArraySet() throws IOException {
    this.append(op(AGBVm.GP_ARRAY, AGBVm.OP_ARRAY_SET));
    return this;
  }

  /**
   * Adiciona uma instrução do usuario
   * 
   * @param leftCode
   * @param rightCode
   * @return this
   * @throws IOException
   */
  public AGBOpcode opUser(int leftCode, int rightCode) throws IOException {
    this.append(op(AGBVm.GP_USER + leftCode, rightCode));
    return this;
  }

  /**
   * Apenda um conteudo
   * 
   * @param text
   * @throws IOException
   */
  public void append(String text) throws IOException {
    this.size += text.length() / 8;
    this.output.write(text.getBytes());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    if (this.output instanceof ByteArrayOutputStream) {
      ByteArrayOutputStream output = (ByteArrayOutputStream) this.output;
      return output.toString();
    }
    else {
      return super.toString();
    }
  }

  /**
   * Converte em string entendido como Instrução
   * 
   * @param text
   * @return text modificado
   */
  protected static String str(String text) {
    StringBuilder sb = new StringBuilder();
    char[] chars = text.toCharArray();
    for (int n = 0;; n++) {
      char c1 = 0, c2 = 0, c3 = 0, c4 = 0;
      if (n * 4 < chars.length) {
        c1 = chars[n * 4];
      }
      if (n * 4 + 1 < chars.length) {
        c2 = chars[n * 4 + 1];
      }
      if (n * 4 + 2 < chars.length) {
        c3 = chars[n * 4 + 2];
      }
      if (n * 4 + 3 < chars.length) {
        c4 = chars[n * 4 + 3];
      }
      sb.append(op(c1));
      if (c1 == 0) {
        break;
      }
      sb.append(op(c2));
      if (c2 == 0) {
        break;
      }
      sb.append(op(c3));
      if (c3 == 0) {
        break;
      }
      sb.append(op(c4));
      if (c4 == 0) {
        break;
      }
    }
    return sb.toString();
  }

  /**
   * Cria o opcode
   * 
   * @param opcode
   * @return opcode
   */
  protected static String op(int opcode) {
    return "" + ((char) ('a' + ((opcode & 0xF0000000) >> 28)))
      + ((char) ('a' + ((opcode & 0xF000000) >> 24)))
      + ((char) ('a' + ((opcode & 0xF00000) >> 20)))
      + ((char) ('a' + ((opcode & 0xF0000) >> 16)))
      + ((char) ('a' + ((opcode & 0xF000) >> 12)))
      + ((char) ('a' + ((opcode & 0xF00) >> 8)))
      + ((char) ('a' + ((opcode & 0xF0) >> 4)))
      + ((char) ('a' + (opcode & 0xF)));
  }

  /**
   * Cria a Instrução
   * 
   * @param group
   * @param opcode
   * @return Instrução
   */
  protected static String op(int group, int opcode) {
    return op(group) + op(opcode);
  }

  /**
   * Cria a Instrução
   * 
   * @param group
   * @param opcode
   * @param data
   * @return Instrução
   */
  protected static String op(int group, int opcode, int data) {
    return op(group, opcode) + op(data);
  }

  /**
   * Retorna o número de bytecodes
   * 
   * @return bytecodes
   */
  public int getPc() {
    return this.size;
  }

}
