package com.agbreder.compiler.node.command;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.agbreder.compiler.AGBCompilerOpcode;
import com.agbreder.compiler.exception.RIllegalArgumentException;
import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.node.expression.rvalue.RValue;
import com.agbreder.compiler.type.RType;
import com.agbreder.compiler.util.AGBDataInputStream;

/**
 * Condição
 * 
 * 
 * @author Bernardo Breder
 */
public class RSwitch extends RCommand {

  /** Condição */
  private RValue condition;
  /** Commando */
  private final List<RCommand> commands = new ArrayList<RCommand>();
  /** Commando */
  private final List<RValue> values = new ArrayList<RValue>();
  /** Else Command */
  private RCommand command;

  /**
   * Construtor
   * 
   * @param input
   */
  public RSwitch(AGBDataInputStream input) {
    this();
  }

  /**
   * Construtor
   */
  public RSwitch() {
    super();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void head(ContextNode c) throws Exception {
    this.getCondition().head(c);
    for (RValue value : this.getValues()) {
      value.head(c);
    }
    for (RCommand command : this.getCommands()) {
      command.head(c);
    }
    if (this.getCommand() != null) {
      this.getCommand().head(c);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void compile(ContextNode c) throws Exception {
    this.getCondition().compile(c);
    for (RValue value : this.getValues()) {
      value.compile(c);
    }
    for (RCommand command : this.getCommands()) {
      command.compile(c);
    }
    if (this.getCommand() != null) {
      this.getCommand().compile(c);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void link(ContextNode c) throws Exception {
    this.getCondition().link(c);
    for (RValue value : this.getValues()) {
      value.link(c);
      RType type = value.getType();
      if (!(type.isString() || type.isNumber() || type.isBoolean())) {
        throw new RIllegalArgumentException(value.getToken());
      }
      if (!type.equals(this.getCondition().getType())) {
        throw new RIllegalArgumentException(value.getToken());
      }
    }
    for (RCommand command : this.getCommands()) {
      command.link(c);
    }
    if (this.getCommand() != null) {
      this.getCommand().link(c);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void build(AGBCompilerOpcode opcodes) throws Exception {
    List<RCommand> commands = this.getCommands();
    List<Integer> commandCounts = new ArrayList<Integer>(commands.size());
    for (RCommand command : commands) {
      commandCounts.add(AGBCompilerOpcode.getBytecodeCount(command) + 3);
    }
    List<RValue> values = this.getValues();
    List<Integer> valueCounts = new ArrayList<Integer>(values.size());
    for (RValue value : values) {
      valueCounts.add(AGBCompilerOpcode.getBytecodeCount(value));
    }
    int elseCommandCount = 0;
    if (this.getCommand() != null) {
      elseCommandCount =
        AGBCompilerOpcode.getBytecodeCount(this.getCommand()) + 3;
    }
    this.getCondition().build(opcodes);
    for (int n = 0; n < values.size(); n++) {
      RValue value = values.get(n);
      opcodes.opStackDup(0);
      value.build(opcodes);
      opcodes.opEqual(value);
      {
        int index = 3;
        if (this.getCommand() == null) {
          index += 3;
        }
        for (int m = n + 1; m < values.size(); m++) {
          index += 3 + 2 + 3 + valueCounts.get(m);
        }
        for (int m = 0; m < n; m++) {
          index += commandCounts.get(m) + 3;
        }
        opcodes.opJumpTrueNext(index);
      }
    }
    if (this.getCommand() != null) {
      int index = 0;
      for (int n = 0; n < commands.size(); n++) {
        index += commandCounts.get(n) + 3;
      }
      opcodes.opJumpIntNext(index);
    }
    else {
      {
        int index = 0;
        for (int n = 0; n < commands.size(); n++) {
          index += commandCounts.get(n) + 3;
        }
        opcodes.opStackPop(1);
        opcodes.opJumpIntNext(index);
      }
    }
    for (int n = 0; n < commands.size(); n++) {
      opcodes.setCount(1);
      opcodes.opStackPop(1);
      RCommand command = commands.get(n);
      command.build(opcodes);
      {
        int index = 0;
        for (int m = commands.size() - 1; m > n; m--) {
          index += commandCounts.get(m) + 3;
        }
        if (this.getCommand() != null) {
          index += elseCommandCount;
        }
        opcodes.opJumpIntNext(index);
      }
    }
    if (this.getCommand() != null) {
      opcodes.setCount(1);
      opcodes.opStackPop(1);
      this.getCommand().build(opcodes);
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
   * @return commands
   */
  public List<RCommand> getCommands() {
    return commands;
  }

  /**
   * Retorna
   * 
   * @return values
   */
  public List<RValue> getValues() {
    return values;
  }

  /**
   * @param e
   * @see java.util.List#add(java.lang.Object)
   */
  public void addCommand(RCommand e) {
    commands.add(e);
  }

  /**
   * @param e
   * @see java.util.List#add(java.lang.Object)
   */
  public void addValue(RValue e) {
    values.add(e);
  }

  /**
   * Retorna
   * 
   * @return command
   */
  public RCommand getCommand() {
    return command;
  }

  /**
   * @param command
   */
  public void setCommand(RCommand command) {
    this.command = command;
  }

}
