package com.agbreder.compiler.node.expression.rvalue;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import com.agbreder.compiler.AGBCompilerOpcode;
import com.agbreder.compiler.exception.RException;
import com.agbreder.compiler.exception.RValueNotFoundException;
import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.node.RVariableNode;
import com.agbreder.compiler.node.command.RBlock;
import com.agbreder.compiler.node.command.RMethod;
import com.agbreder.compiler.util.AGBDataInputStream;
import com.agbreder.compiler.util.RToken;

/**
 * Identificador
 * 
 * 
 * @author Bernardo Breder
 */
public class RIdentify extends RValue {

  /** Indice na pilha */
  private Integer variableIndex;
  /** Indice na pilha */
  private RVariableNode variable;

  /**
   * Construtor
   * 
   * @param input
   * @throws IOException
   */
  public RIdentify(AGBDataInputStream input) throws IOException {
    this(new RToken(input));
  }

  /**
   * @param token
   */
  public RIdentify(RToken token) {
    super(token);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void head(ContextNode c) throws Exception {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void compile(ContextNode c) throws RException {
    boolean found = false;
    String name = this.getToken().getText();
    for (int m = c.getBlocks().size() - 1; m >= 0; m--) {
      RBlock block = c.getBlocks().get(m);
      List<RVariableNode> variables = block.getVariables();
      int size = variables.size();
      for (int n = size - 1; n >= 0; n--) {
        RVariableNode node = variables.get(n);
        if (node.getName().getText().equals(name)) {
          this.variableIndex = size - n - 1;
          this.variable = node;
          found = true;
          break;
        }
      }
      if (this.variable != null) {
        break;
      }
    }
    if (this.variable == null) {
      RMethod method = c.getLastMethod();
      if (method != null) {
        List<RVariableNode> params = method.getParams();
        int paramCount = params.size();
        for (int n = 0; n < paramCount; n++) {
          RVariableNode param = params.get(n);
          if (name.equals(param.getName().getText())) {
            this.variableIndex =
              paramCount + method.getVariables().size() - n - 1;
            this.variable = param;
            found = true;
            break;
          }
        }
      }
    }
    if (!found) {
      throw new RValueNotFoundException(name);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void link(ContextNode c) throws RException {
    this.setType(this.variable.getType());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void build(AGBCompilerOpcode opcodes) throws IOException {
    opcodes.opStackLoad(variableIndex);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save(DataOutputStream output) throws IOException {
  }

}
