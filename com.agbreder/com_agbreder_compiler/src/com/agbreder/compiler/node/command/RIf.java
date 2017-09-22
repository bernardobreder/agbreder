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
public class RIf extends RCommand {

  /** Condição */
  private RValue condition;
  /** Commando */
  private RCommand command;
  /** Commando */
  private RCommand elseCommand;

  /**
   * Construtor
   * 
   * @param input
   */
  public RIf(AGBDataInputStream input) {
    this(null, null, null);
  }

  /**
   * @param condition
   * @param command
   * @param elseCommand
   */
  public RIf(RValue condition, RCommand command, RCommand elseCommand) {
    super();
    this.condition = condition;
    this.command = command;
    this.elseCommand = elseCommand;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void head(ContextNode c) throws Exception {
    this.getCondition().head(c);
    this.getCommand().head(c);
    if (this.getElseCommand() != null) {
      this.getElseCommand().head(c);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void compile(ContextNode c) throws Exception {
    this.getCondition().compile(c);
    this.getCommand().compile(c);
    if (this.getElseCommand() != null) {
      this.getElseCommand().compile(c);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void link(ContextNode c) throws Exception {
    this.getCondition().link(c);
    this.getCommand().link(c);
    if (this.getElseCommand() != null) {
      this.getElseCommand().link(c);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void build(AGBCompilerOpcode opcodes) throws Exception {
    int index = AGBCompilerOpcode.getBytecodeCount(this.getCommand());
    int elseIndex = 0;
    if (this.getElseCommand() != null) {
      elseIndex = AGBCompilerOpcode.getBytecodeCount(this.getElseCommand());
    }
    this.getCondition().build(opcodes);
    if (this.getElseCommand() != null) {
      opcodes.opJumpFalseNext(index + 3);
    }
    else {
      opcodes.opJumpFalseNext(index);
    }
    this.getCommand().build(opcodes);
    if (this.getElseCommand() != null) {
      opcodes.opJumpIntNext(elseIndex);
      this.getElseCommand().build(opcodes);
    }
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
   * @param condition
   */
  public void setCondition(RValue condition) {
    this.condition = condition;
  }

  /**
   * Retorna
   * 
   * @return block
   */
  public RCommand getCommand() {
    return command;
  }

  /**
   * @param block
   */
  public void setCommand(RCommand block) {
    this.command = block;
  }

  /**
   * Retorna
   * 
   * @return elseCommand
   */
  public RCommand getElseCommand() {
    return elseCommand;
  }

  /**
   * @param elseCommand
   */
  public void setElseCommand(RCommand elseCommand) {
    this.elseCommand = elseCommand;
  }

}
