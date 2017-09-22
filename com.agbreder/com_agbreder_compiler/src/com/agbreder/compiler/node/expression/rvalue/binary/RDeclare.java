package com.agbreder.compiler.node.expression.rvalue.binary;

import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.node.RVariableNode;
import com.agbreder.compiler.node.expression.lvalue.LValue;
import com.agbreder.compiler.node.expression.rvalue.RValue;
import com.agbreder.compiler.type.RType;
import com.agbreder.compiler.util.AGBDataInputStream;
import com.agbreder.compiler.util.RToken;

/**
 * Declaração
 * 
 * 
 * @author Bernardo Breder
 */
public class RDeclare extends RAssign {

  /**
   * Construtor
   * 
   * @param input
   */
  public RDeclare(AGBDataInputStream input) {
    this(null, null, null, null);
  }

  /**
   * Construtor
   * 
   * @param token
   * @param left
   * @param right
   * @param type
   */
  public RDeclare(RToken token, LValue left, RValue right, RType type) {
    super(token, left, right);
    this.setType(type);
  }

  /**
   * {@inheritDoc}
   */
  @Override 
  public void head(ContextNode c) throws Exception {
    RVariableNode variableNode =
      new RVariableNode(this.getType(), this.getLeft().getToken());
    c.getLastBlock().add(variableNode);
    if (c.getLastMethod() != null) {
      c.getLastMethod().addVariable(variableNode);
    }
    super.head(c);
  }

}
