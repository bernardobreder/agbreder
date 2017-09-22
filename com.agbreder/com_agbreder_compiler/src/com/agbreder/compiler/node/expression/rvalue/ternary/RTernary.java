package com.agbreder.compiler.node.expression.rvalue.ternary;

import java.io.IOException;

import com.agbreder.compiler.AGBCompilerOpcode;
import com.agbreder.compiler.exception.RWrongTypeException;
import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.node.expression.rvalue.RValue;
import com.agbreder.compiler.node.expression.rvalue.binary.RBinary;
import com.agbreder.compiler.util.AGBDataInputStream;
import com.agbreder.compiler.util.RToken;

/**
 * Classe primitivas
 * 
 * 
 * @author Bernardo Breder
 */
public class RTernary extends RBinary {

  /** Left */
  private RValue center;

  /**
   * Construtor
   * 
   * @param input
   * @throws IOException
   */
  public RTernary(AGBDataInputStream input) throws IOException {
    this(new RToken(input), (RValue) input.create(input), (RValue) input
      .create(input), (RValue) input.create(input));
  }

  /**
   * @param token
   * @param left
   * @param center
   * @param right
   */
  public RTernary(RToken token, RValue left, RValue center, RValue right) {
    super(token, left, right);
    this.center = center;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void head(ContextNode c) throws Exception {
    super.head(c);
    this.getCenter().head(c);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void compile(ContextNode c) throws Exception {
    super.compile(c);
    this.getCenter().compile(c);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void link(ContextNode c) throws Exception {
    super.link(c);
    this.getCenter().link(c);
    if (!this.getRight().getType().canCast(this.getCenter().getType())) {
      throw new RWrongTypeException(this.getToken());
    }
    this.setType(this.getRight().getType());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void build(AGBCompilerOpcode opcodes) throws Exception {
    int centerOpcodes = AGBCompilerOpcode.getBytecodeCount(this.getCenter());
    int rightOpcodes = AGBCompilerOpcode.getBytecodeCount(this.getRight());
    this.getLeft().build(opcodes);
    opcodes.opJumpTrueNext(3);
    opcodes.opJumpIntNext(centerOpcodes + 3);
    this.getCenter().build(opcodes);
    opcodes.opJumpIntNext(rightOpcodes);
    this.getRight().build(opcodes);
  }

  /**
   * Retorna
   * 
   * @return left
   */
  public RValue getCenter() {
    return center;
  }

  /**
   * @param right
   */
  public void setCenter(RValue right) {
    this.center = right;
  }

}
