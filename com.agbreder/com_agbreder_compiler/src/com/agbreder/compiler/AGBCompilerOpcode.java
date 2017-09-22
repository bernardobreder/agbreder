package com.agbreder.compiler;

import java.io.IOException;
import java.io.OutputStream;

import com.agbreder.compiler.exception.RException;
import com.agbreder.compiler.exception.RNotImplementedException;
import com.agbreder.compiler.node.RNode;
import com.agbreder.compiler.node.expression.rvalue.RValue;
import com.agbreder.compiler.type.RType;
import com.agbreder.vm.AGBOpcode;

/**
 * Criador de bytecode
 * 
 * 
 * @author Bernardo Breder
 */
public class AGBCompilerOpcode extends AGBOpcode {

  /** Contador */
  private int count = 0;

  /**
   * Construtor
   */
  public AGBCompilerOpcode() {
    super();
  }

  /**
   * @param output
   */
  public AGBCompilerOpcode(OutputStream output) {
    super(output);
  }

  /**
   * Incrementa o contador
   * 
   * @param count
   * @return owner
   */
  public AGBCompilerOpcode incCount(int count) {
    this.count += count;
    return this;
  }

  /**
   * Decrementa o contador
   * 
   * @param count
   * @return owner
   */
  public AGBCompilerOpcode decCount(int count) {
    this.count -= count;
    return this;
  }

  /**
   * Decrementa o contador
   * 
   * @return owner
   */
  public AGBCompilerOpcode resetCount() {
    this.count = 0;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opStackPush(int count) throws IOException {
    this.incCount(count);
    return super.opStackPush(count);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opStackPop(int count) throws IOException {
    this.decCount(count);
    return super.opStackPop(count);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opStackLoad(int index) throws IOException {
    super.opStackLoad(index + this.getCount());
    this.incCount(1);
    return this;
  }

  /**
   * Duplica o do topo da pilha
   * 
   * @param index
   * @return owner
   * @throws IOException
   */
  public AGBOpcode opStackDup(int index) throws IOException {
    this.incCount(1);
    return super.opStackLoad(index);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opStackStore(int index) throws IOException {
    return this.opStackStoreAbs(index + this.getCount());
  }

  /**
   * Adiciona uma instrução
   * 
   * @param index
   * @return this
   * @throws IOException
   */
  public AGBOpcode opStackStoreAbs(int index) throws IOException {
    super.opStackStore(index);
    this.decCount(1);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opStackInt(int index) throws IOException {
    this.incCount(1);
    return super.opStackInt(index);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opLoadTrue() throws IOException {
    this.incCount(1);
    return super.opLoadTrue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opLoadNull() throws IOException {
    this.incCount(1);
    return super.opLoadNull();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opLoadFalse() throws IOException {
    this.incCount(1);
    return super.opLoadFalse();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opLoadNum(int index) throws IOException {
    this.incCount(1);
    return super.opLoadNum(index);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opLoadStr(int index) throws IOException {
    this.incCount(1);
    return super.opLoadStr(index);
  }

  /**
   * Adiciona uma Instrução
   * 
   * @param index
   * @return owner
   * @throws IOException
   */
  public AGBOpcode opJumpFalseNext(int index) throws IOException {
    return this.opJumpFalse(index + this.getPc() + 3);
  }

  /**
   * Adiciona uma Instrução
   * 
   * @param index
   * @return owner
   * @throws IOException
   */
  public AGBOpcode opJumpTrueNext(int index) throws IOException {
    return this.opJumpTrue(index + this.getPc() + 3);
  }

  /**
   * Adiciona uma Instrução
   * 
   * @param index
   * @return owner
   * @throws IOException
   */
  @Override
  public AGBOpcode opJumpFalse(int index) throws IOException {
    this.decCount(1);
    return super.opJumpFalse(index);
  }

  /**
   * Adiciona uma Instrução
   * 
   * @param index
   * @return owner
   * @throws IOException
   */
  @Override
  public AGBOpcode opJumpTrue(int index) throws IOException {
    this.decCount(1);
    return super.opJumpTrue(index);
  }

  /**
   * Adiciona uma Instrução
   * 
   * @param index
   * @return owner
   * @throws IOException
   */
  public AGBOpcode opJumpIntNext(int index) throws IOException {
    super.opJumpInt(index + this.getPc() + 3);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opBoolOr() throws IOException {
    this.decCount(1);
    return super.opBoolOr();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opBoolAnd() throws IOException {
    this.decCount(1);
    return super.opBoolAnd();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opNumSum() throws IOException {
    this.decCount(1);
    return super.opNumSum();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opNumSub() throws IOException {
    this.decCount(1);
    return super.opNumSub();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opNumMul() throws IOException {
    this.decCount(1);
    return super.opNumMul();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opNumDiv() throws IOException {
    this.decCount(1);
    return super.opNumDiv();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opNumEqual() throws IOException {
    this.decCount(1);
    return super.opNumEqual();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opNumNotEqual() throws IOException {
    this.decCount(1);
    return super.opNumNotEqual();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opNumGreaterThan() throws IOException {
    this.decCount(1);
    return super.opNumGreaterThan();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opNumEqualGreaterThan() throws IOException {
    this.decCount(1);
    return super.opNumEqualGreaterThan();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opNumLowerThan() throws IOException {
    this.decCount(1);
    return super.opNumLowerThan();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opNumEqualLowerThan() throws IOException {
    this.decCount(1);
    return super.opNumEqualLowerThan();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opStrSum() throws IOException {
    this.decCount(1);
    return super.opStrSum();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opStrEqual() throws IOException {
    this.decCount(1);
    return super.opStrEqual();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opBoolEqual() throws IOException {
    this.decCount(1);
    return super.opBoolEqual();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opBoolNotEqual() throws IOException {
    this.decCount(1);
    return super.opBoolNotEqual();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opStrNotEqual() throws IOException {
    this.decCount(1);
    return super.opStrNotEqual();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opStrGreaterThan() throws IOException {
    this.decCount(1);
    return super.opStrGreaterThan();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opStrEqualGreaterThan() throws IOException {
    this.decCount(1);
    return super.opStrEqualGreaterThan();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opStrLowerThan() throws IOException {
    this.decCount(1);
    return super.opStrLowerThan();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opStrEqualLowerThan() throws IOException {
    this.decCount(1);
    return super.opStrEqualLowerThan();
  }

  /**
   * Adiciona uma Instrução
   * 
   * @return owner
   * @throws IOException
   */
  @Override
  public AGBOpcode opArrayGet() throws IOException {
    super.opArrayGet();
    this.decCount(1);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AGBOpcode opArraySet() throws IOException {
    super.opArraySet();
    this.decCount(3);
    return this;
  }

  /**
   * @param value
   * @throws RException
   * @throws IOException
   */
  public void opEqual(RValue value) throws RException, IOException {
    RType type = value.getType();
    if (type.isNumber()) {
      this.opNumEqual();
    }
    else if (type.isString()) {
      this.opStrEqual();
    }
    else if (type.isBoolean()) {
      this.opBoolEqual();
    }
    else {
      throw new RNotImplementedException();
    }
  }

  /**
   * Retorna o contador
   * 
   * @return contador
   */
  public int getCount() {
    return count;
  }

  /**
   * @param count
   */
  public void setCount(int count) {
    this.count = count;
  }

  /**
   * Retorna o número de bytecodes
   * 
   * @param node
   * @return numero
   * @throws Exception
   */
  public static int getBytecodeCount(RNode node) throws Exception {
    AGBCompilerOpcode opcodes = new AGBCompilerOpcode();
    node.build(opcodes);
    return opcodes.getPc();
  }

}
