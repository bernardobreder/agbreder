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
public class RRepeat extends RCommand {

  /** Condição */
  private RValue condition;
  /** Commando */
  private RCommand block;

  /**
   * Construtor
   * 
   * @param input
   */
  public RRepeat(AGBDataInputStream input) {
    this(null, null);
  }

  /**
   * @param condition
   * @param block
   */
  public RRepeat(RValue condition, RCommand block) {
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
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void build(AGBCompilerOpcode opcodes) throws Exception {
    int index = opcodes.getPc();
    this.getBlock().build(opcodes);
    this.getCondition().build(opcodes);
    opcodes.opJumpTrue(index);
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

  /**
   * @param condition
   */
  public void setCondition(RValue condition) {
    this.condition = condition;
  }

  /**
   * @param block
   */
  public void setBlock(RCommand block) {
    this.block = block;
  }

}
