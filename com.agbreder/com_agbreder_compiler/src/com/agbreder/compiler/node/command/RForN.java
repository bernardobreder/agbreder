package com.agbreder.compiler.node.command;

import java.io.DataOutputStream;
import java.io.IOException;

import com.agbreder.compiler.node.RVariableNode;
import com.agbreder.compiler.node.expression.lvalue.LIdentify;
import com.agbreder.compiler.node.expression.rvalue.RIdentify;
import com.agbreder.compiler.node.expression.rvalue.RValue;
import com.agbreder.compiler.node.expression.rvalue.binary.RAssign;
import com.agbreder.compiler.node.expression.rvalue.binary.RDeclare;
import com.agbreder.compiler.node.expression.rvalue.binary.REqualLower;
import com.agbreder.compiler.node.expression.rvalue.binary.RSum;
import com.agbreder.compiler.node.expression.rvalue.primitive.RNumber;
import com.agbreder.compiler.type.RNumberType;
import com.agbreder.compiler.util.AGBDataInputStream;
import com.agbreder.compiler.util.RToken;

/**
 * For de N
 * 
 * 
 * @author Bernardo Breder
 */
public class RForN extends RBlock {

  /** Nome da variavel */
  private RToken name;
  /** Begin */
  private RValue begin;
  /** End */
  private RValue end;

  /**
   * Construtor
   * 
   * @param input
   * @throws IOException
   */
  public RForN(AGBDataInputStream input) throws IOException {
    super(input);
    {
      int count = input.readInt();
      for (int n = 0; n < count; n++) {
        this.getCommands().add((RCommand) input.create(input));
      }
    }
    {
      int count = input.readInt();
      for (int n = 0; n < count; n++) {
        this.getCommands().add((RCommand) input.create(input));
      }
    }
  }

  /**
   * Construtor
   * 
   * @param name
   * @param begin
   * @param end
   * @param command
   */
  public RForN(RToken name, RValue begin, RValue end, RCommand command) {
    super();
    this.name = name;
    this.begin = begin;
    this.end = end;
    RFor c = new RFor();
    c.addInit(new RExpression(new RDeclare(name, new LIdentify(name), begin,
      RNumberType.DEFAULT)));
    c.setCondition(new REqualLower(name, new RIdentify(name), end));
    c.addNext(new RExpression(new RAssign(name, new LIdentify(name), new RSum(
      name, new RIdentify(name), new RNumber(new RToken("1"))))));
    c.setCommand(command);
    this.add(c);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save(DataOutputStream output) throws IOException {
    output.writeInt(AGBDataInputStream.FOR_N_ID);
    super.save(output);

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
   * @return name
   */
  public RToken getName() {
    return name;
  }

  /**
   * Retorna
   * 
   * @return begin
   */
  public RValue getBegin() {
    return begin;
  }

  /**
   * Retorna
   * 
   * @return end
   */
  public RValue getEnd() {
    return end;
  }

  /**
   * @param name
   */
  public void setName(RToken name) {
    this.name = name;
  }

  /**
   * @param begin
   */
  public void setBegin(RValue begin) {
    this.begin = begin;
  }

  /**
   * @param end
   */
  public void setEnd(RValue end) {
    this.end = end;
  }

}
