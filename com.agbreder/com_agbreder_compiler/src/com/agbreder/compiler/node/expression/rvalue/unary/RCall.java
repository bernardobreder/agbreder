package com.agbreder.compiler.node.expression.rvalue.unary;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.agbreder.compiler.AGBCompilerOpcode;
import com.agbreder.compiler.exception.RIllegalArgumentException;
import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.node.command.RMethod;
import com.agbreder.compiler.node.expression.rvalue.RValue;
import com.agbreder.compiler.util.AGBDataInputStream;
import com.agbreder.compiler.util.RToken;

/**
 * Classe primitivas
 * 
 * 
 * @author Bernardo Breder
 */
public class RCall extends RValue {

  /** Parametros */
  private final List<RValue> params = new ArrayList<RValue>();
  /** Method */
  private RMethod method;

  /**
   * Construtor
   * 
   * @param input
   * @throws IOException
   */
  public RCall(AGBDataInputStream input) throws IOException {
    this(new RToken(input));
    this.setMethod((RMethod) input.create(input));
    {
      int count = input.readInt();
      for (int n = 0; n < count; n++) {
        this.getParams().add((RValue) input.create(input));
      }
    }
  }

  /**
   * @param token
   */
  public RCall(RToken token) {
    super(token);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void head(ContextNode c) throws Exception {
    for (RValue param : params) {
      param.head(c);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void compile(ContextNode c) throws Exception {
    for (RValue param : params) {
      param.compile(c);
    }
    String name = this.getToken().getText();
    for (RMethod method : c.getMethods()) {
      if (name.equals(method.getName().getText())) {
        this.method = method;
        break;
      }
    }
    if (this.method == null) {
      throw new RIllegalArgumentException(this.getToken());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void link(ContextNode c) throws Exception {
    for (RValue param : params) {
      param.link(c);
    }
    this.setType(method.getType());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void build(AGBCompilerOpcode opcodes) throws Exception {
    opcodes.opStackPush(1);
    for (RValue param : params) {
      param.build(opcodes);
    }
    opcodes.opJumpCall(method.getAddress());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save(DataOutputStream output) throws IOException {
  }

  /**
   * Adiciona um parametro
   * 
   * @param value
   */
  public void addParam(RValue value) {
    this.params.add(value);
  }

  /**
   * Retorna
   * 
   * @return method
   */
  public RMethod getMethod() {
    return method;
  }

  /**
   * @param method
   */
  public void setMethod(RMethod method) {
    this.method = method;
  }

  /**
   * Retorna
   * 
   * @return params
   */
  public List<RValue> getParams() {
    return params;
  }

}
