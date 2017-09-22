package com.agbreder.compiler.parser;

import java.util.ArrayList;
import java.util.List;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.exception.AGBTokenException;
import com.agbreder.compiler.parser.node.AGBAndBitNode;
import com.agbreder.compiler.parser.node.AGBAndNode;
import com.agbreder.compiler.parser.node.AGBAskNode;
import com.agbreder.compiler.parser.node.AGBAssignNode;
import com.agbreder.compiler.parser.node.AGBBlockNode;
import com.agbreder.compiler.parser.node.AGBBooleanNode;
import com.agbreder.compiler.parser.node.AGBBreakNode;
import com.agbreder.compiler.parser.node.AGBCallFunctionNode;
import com.agbreder.compiler.parser.node.AGBCallNode;
import com.agbreder.compiler.parser.node.AGBCaseNode;
import com.agbreder.compiler.parser.node.AGBCastNode;
import com.agbreder.compiler.parser.node.AGBCatchNode;
import com.agbreder.compiler.parser.node.AGBClassNode;
import com.agbreder.compiler.parser.node.AGBCommandNode;
import com.agbreder.compiler.parser.node.AGBDeclareNode;
import com.agbreder.compiler.parser.node.AGBDivNode;
import com.agbreder.compiler.parser.node.AGBEmptyNode;
import com.agbreder.compiler.parser.node.AGBEqualNode;
import com.agbreder.compiler.parser.node.AGBExpressionNode;
import com.agbreder.compiler.parser.node.AGBFieldFunctionNode;
import com.agbreder.compiler.parser.node.AGBFieldNode;
import com.agbreder.compiler.parser.node.AGBForNNode;
import com.agbreder.compiler.parser.node.AGBForNode;
import com.agbreder.compiler.parser.node.AGBFunctionNode;
import com.agbreder.compiler.parser.node.AGBGetFieldNode;
import com.agbreder.compiler.parser.node.AGBGreaterEqualNode;
import com.agbreder.compiler.parser.node.AGBGreaterNode;
import com.agbreder.compiler.parser.node.AGBIfNode;
import com.agbreder.compiler.parser.node.AGBLIdentifyNode;
import com.agbreder.compiler.parser.node.AGBLSetFieldNode;
import com.agbreder.compiler.parser.node.AGBLValue;
import com.agbreder.compiler.parser.node.AGBLowEqualNode;
import com.agbreder.compiler.parser.node.AGBLowerNode;
import com.agbreder.compiler.parser.node.AGBMethodNode;
import com.agbreder.compiler.parser.node.AGBModNode;
import com.agbreder.compiler.parser.node.AGBMulNode;
import com.agbreder.compiler.parser.node.AGBNativeNode;
import com.agbreder.compiler.parser.node.AGBNegNode;
import com.agbreder.compiler.parser.node.AGBNewNode;
import com.agbreder.compiler.parser.node.AGBNode;
import com.agbreder.compiler.parser.node.AGBNotEqualNode;
import com.agbreder.compiler.parser.node.AGBNotNode;
import com.agbreder.compiler.parser.node.AGBNullNode;
import com.agbreder.compiler.parser.node.AGBNumberNode;
import com.agbreder.compiler.parser.node.AGBOrBitNode;
import com.agbreder.compiler.parser.node.AGBOrNode;
import com.agbreder.compiler.parser.node.AGBPosDecNode;
import com.agbreder.compiler.parser.node.AGBPosIncNode;
import com.agbreder.compiler.parser.node.AGBPreDecNode;
import com.agbreder.compiler.parser.node.AGBPreIncNode;
import com.agbreder.compiler.parser.node.AGBRIdentifyNode;
import com.agbreder.compiler.parser.node.AGBRValue;
import com.agbreder.compiler.parser.node.AGBRValueNode;
import com.agbreder.compiler.parser.node.AGBRepeatNode;
import com.agbreder.compiler.parser.node.AGBResourceNode;
import com.agbreder.compiler.parser.node.AGBReturnNode;
import com.agbreder.compiler.parser.node.AGBShiftLeftNode;
import com.agbreder.compiler.parser.node.AGBShiftRightNode;
import com.agbreder.compiler.parser.node.AGBStringNode;
import com.agbreder.compiler.parser.node.AGBSubNode;
import com.agbreder.compiler.parser.node.AGBSumNode;
import com.agbreder.compiler.parser.node.AGBSuperConstrutorNode;
import com.agbreder.compiler.parser.node.AGBSuperNode;
import com.agbreder.compiler.parser.node.AGBSwitchNode;
import com.agbreder.compiler.parser.node.AGBThrowClassNode;
import com.agbreder.compiler.parser.node.AGBThrowNode;
import com.agbreder.compiler.parser.node.AGBTryNode;
import com.agbreder.compiler.parser.node.AGBVariableNode;
import com.agbreder.compiler.parser.node.AGBWhileNode;
import com.agbreder.compiler.parser.node.type.AGBBooleanArrayTypeNode;
import com.agbreder.compiler.parser.node.type.AGBBooleanTypeNode;
import com.agbreder.compiler.parser.node.type.AGBByteArrayTypeNode;
import com.agbreder.compiler.parser.node.type.AGBByteTypeNode;
import com.agbreder.compiler.parser.node.type.AGBClassTypeNode;
import com.agbreder.compiler.parser.node.type.AGBFunctionTypeNode;
import com.agbreder.compiler.parser.node.type.AGBNumberArrayTypeNode;
import com.agbreder.compiler.parser.node.type.AGBNumberTypeNode;
import com.agbreder.compiler.parser.node.type.AGBObjectTypeNode;
import com.agbreder.compiler.parser.node.type.AGBStringTypeNode;
import com.agbreder.compiler.parser.node.type.AGBThisNode;
import com.agbreder.compiler.parser.node.type.AGBThisTypeNode;
import com.agbreder.compiler.parser.node.type.AGBTypeNode;
import com.agbreder.compiler.token.AGBToken;
import com.agbreder.compiler.token.AGBTokenType;
import com.agbreder.compiler.util.LightArrayList;

/**
 * Realiza o parser de um conjunto de tokens
 * 
 * @author bernardobreder
 */
public class AGBParser {

	/** Tokens */
	public List<AGBToken> tokens;

	/** Indice */
	public int index;

	/** Caminho */
	private final String filename;

	/**
	 * Construtor
	 * 
	 * @param filename
	 * @param tokens
	 */
	public AGBParser(String filename, List<AGBToken> tokens) {
		this.filename = filename;
		this.tokens = tokens;
		this.index = 0;
	}

	/**
	 * Realiza o parser de uma lista de tokens
	 * 
	 * @return lista de nodes
	 * @throws AGBException
	 */
	public List<AGBCommandNode> execute() throws AGBException {
		List<AGBCommandNode> list = new ArrayList<AGBCommandNode>();
		while (!isEof()) {
			list.add(doFileCmd());
		}
		return list;
	}

	/**
	 * @return node
	 * @throws AGBException
	 */
	public AGBCommandNode doFileCmd() throws AGBException {
		if (isThrowClass()) {
			return doThrowClass(null);
		} else {
			return doClass(null);
		}
	}

	/**
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBCommandNode doCmd(AGBCommandNode parent) throws AGBException {
		if (isBlock()) {
			return doBlock(parent);
		} else if (isIf()) {
			return doIf(parent);
		} else if (isWhile()) {
			return doWhile(parent);
		} else if (isForN()) {
			return doForN(parent);
		} else if (isFor()) {
			return doFor(parent);
		} else if (isRepeat()) {
			return doRepeat(parent);
		} else if (isSwitch()) {
			return doSwitch(parent);
		} else if (isReturn()) {
			return doReturn(parent);
		} else if (isSuperConstrutor()) {
			return doSuperConstrutor(parent);
		} else if (isBreak()) {
			return doBreak(parent);
		} else if (isTry()) {
			return doTry(parent);
		} else if (isThrow()) {
			return doThrow(parent);
		} else if (isSemiComma()) {
			return doSemiComma(parent);
		} else {
			return new AGBExpressionNode(parent, doExp(parent));
		}
	}

	/**
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBNode doClassCmd(AGBCommandNode parent) throws AGBException {
		if (isMethod()) {
			return doMethod(parent);
		} else {
			return doField(parent);
		}
	}

	/**
	 * Verifica se é um bloco
	 * 
	 * @return boolean
	 */
	public boolean isClass() {
		return is(AGBTokenType.CLASS);
	}

	/**
	 * Realiza a leitura de um bloco
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBCommandNode doClass(AGBCommandNode parent) throws AGBException {
		AGBClassNode node = new AGBClassNode(parent);
		check(AGBTokenType.CLASS);
		node.setName(doId());
		if (can(AGBTokenType.EXTENDS)) {
			node.setExtendToken(doId());
		}
		check(AGBTokenType.DO);
		while (!is(AGBTokenType.END)) {
			node.getCommands().add(doClassCmd(node));
		}
		check(AGBTokenType.END);
		return node;
	}

	/**
	 * Verifica se é um bloco
	 * 
	 * @return boolean
	 */
	public boolean isThrowClass() {
		return is(AGBTokenType.THROW);
	}

	/**
	 * Realiza a leitura de um bloco
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBCommandNode doThrowClass(AGBCommandNode parent) throws AGBException {
		AGBThrowClassNode node = new AGBThrowClassNode(parent);
		check(AGBTokenType.THROW);
		if (can(AGBTokenType.RUNTIME)) {
			node.setRuntime(true);
		}
		node.setName(doId());
		if (can(AGBTokenType.EXTENDS)) {
			node.setExtendToken(doId());
		}
		check(AGBTokenType.DO);
		while (!is(AGBTokenType.END)) {
			node.getCommands().add(doClassCmd(node));
		}
		check(AGBTokenType.END);
		return node;
	}

	/**
	 * Verifica se é um bloco
	 * 
	 * @return boolean
	 */
	public boolean isDeclare() {
		int index = isType();
		if (index == 0) {
			return false;
		}
		return isId(index) && is(AGBTokenType.EQUAL, index + 1);
	}

	/**
	 * Verifica se é um bloco
	 * 
	 * @return boolean
	 */
	public boolean isCast() {
		if (!is(AGBTokenType.LPARAM)) {
			return false;
		}
		int index = isType(1);
		if (index == 0) {
			return false;
		}
		return is(AGBTokenType.RPARAM, index + 1);
	}

	/**
	 * Verifica se é um bloco
	 * 
	 * @return boolean
	 */
	public boolean isMethod() {
		int index = 0;
		if (is(AGBTokenType.STATIC, index)) {
			index++;
		}
		int aux = isTypeThis(index);
		if (aux == 0) {
			return false;
		}
		index += aux;
		return isId(index) && is(AGBTokenType.LPARAM, index + 1);
	}

	/**
	 * Verifica se é um bloco
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBNode doField(AGBNode parent) throws AGBException {
		AGBTypeNode type = doType();
		AGBToken name = doId();
		return new AGBFieldNode(parent, type, name);
	}

	/**
	 * Verifica se é um bloco
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBMethodNode doHeadMethod(AGBCommandNode parent) throws AGBException {
		AGBMethodNode node = new AGBMethodNode(parent);
		for (;;) {
			if (can(AGBTokenType.STATIC)) {
				node.setStaticAtt(true);
			} else {
				break;
			}
		}
		node.setReturnType(doTypeThis());
		node.setName(doId());
		check(AGBTokenType.LPARAM);
		while (!is(AGBTokenType.RPARAM)) {
			AGBTypeNode ptype = doType();
			AGBToken ptoken = doId();
			node.getParams().add(new AGBVariableNode(node, ptoken, ptype));
			if (!can(AGBTokenType.COMMA)) {
				break;
			}
		}
		check(AGBTokenType.RPARAM);
		if (can(AGBTokenType.THROW)) {
			do {
				node.getThrowsTypes().add(doType());
			} while (can(AGBTokenType.COMMA));
		}
		return node;
	}

	/**
	 * Verifica se é um if
	 * 
	 * @return boolean
	 */
	public boolean isBlock() {
		return is(AGBTokenType.DO);
	}

	/**
	 * Realiza a leitura de um bloco
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBBlockNode doBlock(AGBNode parent) throws AGBException {
		AGBBlockNode node = new AGBBlockNode(parent);
		check(AGBTokenType.DO);
		while (!is(AGBTokenType.END)) {
			node.getCommands().add(doCmd(node));
		}
		check(AGBTokenType.END);
		return node;
	}

	/**
	 * Verifica se é um if
	 * 
	 * @return boolean
	 */
	public boolean isIf() {
		return is(AGBTokenType.IF);
	}

	/**
	 * Realiza a leitura de um if
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBCommandNode doIf(AGBCommandNode parent) throws AGBException {
		AGBIfNode node = new AGBIfNode(parent);
		check(AGBTokenType.IF);
		node.setCondition(doExp(node));
		node.setCommand(doCmd(node));
		if (can(AGBTokenType.ELSE)) {
			node.setElseCommand(doCmd(node));
		}
		return node;
	}

	/**
	 * Verifica se é um while
	 * 
	 * @return boolean
	 */
	public boolean isWhile() {
		return is(AGBTokenType.WHILE);
	}

	/**
	 * Realiza a leitura de um while
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBCommandNode doWhile(AGBCommandNode parent) throws AGBException {
		AGBWhileNode node = new AGBWhileNode(parent);
		check(AGBTokenType.WHILE);
		node.setCondition(doExp(node));
		node.setCommand(doCmd(node));
		return node;
	}

	/**
	 * Verifica se é um if
	 * 
	 * @return boolean
	 */
	public boolean isRepeat() {
		return is(AGBTokenType.REPEAT);
	}

	/**
	 * Realiza a leitura de um if
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBCommandNode doRepeat(AGBCommandNode parent) throws AGBException {
		AGBRepeatNode node = new AGBRepeatNode(parent);
		check(AGBTokenType.REPEAT);
		node.setCommand(doCmd(node));
		check(AGBTokenType.WHILE);
		node.setCondition(doExp(node));
		return node;
	}

	/**
	 * Verifica se é um switch
	 * 
	 * @return boolean
	 */
	public boolean isSwitch() {
		return is(AGBTokenType.SWITCH);
	}

	/**
	 * Realiza a leitura de um switch
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBCommandNode doSwitch(AGBCommandNode parent) throws AGBException {
		AGBSwitchNode node = new AGBSwitchNode(parent);
		check(AGBTokenType.SWITCH);
		node.setCondition(doExp(node));
		check(AGBTokenType.DO);
		List<AGBCaseNode> cases = new ArrayList<AGBCaseNode>();
		do {
			AGBCaseNode caseNode = new AGBCaseNode(node);
			check(AGBTokenType.CASE);
			caseNode.setValue(doExp(node));
			caseNode.setCommand(doCmd(caseNode));
			cases.add(caseNode);
		} while (is(AGBTokenType.CASE));
		node.setCases(cases);
		if (can(AGBTokenType.ELSE)) {
			node.setElcmd(doBlock(node));
		}
		check(AGBTokenType.END);
		return node;
	}

	/**
	 * Verifica se é um for
	 * 
	 * @return boolean
	 */
	public boolean isFor() {
		return is(AGBTokenType.FOR);
	}

	/**
	 * Realiza a leitura de um for
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBCommandNode doFor(AGBCommandNode parent) throws AGBException {
		AGBForNode node = new AGBForNode(parent);
		check(AGBTokenType.FOR);
		check(AGBTokenType.LPARAM);
		List<AGBNode> inits = new ArrayList<AGBNode>();
		while (!is(AGBTokenType.SEMICOMMA)) {
			inits.add(doCmd(node.getBlock()));
		}
		node.setInits(inits);
		AGBToken token = check(AGBTokenType.SEMICOMMA);
		if (is(AGBTokenType.SEMICOMMA)) {
			node.setCondition(new AGBBooleanNode(node.getBlock(), token, true));
		} else {
			node.setCondition(doExp(node.getBlock()));
		}
		check(AGBTokenType.SEMICOMMA);
		List<AGBNode> nexts = new ArrayList<AGBNode>();
		while (!is(AGBTokenType.RPARAM)) {
			nexts.add(doCmd(node.getBlock()));
		}
		node.setNexts(nexts);
		check(AGBTokenType.RPARAM);
		node.getBlock().getCommands().add(doCmd(node.getBlock()));
		return node;
	}

	/**
	 * Verifica se é um for
	 * 
	 * @return boolean
	 */
	public boolean isForN() {
		return is(AGBTokenType.FOR) && isId(1);
	}

	/**
	 * Realiza a leitura de um for
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBCommandNode doForN(AGBCommandNode parent) throws AGBException {
		AGBForNNode node = new AGBForNNode(parent);
		check(AGBTokenType.FOR);
		node.setName(doId());
		check(AGBTokenType.EQUAL);
		node.setLeft(doExp(node.getBlock()));
		check(AGBTokenType.COMMA);
		node.setRight(doExp(node.getBlock()));
		node.getBlock().getCommands().add(doCmd(node.getBlock()));
		return node;
	}

	/**
	 * Verifica se é um return
	 * 
	 * @return boolean
	 */
	public boolean isReturn() {
		return is(AGBTokenType.RETURN);
	}

	/**
	 * Realiza a leitura de um return
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBCommandNode doReturn(AGBCommandNode parent) throws AGBException {
		AGBReturnNode node = new AGBReturnNode(parent);
		check(AGBTokenType.RETURN);
		node.setLeft(doExp(node));
		return node;
	}

	/**
	 * Verifica se é um return
	 * 
	 * @return boolean
	 */
	public boolean isSuperConstrutor() {
		return is(AGBTokenType.SUPER) && is(AGBTokenType.LPARAM, 1);
	}

	/**
	 * Realiza a leitura de um return
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBCommandNode doSuperConstrutor(AGBCommandNode parent) throws AGBException {
		AGBSuperConstrutorNode node = new AGBSuperConstrutorNode(parent);
		node.setToken(check(AGBTokenType.SUPER));
		check(AGBTokenType.LPARAM);
		if (!is(AGBTokenType.RPARAM)) {
			{
				AGBRValueNode left = doExp(node);
				node.getParams().add(left);
			}
			while (can(AGBTokenType.COMMA)) {
				AGBRValueNode left = doExp(node);
				node.getParams().add(left);
			}
		}
		check(AGBTokenType.RPARAM);
		return node;
	}

	/**
	 * Verifica se é um return
	 * 
	 * @return boolean
	 */
	public boolean isBreak() {
		return is(AGBTokenType.BREAK);
	}

	/**
	 * Realiza a leitura de um return
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBCommandNode doBreak(AGBCommandNode parent) throws AGBException {
		AGBToken token = check(AGBTokenType.BREAK);
		return new AGBBreakNode(parent, token);
	}

	/**
	 * Verifica se é um return
	 * 
	 * @return boolean
	 */
	public boolean isTry() {
		return is(AGBTokenType.TRY);
	}

	/**
	 * Realiza a leitura de um return
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBTryNode doTry(AGBCommandNode parent) throws AGBException {
		AGBToken token = check(AGBTokenType.TRY);
		AGBTryNode node = new AGBTryNode(parent, token);
		node.setCommand(doCmd(node));
		while (isCatch()) {
			node.getCatchs().add(doCatch(node));
		}
		return node;
	}

	/**
	 * Verifica se é um return
	 * 
	 * @return boolean
	 */
	public boolean isCatch() {
		return is(AGBTokenType.CATCH);
	}

	/**
	 * Realiza a leitura de um return
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBCatchNode doCatch(AGBCommandNode parent) throws AGBException {
		check(AGBTokenType.CATCH);
		AGBCatchNode node = new AGBCatchNode(parent);
		node.setType(doType());
		node.setName(doId());
		node.getBlock().getCommands().add(doCmd(node.getBlock()));
		return node;
	}

	/**
	 * Verifica se é um return
	 * 
	 * @return boolean
	 */
	public boolean isThrow() {
		return is(AGBTokenType.THROW);
	}

	/**
	 * Realiza a leitura de um return
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBCommandNode doThrow(AGBCommandNode parent) throws AGBException {
		AGBToken token = check(AGBTokenType.THROW);
		AGBThrowNode node = new AGBThrowNode(parent, token);
		node.setValue(this.doExp(node));
		return node;
	}

	/**
	 * Verifica se é um return
	 * 
	 * @return boolean
	 */
	public boolean isSemiComma() {
		return is(AGBTokenType.SEMICOMMA);
	}

	/**
	 * Realiza a leitura de um return
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBCommandNode doSemiComma(AGBCommandNode parent) throws AGBException {
		check(AGBTokenType.SEMICOMMA);
		return new AGBEmptyNode(parent);
	}

	/**
	 * Realiza a leitura de um return
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBCommandNode doMethod(AGBCommandNode parent) throws AGBException {
		AGBMethodNode method = doHeadMethod(parent);
		method.addCommand(doBlock(method));
		method.getParent(AGBClassNode.class).addMethod(method);
		return method;
	}

	/**
	 * Retorna o Expression
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBRValueNode doExp(AGBNode parent) throws AGBException {
		return doTernary(parent);
	}

	/**
	 * Retorna o Ternary
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBRValueNode doTernary(AGBNode parent) throws AGBException {
		AGBRValueNode left = doOrAnd(parent);
		if (is(AGBTokenType.ASK)) {
			AGBAskNode node = new AGBAskNode(parent);
			node.setLeft(left);
			node.setToken(check(AGBTokenType.ASK));
			node.setCenter(doTernary(parent));
			check(AGBTokenType.DOTDOT);
			node.setRight(doTernary(parent));
			left = node;
		}
		return left;
	}

	/**
	 * Retorna o Or
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBRValueNode doOrAnd(AGBNode parent) throws AGBException {
		AGBRValueNode left = doEqual(parent);
		if (is(AGBTokenType.AND) || is(AGBTokenType.OR)) {
			if (is(AGBTokenType.AND)) {
				AGBAndNode node = new AGBAndNode(parent);
				node.setLeft(left);
				node.setToken(check(AGBTokenType.AND));
				node.setRight(doOrAnd(node));
				left = node;
			} else if (is(AGBTokenType.OR)) {
				AGBOrNode node = new AGBOrNode(parent);
				node.setLeft(left);
				node.setToken(check(AGBTokenType.OR));
				node.setRight(doOrAnd(node));
				left = node;
			}
		}
		return left;
	}

	/**
	 * Retorna o Equal
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBRValueNode doEqual(AGBNode parent) throws AGBException {
		AGBRValueNode left = doShift(parent);
		while ((is(AGBTokenType.EQUAL) && is(AGBTokenType.EQUAL, 1)) || (is(AGBTokenType.NOT) && is(AGBTokenType.EQUAL, 1)) || is(AGBTokenType.GREATER) || is(AGBTokenType.LOWER)) {
			if (is(AGBTokenType.EQUAL) && is(AGBTokenType.EQUAL, 1)) {
				AGBEqualNode node = new AGBEqualNode(parent);
				node.setLeft(left);
				node.setToken(check(AGBTokenType.EQUAL));
				check(AGBTokenType.EQUAL);
				node.setRight(doShift(node));
				left = node;
			} else if (is(AGBTokenType.NOT) && is(AGBTokenType.EQUAL, 1)) {
				AGBNotEqualNode node = new AGBNotEqualNode(parent);
				node.setLeft(left);
				node.setToken(check(AGBTokenType.NOT));
				check(AGBTokenType.EQUAL);
				node.setRight(doShift(node));
				left = node;
			} else if (is(AGBTokenType.GREATER) && is(AGBTokenType.EQUAL, 1)) {
				AGBGreaterEqualNode node = new AGBGreaterEqualNode(parent);
				node.setLeft(left);
				node.setToken(check(AGBTokenType.GREATER));
				check(AGBTokenType.EQUAL);
				node.setRight(doShift(node));
				left = node;
			} else if (is(AGBTokenType.LOWER) && is(AGBTokenType.EQUAL, 1)) {
				AGBLowEqualNode node = new AGBLowEqualNode(parent);
				node.setLeft(left);
				node.setToken(check(AGBTokenType.LOWER));
				check(AGBTokenType.EQUAL);
				node.setRight(doShift(node));
				left = node;
			} else if (is(AGBTokenType.GREATER)) {
				AGBGreaterNode node = new AGBGreaterNode(parent);
				node.setLeft(left);
				node.setToken(check(AGBTokenType.GREATER));
				node.setRight(doShift(node));
				left = node;
			} else if (is(AGBTokenType.LOWER)) {
				AGBLowerNode node = new AGBLowerNode(parent);
				node.setLeft(left);
				node.setToken(check(AGBTokenType.LOWER));
				node.setRight(doShift(node));
				left = node;
			}
		}
		return left;
	}

	/**
	 * Retorna o Sum
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBRValueNode doShift(AGBNode parent) throws AGBException {
		AGBRValueNode left = doSumSub(parent);
		while ((is(AGBTokenType.LOWER) && is(AGBTokenType.LOWER, 1)) || (is(AGBTokenType.GREATER) && is(AGBTokenType.GREATER, 1)) || is(AGBTokenType.AND_BIT) || is(AGBTokenType.OR_BIT)) {
			if (is(AGBTokenType.LOWER) && is(AGBTokenType.LOWER, 1)) {
				AGBShiftLeftNode node = new AGBShiftLeftNode(parent);
				check(AGBTokenType.LOWER);
				check(AGBTokenType.LOWER);
				node.setLeft(left);
				node.setRight(doSumSub(node));
				node.setToken(node.getRight().getToken());
				left = node;
			} else if (is(AGBTokenType.GREATER) && is(AGBTokenType.GREATER, 1)) {
				AGBShiftRightNode node = new AGBShiftRightNode(parent);
				check(AGBTokenType.GREATER);
				check(AGBTokenType.GREATER);
				node.setLeft(left);
				node.setRight(doSumSub(node));
				node.setToken(node.getRight().getToken());
				left = node;
			} else if (is(AGBTokenType.AND_BIT)) {
				AGBAndBitNode node = new AGBAndBitNode(parent);
				check(AGBTokenType.AND_BIT);
				node.setLeft(left);
				node.setRight(doSumSub(node));
				node.setToken(node.getRight().getToken());
				left = node;
			} else if (is(AGBTokenType.OR_BIT)) {
				AGBOrBitNode node = new AGBOrBitNode(parent);
				check(AGBTokenType.OR_BIT);
				node.setLeft(left);
				node.setRight(doSumSub(node));
				node.setToken(node.getRight().getToken());
				left = node;
			}
		}
		return left;
	}

	/**
	 * Retorna o Sum
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBRValueNode doSumSub(AGBNode parent) throws AGBException {
		AGBRValueNode left = doMulDiv(parent);
		while ((is(AGBTokenType.SUM) && !is(AGBTokenType.SUM, 1)) || (is(AGBTokenType.SUB) && !is(AGBTokenType.SUB, 1))) {
			if (is(AGBTokenType.SUM) && !is(AGBTokenType.SUM, 1)) {
				AGBSumNode node = new AGBSumNode(parent);
				node.setToken(check(AGBTokenType.SUM));
				node.setLeft(left);
				node.setRight(doMulDiv(node));
				left = node;
			} else if (is(AGBTokenType.SUB) && !is(AGBTokenType.SUB, 1)) {
				AGBSubNode node = new AGBSubNode(parent);
				node.setToken(check(AGBTokenType.SUB));
				node.setLeft(left);
				node.setRight(doMulDiv(node));
				left = node;
			}
		}
		return left;
	}

	/**
	 * Retorna o Mul
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBRValueNode doMulDiv(AGBNode parent) throws AGBException {
		AGBRValueNode left = doUnary(parent);
		while (is(AGBTokenType.MUL) || is(AGBTokenType.DIV) || is(AGBTokenType.MOD)) {
			if (is(AGBTokenType.MUL)) {
				AGBMulNode node = new AGBMulNode(parent);
				node.setToken(check(AGBTokenType.MUL));
				node.setLeft(left);
				node.setRight(doUnary(node));
				left = node;
			} else if (is(AGBTokenType.DIV)) {
				AGBDivNode node = new AGBDivNode(parent);
				node.setToken(check(AGBTokenType.DIV));
				node.setLeft(left);
				node.setRight(doUnary(node));
				left = node;
			} else if (is(AGBTokenType.MOD)) {
				AGBModNode node = new AGBModNode(parent);
				node.setToken(check(AGBTokenType.MOD));
				node.setLeft(left);
				node.setRight(doUnary(node));
				left = node;
			}
		}
		return left;
	}

	/**
	 * Retorna o Unary
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBRValueNode doUnary(AGBNode parent) throws AGBException {
		if (is(AGBTokenType.SUB) && is(AGBTokenType.SUB, 1)) {
			AGBPreDecNode node = new AGBPreDecNode(parent);
			check(AGBTokenType.SUB);
			check(AGBTokenType.SUB);
			node.setToken(doId());
			node.setLeft(new AGBRIdentifyNode(node, node.getToken()));
			return node;
		} else if (is(AGBTokenType.SUM) && is(AGBTokenType.SUM, 1)) {
			AGBPreIncNode node = new AGBPreIncNode(parent);
			check(AGBTokenType.SUM);
			check(AGBTokenType.SUM);
			node.setToken(doId());
			node.setLeft(new AGBRIdentifyNode(node, node.getToken()));
			return node;
		} else if (is(AGBTokenType.SUB)) {
			AGBNegNode node = new AGBNegNode(parent);
			node.setToken(check(AGBTokenType.SUB));
			node.setLeft(doRValue(node));
			return node;
		} else if (isLValue() && is(AGBTokenType.SUB, 1) && is(AGBTokenType.SUB, 2)) {
			AGBPosDecNode node = new AGBPosDecNode(parent);
			node.setToken(doId());
			check(AGBTokenType.SUB);
			check(AGBTokenType.SUB);
			node.setLeft(new AGBRIdentifyNode(node, node.getToken()));
			return node;
		} else if (isLValue() && is(AGBTokenType.SUM, 1) && is(AGBTokenType.SUM, 2)) {
			AGBPosIncNode node = new AGBPosIncNode(parent);
			node.setToken(doId());
			check(AGBTokenType.SUM);
			check(AGBTokenType.SUM);
			node.setLeft(new AGBRIdentifyNode(node, node.getToken()));
			return node;
		} else if (isCast()) {
			AGBCastNode node = new AGBCastNode(parent);
			check(AGBTokenType.LPARAM);
			node.setType(doType());
			node.setToken(node.getType().getToken());
			check(AGBTokenType.RPARAM);
			node.setLeft(doRValue(node));
			return node;
		} else if (is(AGBTokenType.NOT)) {
			AGBNotNode node = new AGBNotNode(parent);
			node.setToken(check(AGBTokenType.NOT));
			node.setLeft(doRValue(node));
			return node;
		} else {
			return doRValue(parent);
		}
	}

	/**
	 * Retorna o literal
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBRValueNode doRValue(AGBNode parent) throws AGBException {
		if (isLiteral()) {
			return doLiteral(parent);
		} else if (isDeclare()) {
			return doDeclareValue(parent);
		} else {
			AGBRValueNode left = doRValuePrefix(parent);
			while (isRValuePosfix()) {
				left = doRValuePosfix(parent, left);
			}
			if (is(AGBTokenType.EQUAL) && !is(AGBTokenType.EQUAL, 1)) {
				AGBAssignNode node = new AGBAssignNode(parent);
				check(AGBTokenType.EQUAL);
				AGBLValue leftValue = null;
				if (left instanceof AGBRIdentifyNode) {
					leftValue = new AGBLIdentifyNode(node, left.getToken());
				} else if (left instanceof AGBGetFieldNode) {
					leftValue = new AGBLSetFieldNode(node, ((AGBGetFieldNode) left), left.getToken());
				} else {
					throw new AGBTokenException("lvalue is wrong", left.getToken());
				}
				node.setLeft(leftValue);
				node.setToken(node.getLeft().getToken());
				node.setRight(doExp(node));
				left = node;
			}
			return left;
		}
	}

	/**
	 * Processa o RValue Prefix
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBRValueNode doRValuePrefix(AGBNode parent) throws AGBException {
		if (isParamValue()) {
			return doParamValue(parent);
		} else if (isThis()) {
			return doThis(parent);
		} else if (isSuper()) {
			return doSuper(parent);
		} else if (isNativeValue()) {
			return doNativeValue(parent);
		} else if (isNew()) {
			return doNew(parent);
		} else if (isFunction()) {
			return doFunction(parent);
		} else {
			return doIdentifyValue(parent);
		}
	}

	/**
	 * Processa node
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBRValueNode doParamValue(AGBNode parent) throws AGBException {
		check(AGBTokenType.LPARAM);
		AGBRValueNode left = doExp(parent);
		check(AGBTokenType.RPARAM);
		return left;
	}

	/**
	 * Processa node
	 * 
	 * @return node
	 * @throws AGBException
	 */
	public boolean isParamValue() throws AGBException {
		return is(AGBTokenType.LPARAM);
	}

	/**
	 * Processa node
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBRIdentifyNode doIdentifyValue(AGBNode parent) throws AGBException {
		return new AGBRIdentifyNode(parent, doId());
	}

	/**
	 * Processa node
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBRValue doAssignValue(AGBNode parent) throws AGBException {
		AGBAssignNode node = new AGBAssignNode(parent);
		node.setLeft(doLValue(node));
		node.setToken(check(AGBTokenType.EQUAL));
		node.setRight(doExp(node));
		return node;
	}

	/**
	 * Processa node
	 * 
	 * @return node
	 * @throws AGBException
	 */
	public boolean isAssignValue() throws AGBException {
		return isLValue() && is(AGBTokenType.EQUAL, 1) && !is(AGBTokenType.EQUAL, 2);
	}

	/**
	 * Processa node
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBDeclareNode doDeclareValue(AGBNode parent) throws AGBException {
		AGBDeclareNode node = new AGBDeclareNode(parent);
		node.setType(doType());
		node.setToken(doId());
		check(AGBTokenType.EQUAL);
		node.setRight(doExp(node));
		node.setLeft(new AGBLIdentifyNode(node, node.getToken()));
		return node;
	}

	/**
	 * Processa node
	 * 
	 * @return node
	 * @throws AGBException
	 */
	public boolean isNativeValue() throws AGBException {
		return is(AGBTokenType.NATIVE);
	}

	/**
	 * Processa node
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBRValueNode doNativeValue(AGBNode parent) throws AGBException {
		AGBNativeNode node = new AGBNativeNode(parent);
		check(AGBTokenType.NATIVE);
		node.setToken(doId());
		check(AGBTokenType.DOT);
		node.setOpcodeToken(doId());
		while (can(AGBTokenType.COMMA)) {
			node.getParams().add(doExp(node));
		}
		check(AGBTokenType.NATIVE);
		return node;
	}

	/**
	 * Processa node
	 * 
	 * @return node
	 * @throws AGBException
	 */
	public boolean isNullLiteral() throws AGBException {
		return is(AGBTokenType.NULL);
	}

	/**
	 * Processa node
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBRValueNode doNullLiteral(AGBNode parent) throws AGBException {
		return new AGBNullNode(parent, check(AGBTokenType.NULL));
	}

	/**
	 * Processa node
	 * 
	 * @return node
	 * @throws AGBException
	 */
	public boolean isThis() throws AGBException {
		return is(AGBTokenType.THIS);
	}

	/**
	 * Processa node
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBRValueNode doThis(AGBNode parent) throws AGBException {
		AGBThisNode node = new AGBThisNode(parent);
		node.setToken(check(AGBTokenType.THIS));
		return node;
	}

	/**
	 * Processa node
	 * 
	 * @return node
	 * @throws AGBException
	 */
	public boolean isSuper() throws AGBException {
		return is(AGBTokenType.SUPER);
	}

	/**
	 * Processa node
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBRValueNode doSuper(AGBNode parent) throws AGBException {
		AGBSuperNode node = new AGBSuperNode(parent);
		node.setToken(check(AGBTokenType.SUPER));
		return node;
	}

	/**
	 * Processa node
	 * 
	 * @return node
	 * @throws AGBException
	 */
	public boolean isNumberLiteral() throws AGBException {
		return is(AGBTokenType.NUMBER);
	}

	/**
	 * Processa node
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBNumberNode doNumberLiteral(AGBNode parent) throws AGBException {
		return new AGBNumberNode(parent, check(AGBTokenType.NUMBER));
	}

	/**
	 * Processa node
	 * 
	 * @return node
	 * @throws AGBException
	 */
	public boolean isStringLiteral() throws AGBException {
		return is(AGBTokenType.STRING);
	}

	/**
	 * Processa node
	 * 
	 * @return node
	 * @throws AGBException
	 */
	public boolean isResourceLiteral() throws AGBException {
		return is(AGBTokenType.RESOURCE);
	}

	/**
	 * Processa node
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBRValueNode doStringLiteral(AGBNode parent) throws AGBException {
		return new AGBStringNode(parent, check(AGBTokenType.STRING));
	}

	/**
	 * Processa node
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBRValueNode doResourceLiteral(AGBNode parent) throws AGBException {
		return new AGBResourceNode(parent, check(AGBTokenType.RESOURCE));
	}

	/**
	 * Processa node
	 * 
	 * @return node
	 * @throws AGBException
	 */
	public boolean isBooleanLiteral() throws AGBException {
		return is(AGBTokenType.TRUE) || is(AGBTokenType.FALSE);
	}

	/**
	 * Processa node
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBRValueNode doBooleanLiteral(AGBNode parent) throws AGBException {
		if (is(AGBTokenType.TRUE)) {
			AGBToken token = check(AGBTokenType.TRUE);
			return new AGBBooleanNode(parent, token, true);
		} else {
			AGBToken token = check(AGBTokenType.FALSE);
			return new AGBBooleanNode(parent, token, false);
		}
	}

	/**
	 * Retorna o literal
	 * 
	 * @return node
	 * @throws AGBException
	 */
	public boolean isLiteral() throws AGBException {
		return isNumberLiteral() || isStringLiteral() || isResourceLiteral() || is(AGBTokenType.TRUE) || is(AGBTokenType.FALSE) || isNullLiteral();
	}

	/**
	 * Retorna o literal
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBRValueNode doLiteral(AGBNode parent) throws AGBException {
		if (isNumberLiteral()) {
			return doNumberLiteral(parent);
		} else if (isStringLiteral()) {
			return doStringLiteral(parent);
		} else if (isResourceLiteral()) {
			return doResourceLiteral(parent);
		} else if (isBooleanLiteral()) {
			return doBooleanLiteral(parent);
		} else {
			return doNullLiteral(parent);
		}
	}

	/**
	 * Indica se tem um posfix para rvalue
	 * 
	 * @return flag
	 */
	public boolean isRValuePosfix() {
		return is(AGBTokenType.DOT) || is(AGBTokenType.LPARAM);
	}

	/**
	 * Recupera o identificador ou a chamada de método
	 * 
	 * @param parent
	 * @param left
	 * @return node
	 * @throws AGBException
	 */
	public AGBRValueNode doRValuePosfix(AGBNode parent, AGBRValueNode left) throws AGBException {
		if (is(AGBTokenType.DOT)) {
			check(AGBTokenType.DOT);
			if (isId() && is(AGBTokenType.LPARAM, 1)) {
				AGBCallNode node = new AGBCallNode(parent);
				node.setToken(doId());
				node.setLeft(left);
				check(AGBTokenType.LPARAM);
				if (!is(AGBTokenType.RPARAM)) {
					node.getParams().add(doExp(node));
					while (can(AGBTokenType.COMMA)) {
						node.getParams().add(doExp(node));
					}
				}
				check(AGBTokenType.RPARAM);
				return node;
			} else {
				AGBGetFieldNode node = new AGBGetFieldNode(parent);
				node.setLeft(left);
				node.setToken(doId());
				return node;
			}
		} else {
			AGBCallFunctionNode node = new AGBCallFunctionNode(parent);
			node.setLeft(left);
			check(AGBTokenType.LPARAM);
			while (!is(AGBTokenType.RPARAM)) {
				node.getValues().add(doExp(node));
				if (!can(AGBTokenType.COMMA)) {
					break;
				}
			}
			check(AGBTokenType.RPARAM);
			return node;
		}
	}

	/**
	 * Verifica o node de New
	 * 
	 * @return boolean
	 */
	public boolean isNew() {
		return is(AGBTokenType.NEW);
	}

	/**
	 * Recurera o node de New
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBRValueNode doNew(AGBNode parent) throws AGBException {
		AGBNewNode node = new AGBNewNode(parent);
		check(AGBTokenType.NEW);
		node.setType(doType());
		node.setToken(node.getType().getToken());
		check(AGBTokenType.LPARAM);
		while (!is(AGBTokenType.RPARAM)) {
			node.getParams().add(doExp(node));
			if (!can(AGBTokenType.COMMA)) {
				break;
			}
		}
		check(AGBTokenType.RPARAM);
		return node;
	}

	/**
	 * Verifica o node de New
	 * 
	 * @return boolean
	 */
	public boolean isFunction() {
		return is(AGBTokenType.FUNCTION);
	}

	/**
	 * Recurera o node de New
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBRValueNode doFunction(AGBNode parent) throws AGBException {
		AGBFunctionNode node = new AGBFunctionNode(parent);
		node.setToken(check(AGBTokenType.FUNCTION));
		node.setReturnType(doTypeThis());
		check(AGBTokenType.LPARAM);
		while (isType() > 0) {
			AGBTypeNode ptype = doType();
			AGBToken ptoken = doId();
			node.getParams().add(new AGBVariableNode(node, ptoken, ptype));
			if (!can(AGBTokenType.COMMA)) {
				break;
			}
		}
		check(AGBTokenType.RPARAM);
		if (can(AGBTokenType.THROW)) {
			do {
				node.getThrowsTypes().add(doType());
			} while (can(AGBTokenType.COMMA));
		}
		check(AGBTokenType.LPARAM);
		List<AGBDeclareNode> declares = new LightArrayList<AGBDeclareNode>();
		while (!is(AGBTokenType.RPARAM)) {
			AGBDeclareNode declare = doDeclareValue(parent);
			declares.add(declare);
			node.addDeclare(declare);
			if (!can(AGBTokenType.COMMA)) {
				break;
			}
		}
		AGBMethodNode method = new AGBMethodNode(node);
		{
			method.setReturnType(node.getReturnType());
			method.setName(node.getToken());
			method.getParams().addAll(node.getParams());
			method.getThrowsTypes().addAll(node.getThrowsTypes());
			for (int n = 0; n < declares.size(); n++) {
				AGBDeclareNode declare = declares.get(n);
				AGBFieldFunctionNode right = new AGBFieldFunctionNode(node, declare.getRight(), n);
				declare.setRight(right);
				right.setParent(node);
				right.getValue().setParent(node);
				method.addCommand(new AGBExpressionNode(method.getBlock(), declare));
			}
			method.getParent(AGBClassNode.class).addMethod(method);
			node.setMethod(method);
		}
		check(AGBTokenType.RPARAM);
		method.addCommand(doBlock(method.getBlock()));
		return node;
	}

	/**
	 * Indica se tem um LValue
	 * 
	 * @return boolean
	 */
	public boolean isLValue() {
		return isId();
	}

	/**
	 * Realiza a leitura do LValue
	 * 
	 * @param parent
	 * @return node
	 * @throws AGBException
	 */
	public AGBLValue doLValue(AGBNode parent) throws AGBException {
		AGBToken token = doId();
		return new AGBLIdentifyNode(parent, token);
	}

	/**
	 * Indica se tem um Type
	 * 
	 * @param index
	 * @return boolean
	 */
	public int isType(int index) {
		int init = index;
		if (is(AGBTokenType.NUM, index) || is(AGBTokenType.BOOL, index) || is(AGBTokenType.BYTE, index)) {
			if (is(AGBTokenType.LARRAY, index + 1) && is(AGBTokenType.RARRAY, index + 2)) {
				return 3;
			} else {
				return 1;
			}
		} else if (is(AGBTokenType.STR, index) || is(AGBTokenType.OBJ, index) || isId(index)) {
			return 1;
		} else if (is(AGBTokenType.FUNCTION, index++)) {
			int aux = 0;
			if ((aux = isTypeThis(index)) == 0) {
				return 0;
			}
			index += aux;
			if (!is(AGBTokenType.LPARAM, index++)) {
				return 0;
			}
			while (!is(AGBTokenType.RPARAM, index)) {
				if ((aux = isTypeThis(index)) == 0) {
					return 0;
				}
				index += aux;
				if (!is(AGBTokenType.COMMA, index)) {
					break;
				} else {
					index++;
				}
			}
			if (!is(AGBTokenType.RPARAM, index++)) {
				return 0;
			}
			return index - init;
		} else {
			return 0;
		}
	}

	/**
	 * Indica se tem um Type
	 * 
	 * @return boolean
	 */
	public int isType() {
		return isType(0);
	}

	/**
	 * Indica se tem um Type
	 * 
	 * @return boolean
	 */
	public int isTypeThis() {
		return isTypeThis(0);
	}

	/**
	 * Indica se tem um Type
	 * 
	 * @param index
	 * @return boolean
	 */
	public int isTypeThis(int index) {
		int aux = isType(index);
		if (aux > 0) {
			return aux;
		}
		return is(AGBTokenType.THIS, index) ? 1 : 0;
	}

	/**
	 * Reliza a leitura do Type
	 * 
	 * @return node
	 * @throws AGBException
	 */
	public AGBTypeNode doKnowType() throws AGBException {
		if (is(AGBTokenType.NUM)) {
			AGBToken token = check(AGBTokenType.NUM);
			if (is(AGBTokenType.LARRAY) && is(AGBTokenType.RARRAY, 1)) {
				check(AGBTokenType.LARRAY);
				check(AGBTokenType.RARRAY);
				return new AGBNumberArrayTypeNode(token);
			} else {
				return new AGBNumberTypeNode(token);
			}
		} else if (is(AGBTokenType.BOOL)) {
			AGBToken token = check(AGBTokenType.BOOL);
			if (is(AGBTokenType.LARRAY) && is(AGBTokenType.RARRAY, 1)) {
				check(AGBTokenType.LARRAY);
				check(AGBTokenType.RARRAY);
				return new AGBBooleanArrayTypeNode(token);
			} else {
				return new AGBBooleanTypeNode(token);
			}
		} else if (is(AGBTokenType.BYTE)) {
			AGBToken token = check(AGBTokenType.BYTE);
			if (is(AGBTokenType.LARRAY) && is(AGBTokenType.RARRAY, 1)) {
				check(AGBTokenType.LARRAY);
				check(AGBTokenType.RARRAY);
				return new AGBByteArrayTypeNode(token);
			} else {
				return new AGBByteTypeNode(token);
			}
		} else if (is(AGBTokenType.STR)) {
			AGBToken token = check(AGBTokenType.STR);
			return new AGBStringTypeNode(token);
		} else if (is(AGBTokenType.OBJ)) {
			AGBToken token = check(AGBTokenType.OBJ);
			return new AGBObjectTypeNode(token);
		} else if (is(AGBTokenType.FUNCTION)) {
			return doFunctionType();
		}
		return null;
	}

	/**
	 * @return node
	 * @throws AGBException
	 */
	private AGBTypeNode doFunctionType() throws AGBException {
		AGBToken token = check(AGBTokenType.FUNCTION);
		AGBTypeNode returnType = this.doTypeThis();
		check(AGBTokenType.LPARAM);
		List<AGBVariableNode> paramTypes = new LightArrayList<AGBVariableNode>();
		while (isType() > 0) {
			AGBTypeNode type = doType();
			AGBVariableNode var = new AGBVariableNode(null, null, type);
			paramTypes.add(var);
			if (!can(AGBTokenType.COMMA)) {
				break;
			}
		}
		check(AGBTokenType.RPARAM);
		List<AGBTypeNode> throwTypes = new LightArrayList<AGBTypeNode>();
		if (can(AGBTokenType.THROW)) {
			do {
				throwTypes.add(doType());
			} while (can(AGBTokenType.COMMA));
		}
		return new AGBFunctionTypeNode(token, returnType, paramTypes, throwTypes);
	}

	/**
	 * Reliza a leitura do Type
	 * 
	 * @return node
	 * @throws AGBException
	 */
	public AGBTypeNode doType() throws AGBException {
		AGBTypeNode type = this.doKnowType();
		if (type != null) {
			return type;
		}
		AGBToken token = doId();
		return new AGBClassTypeNode(token);
	}

	/**
	 * Realiza a leitura do TypeVoid
	 * 
	 * @return node
	 * @throws AGBException
	 */
	public AGBTypeNode doTypeThis() throws AGBException {
		AGBTypeNode type = this.doKnowType();
		if (type != null) {
			return type;
		}
		if (isThis()) {
			AGBToken token = check(AGBTokenType.THIS);
			return new AGBThisTypeNode(token);
		} else {
			AGBToken token = doId();
			return new AGBClassTypeNode(token);
		}
	}

	/**
	 * Verifica se chegou no final dos tokens
	 * 
	 * @return boolean
	 */
	public boolean isEof() {
		return isEof(0);
	}

	/**
	 * Verifica se chegou no final dos tokens
	 * 
	 * @param index
	 * @return boolean
	 */
	public boolean isEof(int index) {
		return this.index + index >= this.tokens.size();
	}

	/**
	 * Indica se o token corrente é do tipo especificado pelo parametro
	 * 
	 * @param type
	 * @return boolean
	 */
	public boolean is(AGBTokenType type) {
		return is(type, 0);
	}

	/**
	 * Indica se o token corrente é do tipo especificado pelo parametro
	 * 
	 * @param type
	 * @param index
	 * @return boolean
	 */
	public boolean is(AGBTokenType type, int index) {
		return !isEof(index) && get(index).getType() == type.getId();
	}

	/**
	 * Indica se o token corrente é do tipo especificado pelo parametro
	 * 
	 * @param index
	 * @return boolean
	 */
	public AGBToken get(int index) {
		return this.tokens.get(this.index + index);
	}

	/**
	 * Indica se o token corrente é do tipo especificado pelo parametro
	 * 
	 * @return boolean
	 */
	public boolean isId() {
		return is(AGBTokenType.ID);
	}

	/**
	 * Indica se o token corrente é do tipo especificado pelo parametro
	 * 
	 * @param index
	 * @return boolean
	 */
	public boolean isId(int index) {
		return is(AGBTokenType.ID, index);
	}

	/**
	 * Indica se o token corrente é do tipo especificado pelo parametro. Se for,
	 * será incrementado o contador de token
	 * 
	 * @param type
	 * @return boolean
	 */
	public boolean can(AGBTokenType type) {
		return can(type, 0);
	}

	/**
	 * Indica se o token corrente é do tipo especificado pelo parametro. Se for,
	 * será incrementado o contador de token
	 * 
	 * @param type
	 * @param index
	 * @return boolean
	 */
	public boolean can(AGBTokenType type, int index) {
		if (is(type, index)) {
			this.index++;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Exige que o token corrente seja do tipo id
	 * 
	 * @return token
	 * @throws AGBException
	 */
	public AGBToken doId() throws AGBException {
		return check(AGBTokenType.ID);
	}

	/**
	 * Exige que um token esteja corrente
	 * 
	 * @param type
	 * @return token
	 * @throws AGBException
	 */
	public AGBToken check(AGBTokenType type) throws AGBException {
		if (!can(type)) {
			String expected = type.getName();
			if (isEof()) {
				throw error(expected, new AGBToken(filename, "<EOF>", -1, -1, -1));
			}
			AGBToken token = get(0);
			AGBToken atual = token;
			throw error(expected, atual);
		}
		return this.tokens.get(this.index - 1);
	}

	/**
	 * Objeto de erro
	 * 
	 * @param expected
	 * @param atual
	 * @return exception
	 */
	public AGBException error(String expected, AGBToken atual) {
		return new AGBTokenException(String.format("Expected '%s' and not '%s'", expected, atual.getImage()), atual);
	}

}
