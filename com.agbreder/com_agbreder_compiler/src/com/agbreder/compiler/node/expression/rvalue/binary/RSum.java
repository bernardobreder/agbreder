package com.agbreder.compiler.node.expression.rvalue.binary;

import java.io.IOException;

import com.agbreder.compiler.AGBCompilerOpcode;
import com.agbreder.compiler.exception.RIllegalOperationException;
import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.node.expression.rvalue.RValue;
import com.agbreder.compiler.type.RNumberType;
import com.agbreder.compiler.type.RStringType;
import com.agbreder.compiler.util.AGBDataInputStream;
import com.agbreder.compiler.util.RToken;

/**
 * Classe primitivas
 * 
 * 
 * @author Bernardo Breder
 */
public class RSum extends RBinary {

  /**
   * Construtor
   * 
   * @param input
   * @throws IOException
   */
  public RSum(AGBDataInputStream input) throws IOException {
    this(new RToken(input), (RValue) input.create(input), (RValue) input
      .create(input));
  }

  /**
   * @param token
   * @param left
   * @param right
   */
  public RSum(RToken token, RValue left, RValue right) {
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
    if (this.getLeft().getType().isNumber()) {
      if (!this.getRight().getType().isNumber()) {
        throw new RIllegalOperationException(this.getLeft(), this.getToken(),
          this.getRight());
      }
      this.setType(RNumberType.DEFAULT);
    }
    else if (this.getLeft().getType().isString()) {
      if (this.getRight().getType().isObject()) {
        throw new RIllegalOperationException(this.getLeft(), this.getToken(),
          this.getRight());
      }
      this.setType(RStringType.DEFAULT);
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
    if (this.getLeft().getType().isNumber()) {
      opcodes.opNumSum();
    }
    else if (this.getLeft().getType().isString()) {
      if (this.getRight().getType().isBoolean()) {
        opcodes.opBoolToStr();
      }
      else if (this.getRight().getType().isNumber()) {
        opcodes.opNumToStr();
      }
      opcodes.opStrSum();
    }
  }

}
