package com.agbreder.compiler.node.expression.rvalue.binary;

import java.io.IOException;

import com.agbreder.compiler.AGBCompilerOpcode;
import com.agbreder.compiler.exception.RIllegalOperationException;
import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.node.expression.rvalue.RValue;
import com.agbreder.compiler.type.RBooleanType;
import com.agbreder.compiler.util.AGBDataInputStream;
import com.agbreder.compiler.util.RToken;

/**
 * Classe primitivas
 * 
 * 
 * @author Bernardo Breder
 */
public class ROr extends RBinary {

  /**
   * Construtor
   * 
   * @param input
   * @throws IOException
   */
  public ROr(AGBDataInputStream input) throws IOException {
    this(new RToken(input), (RValue) input.create(input), (RValue) input
      .create(input));
  }

  /**
   * @param token
   * @param left
   * @param right
   */
  public ROr(RToken token, RValue left, RValue right) {
    super(token, left, right);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void compile(ContextNode c) throws Exception {
    super.compile(c);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void link(ContextNode c) throws Exception {
    this.getLeft().link(c);
    this.getRight().link(c);
    if (this.getLeft().getType().isBoolean()) {
      if (!this.getRight().getType().isBoolean()) {
        throw new RIllegalOperationException(this.getLeft(), this.getToken(),
          this.getRight());
      }
      this.setType(RBooleanType.DEFAULT);
    }
    else {
      throw new RIllegalOperationException(this.getLeft(), this.getToken(),
        this.getRight());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void build(AGBCompilerOpcode opcodes) throws Exception {
    this.getLeft().build(opcodes);
    this.getRight().build(opcodes);
    opcodes.opBoolOr();
  }

}
