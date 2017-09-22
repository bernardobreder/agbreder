package com.agbreder.compiler.node.command;

import java.io.DataOutputStream;
import java.io.IOException;

import com.agbreder.compiler.AGBCompilerOpcode;
import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.node.expression.rvalue.RValue;
import com.agbreder.compiler.util.AGBDataInputStream;

/**
 * Condição
 * 
 * 
 * @author Bernardo Breder
 */
public class RWhile extends RCommand {

  /** Condição */
  private final RValue condition;
  /** Commando */
  private final RCommand block;
  /** Endereço de memoria */
  private int index;

  /**
   * Construtor
   * 
   * @param input
   */
  public RWhile(AGBDataInputStream input) {
    this(null, null);
  }

  /**
   * @param condition
   * @param block
   */
  public RWhile(RValue condition, RCommand block) {
    super();
    this.condition = condition;
    this.block = block;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void head(ContextNode c) throws Exception {
    this.getCondition().head(c);
    this.getBlock().head(c);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void compile(ContextNode c) throws Exception {
    this.getCondition().compile(c);
    this.getBlock().compile(c);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void link(ContextNode c) throws Exception {
    this.getCondition().link(c);
    this.getBlock().link(c);
    this.index = AGBCompilerOpcode.getBytecodeCount(this.getBlock());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void build(AGBCompilerOpcode opcodes) throws Exception {
    int beginPc = opcodes.getPc();
    this.getCondition().build(opcodes);
    opcodes.opJumpFalseNext(this.index + 3);
    this.getBlock().build(opcodes);
    opcodes.opJumpInt(beginPc);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save(DataOutputStream output) throws IOException {
  }

  /**
   * Retorna
   * 
   * @return condition
   */
  public RValue getCondition() {
    return condition;
  }

  /**
   * Retorna
   * 
   * @return block
   */
  public RCommand getBlock() {
    return block;
  }

}
