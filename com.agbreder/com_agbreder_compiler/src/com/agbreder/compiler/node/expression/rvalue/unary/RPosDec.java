package com.agbreder.compiler.node.expression.rvalue.unary;

import java.io.IOException;

import com.agbreder.compiler.AGBCompilerOpcode;
import com.agbreder.compiler.exception.RWrongTypeException;
import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.node.expression.lvalue.LIdentify;
import com.agbreder.compiler.node.expression.rvalue.RIdentify;
import com.agbreder.compiler.node.expression.rvalue.primitive.RNumber;
import com.agbreder.compiler.type.RNumberType;
import com.agbreder.compiler.util.AGBDataInputStream;
import com.agbreder.compiler.util.RToken;

/**
 * Cast de tipos
 * 
 * 
 * @author Bernardo Breder
 */
public class RPosDec extends RUnary {

  /** LValue */
  private LIdentify lvalue;
  /** LValue */
  private RNumber number;

  /**
   * Construtor
   * 
   * @param input
   * @throws IOException
   */
  public RPosDec(AGBDataInputStream input) throws IOException {
    this(new RToken(input), (RIdentify) input.create(input));
  }

  /**
   * @param token
   * @param left
   */
  public RPosDec(RToken token, RIdentify left) {
    super(token, left);
    this.lvalue = new LIdentify(left.getToken());
    this.number = new RNumber(new RToken("1"));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void head(ContextNode c) throws Exception {
    super.head(c);
    this.lvalue.head(c);
    this.number.head(c);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void compile(ContextNode c) throws Exception {
    super.compile(c);
    this.lvalue.compile(c);
    this.number.compile(c);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void link(ContextNode c) throws Exception {
    super.link(c);
    this.lvalue.link(c);
    this.number.link(c);
    if (!this.getLeft().getType().isNumber()) {
      throw new RWrongTypeException(this.getToken());
    }
    this.setType(RNumberType.DEFAULT);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void build(AGBCompilerOpcode opcodes) throws Exception {
    this.getLeft().build(opcodes);
    opcodes.opStackDup(0);
    this.number.build(opcodes);
    opcodes.opNumSub();
    this.lvalue.build(opcodes);
  }

}
