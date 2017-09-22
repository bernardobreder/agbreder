package com.agbreder.compiler.node.command;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.agbreder.compiler.AGBCompilerOpcode;
import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.node.RNode;
import com.agbreder.compiler.node.expression.rvalue.RValue;
import com.agbreder.compiler.util.AGBDataInputStream;

/**
 * Condição
 * 
 * 
 * @author Bernardo Breder
 */
public class RFor extends RCommand {

  /** Inits */
  private final List<RNode> inits = new ArrayList<RNode>();
  /** Condição */
  private RValue condition;
  /** Nexts */
  private final List<RNode> nexts = new ArrayList<RNode>();
  /** Commando */
  private RCommand command;
  /** Endereço de memoria */
  private transient int index;

  /**
   * Construtor
   * 
   * @param input
   * @throws IOException
   */
  public RFor(AGBDataInputStream input) throws IOException {
    this();
    {
      int count = input.readInt();
      for (int n = 0; n < count; n++) {
        this.getInits().add((RNode) input.create(input));
      }
    }
    {
      this.setCondition((RValue) input.create(input));
    }
    {
      int count = input.readInt();
      for (int n = 0; n < count; n++) {
        this.getNexts().add((RNode) input.create(input));
      }
    }
  }

  /**
   * Construtor
   */
  public RFor() {
    super();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void head(ContextNode c) throws Exception {
    for (RNode init : this.getInits()) {
      init.head(c);
    }
    this.getCondition().head(c);
    for (RNode next : this.getNexts()) {
      next.head(c);
    }
    this.getCommand().head(c);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void compile(ContextNode c) throws Exception {
    for (RNode init : this.getInits()) {
      init.compile(c);
    }
    this.getCondition().compile(c);
    for (RNode next : this.getNexts()) {
      next.compile(c);
    }
    this.getCommand().compile(c);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void link(ContextNode c) throws Exception {
    for (RNode init : this.getInits()) {
      init.link(c);
    }
    this.getCondition().link(c);
    for (RNode next : this.getNexts()) {
      next.link(c);
    }
    this.getCommand().link(c);
    {
      this.index = AGBCompilerOpcode.getBytecodeCount(this.getCommand());
      for (RNode next : this.getNexts()) {
        this.index += AGBCompilerOpcode.getBytecodeCount(next);
      }
      this.index += 3;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void build(AGBCompilerOpcode opcodes) throws Exception {
    for (RNode init : this.getInits()) {
      init.build(opcodes);
    }
    int pc = opcodes.getPc();
    this.getCondition().build(opcodes);
    opcodes.opJumpFalseNext(index);
    this.getCommand().build(opcodes);
    for (RNode init : this.getNexts()) {
      init.build(opcodes);
    }
    opcodes.opJumpInt(pc);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save(DataOutputStream output) throws IOException {
    output.writeInt(AGBDataInputStream.FOR_ID);
    {
      output.writeInt(this.getInits().size());
      for (RNode node : this.getInits()) {
        node.save(output);
      }
    }
    this.getCondition().save(output);
    {
      output.writeInt(this.getNexts().size());
      for (RNode node : this.getNexts()) {
        node.save(output);
      }
    }
    output.writeInt(this.getIndex());
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
   * @return index
   */
  public int getIndex() {
    return index;
  }

  /**
   * @param index
   */
  public void setIndex(int index) {
    this.index = index;
  }

  /**
   * Retorna
   * 
   * @return inits
   */
  public List<RNode> getInits() {
    return inits;
  }

  /**
   * Retorna
   * 
   * @return nexts
   */
  public List<RNode> getNexts() {
    return nexts;
  }

  /**
   * Adiciona um registro
   * 
   * @param v
   */
  public void addInit(RNode v) {
    this.inits.add(v);
  }

  /**
   * Adiciona um registro
   * 
   * @param v
   */
  public void addNext(RNode v) {
    this.nexts.add(v);
  }

}
