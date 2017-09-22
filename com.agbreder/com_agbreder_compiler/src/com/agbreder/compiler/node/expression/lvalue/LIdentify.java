package com.agbreder.compiler.node.expression.lvalue;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import com.agbreder.compiler.AGBCompilerOpcode;
import com.agbreder.compiler.exception.RValueNotFoundException;
import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.node.RVariableNode;
import com.agbreder.compiler.node.command.RBlock;
import com.agbreder.compiler.util.AGBDataInputStream;
import com.agbreder.compiler.util.RToken;

/**
 * Identificador
 * 
 * 
 * @author Bernardo Breder
 */
public class LIdentify extends LValue {

  /** Indice na pilha */
  private Integer index;

  /**
   * Construtor
   * 
   * @param input
   * @throws IOException
   */
  public LIdentify(AGBDataInputStream input) throws IOException {
    this(new RToken(input));
  }

  /**
   * @param token
   */
  public LIdentify(RToken token) {
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
  public void compile(ContextNode c) throws Exception {
    String name = this.getToken().getText();
    for (int m = c.getBlocks().size() - 1; m >= 0; m--) {
      RBlock block = c.getBlocks().get(m);
      List<RVariableNode> variables = block.getVariables();
      int size = variables.size();
      for (int n = size - 1; n >= 0; n--) {
        RVariableNode node = variables.get(n);
        if (node.getName().getText().equals(name)) {
          this.index = size - n - 1;
          break;
        }
      }
      if (this.index != null) {
        break;
      }
    }
    if (this.index == null) {
      throw new RValueNotFoundException(name);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void link(ContextNode c) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void build(AGBCompilerOpcode opcodes) throws IOException {
    opcodes.opStackStore(index);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save(DataOutputStream output) throws IOException {
  }

}
