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
public class REqualLower extends RBinary {

  /** Valor preprocessado */
  private Boolean value;

  /**
   * Construtor
   * 
   * @param input
   * @throws IOException
   */
  public REqualLower(AGBDataInputStream input) throws IOException {
    this(new RToken(input), (RValue) input.create(input), (RValue) input
      .create(input));
  }

  /**
   * @param token
   * @param left
   * @param right
   */
  public REqualLower(RToken token, RValue left, RValue right) {
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
   * 
   * @throws Exception
   */
  @Override
  public void link(ContextNode c) throws Exception {
    this.getLeft().link(c);
    this.getRight().link(c);
    if (this.getLeft().getType().isNumber()) {
      if (!this.getRight().getType().isNumber()) {
        this.value = false;
      }
    }
    else if (this.getLeft().getType().isString()) {
      if (!this.getRight().getType().isString()) {
        this.value = false;
      }
    }
    else {
      throw new RIllegalOperationException(this.getLeft(), this.getToken(),
        this.getRight());
    }
    this.setType(RBooleanType.DEFAULT);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void build(AGBCompilerOpcode opcodes) throws Exception {
    if (this.value != null) {
      if (this.value) {
        opcodes.opLoadTrue();
      }
      else {
        opcodes.opLoadFalse();
      }
    }
    else {
      this.getLeft().build(opcodes);
      this.getRight().build(opcodes);
      if (this.getLeft().getType().isNumber()) {
        opcodes.opNumEqualLowerThan();
      }
      else if (this.getLeft().getType().isString()) {
        opcodes.opStrEqualLowerThan();
      }
    }
  }

}
