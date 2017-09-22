package com.agbreder.compiler.node.command;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.agbreder.compiler.AGBCompilerOpcode;
import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.node.RVariableNode;
import com.agbreder.compiler.util.AGBDataInputStream;

/**
 * Bloco de comandos
 * 
 * 
 * @author Bernardo Breder
 */
public class RBlock extends RCommand {

  /** Lista de comandos */
  private final List<RCommand> commands = new ArrayList<RCommand>();
  /** Lista de variáveis */
  private final List<RVariableNode> variables = new ArrayList<RVariableNode>();

  /**
   * Construtor
   * 
   * @param input
   * @throws IOException
   */
  public RBlock(AGBDataInputStream input) throws IOException {
    {
      int count = input.readInt();
      for (int n = 0; n < count; n++) {
        RCommand node = input.create(input);
        this.getCommands().add(node);
      }
    }
    {
      int count = input.readInt();
      for (int n = 0; n < count; n++) {
        RVariableNode node = input.create(input);
        this.getVariables().add(node);
      }
    }
  }

  /**
   * Construtor padrão
   */
  public RBlock() {
    super();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void head(ContextNode c) throws Exception {
    c.getBlocks().add(this);
    for (RVariableNode variable : this.getVariables()) {
      variable.head(c);
    }
    for (RCommand command : this.getCommands()) {
      command.head(c);
    }
    c.getBlocks().remove(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void compile(ContextNode c) throws Exception {
    c.getBlocks().add(this);
    for (RVariableNode variable : this.getVariables()) {
      variable.compile(c);
    }
    for (RCommand command : this.getCommands()) {
      command.compile(c);
    }
    c.getBlocks().remove(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void link(ContextNode c) throws Exception {
    c.getBlocks().add(this);
    for (RVariableNode variable : this.getVariables()) {
      variable.link(c);
    }
    for (RCommand command : this.getCommands()) {
      command.link(c);
    }
    c.getBlocks().remove(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void build(AGBCompilerOpcode opcodes) throws Exception {
    int size = this.getVariables().size();
    opcodes.decCount(size).opStackPush(size);
    for (RCommand command : this.getCommands()) {
      if (!command.isHeader()) {
        opcodes.setCount(0);
        command.build(opcodes);
      }
    }
    opcodes.incCount(size).opStackPop(size);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save(DataOutputStream output) throws IOException {
    output.writeInt(AGBDataInputStream.BLOCK_ID);
    {
      output.writeInt(this.getCommands().size());
      for (RCommand node : this.getCommands()) {
        node.save(output);
      }
    }
    {
      output.writeInt(this.getVariables().size());
      for (RVariableNode node : this.getVariables()) {
        node.save(output);
      }
    }
  }

  /**
   * Retorna
   * 
   * @return variables
   */
  public List<RVariableNode> getVariables() {
    return variables;
  }

  /**
   * @param e
   * @see java.util.List#add(java.lang.Object)
   */
  public void add(RCommand e) {
    commands.add(e);
  }

  /**
   * @param e
   * @see java.util.List#add(java.lang.Object)
   */
  public void add(RVariableNode e) {
    variables.add(e);
  }

  /**
   * Retorna
   * 
   * @return list
   */
  public List<RCommand> getCommands() {
    return commands;
  }

}
