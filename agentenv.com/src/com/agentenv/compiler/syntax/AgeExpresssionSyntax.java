package com.agentenv.compiler.syntax;

import java.util.List;

import com.agentenv.compiler.lexical.AgeToken;
import com.agentenv.compiler.lexical.AgeTokenType;
import com.agentenv.compiler.node.AgeNodeInterface;
import com.agentenv.compiler.node.expr.right.AgeMulNode;
import com.agentenv.compiler.node.expr.right.AgeRightValueInterface;
import com.agentenv.compiler.node.expr.right.AgeSumNode;
import com.agentenv.compiler.node.expr.right.primitive.AgeNumberNode;

public class AgeExpresssionSyntax extends AgeAbstractSyntax {

	/**
	 * Construtor
	 * 
	 * @param filename
	 * @param tokens
	 */
	public AgeExpresssionSyntax(String filename, List<AgeToken> tokens) {
		super(filename, tokens);
	}

	/**
	 * Leitura de uma expressão
	 * 
	 * @return node
	 * @throws AgeSyntaxException
	 */
	public AgeRightValueInterface execute() throws AgeSyntaxException {
		if (this.isEof()) {
			return null;
		}
		AgeRightValueInterface node = this.doExpression(null);
		if (!this.isEof()) {
			throw new AgeSyntaxException("expected <eof>");
		}
		return node;
	}

	/**
	 * Leitura de uma expressão
	 * 
	 * @return node
	 * @throws AgeSyntaxException
	 */
	protected AgeRightValueInterface doExpression(AgeNodeInterface parent) throws AgeSyntaxException {
		return this.doTernary(parent);
	}

	/**
	 * Leitura de uma expressão
	 * 
	 * @return node
	 * @throws AgeSyntaxException
	 */
	protected AgeRightValueInterface doTernary(AgeNodeInterface parent) throws AgeSyntaxException {
		return this.doOrAnd(parent);
	}

	/**
	 * Leitura de uma expressão
	 * 
	 * @return node
	 * @throws AgeSyntaxException
	 */
	protected AgeRightValueInterface doOrAnd(AgeNodeInterface parent) throws AgeSyntaxException {
		return this.doEqual(parent);
	}

	/**
	 * Leitura de uma expressão
	 * 
	 * @return node
	 * @throws AgeSyntaxException
	 */
	protected AgeRightValueInterface doEqual(AgeNodeInterface parent) throws AgeSyntaxException {
		return this.doSumSub(parent);
	}

	/**
	 * Leitura de uma expressão
	 * 
	 * @return node
	 * @throws AgeSyntaxException
	 */
	protected AgeRightValueInterface doSumSub(AgeNodeInterface parent) throws AgeSyntaxException {
		AgeRightValueInterface left = this.doMulDiv(parent);
		while ((is(AgeTokenType.SUM) && !is(AgeTokenType.SUM, 1)) || (is(AgeTokenType.SUB) && !is(AgeTokenType.SUB, 1))) {
			if (is(AgeTokenType.SUM)) {
				AgeSumNode node = new AgeSumNode(parent);
				node.setToken(check(AgeTokenType.SUM));
				node.setLeft(left);
				node.setRight(this.doMulDiv(node));
				left = node;
			}
			// else if (is(AgeTokenType.SUB)) {
			// AgeSubNode node = new AgeSubNode(parent);
			// node.setToken(check(AgeTokenType.SUB));
			// node.setLeft(left);
			// node.setRight(doMulDiv(node));
			// left = node;
			// }
		}
		return left;
	}

	/**
	 * Leitura de uma expressão
	 * 
	 * @return node
	 * @throws AgeSyntaxException
	 */
	protected AgeRightValueInterface doMulDiv(AgeNodeInterface parent) throws AgeSyntaxException {
		AgeRightValueInterface left = this.doRightValue(parent);
		while (is(AgeTokenType.MUL)||is(AgeTokenType.DIV)) {
			if (is(AgeTokenType.MUL)) {
				AgeMulNode node = new AgeMulNode(parent);
				node.setToken(check(AgeTokenType.MUL));
				node.setLeft(left);
				node.setRight(this.doRightValue(node));
				left = node;
			}
			// else if (is(AgeTokenType.SUB)) {
			// AgeSubNode node = new AgeSubNode(parent);
			// node.setToken(check(AgeTokenType.SUB));
			// node.setLeft(left);
			// node.setRight(doRightValue(node));
			// left = node;
			// }
		}
		return left;
	}

	/**
	 * Leitura de uma expressão
	 * 
	 * @return node
	 * @throws AgeSyntaxException
	 */
	protected AgeRightValueInterface doRightValue(AgeNodeInterface parent) throws AgeSyntaxException {
		return this.doLiteral(parent);
	}

	/**
	 * Leitura de uma expressão
	 * 
	 * @return node
	 * @throws AgeSyntaxException
	 */
	protected AgeRightValueInterface doLiteral(AgeNodeInterface parent) throws AgeSyntaxException {
		return this.doNumberLiteral(parent);
	}

	/**
	 * Processa node
	 * 
	 * @param parent
	 * @return node
	 * @throws AgeSyntaxException
	 */
	public AgeNumberNode doNumberLiteral(AgeNodeInterface parent) throws AgeSyntaxException {
		return new AgeNumberNode(parent, check(AgeTokenType.NUMBER));
	}

	/**
	 * Processa node
	 * 
	 * @return node
	 * @throws AgeSyntaxException
	 */
	protected boolean isNumberLiteral() throws AgeSyntaxException {
		return is(AgeTokenType.NUMBER);
	}

}
