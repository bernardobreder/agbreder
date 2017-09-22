package com.agbreder.compiler.parser.node;

import java.io.IOException;
import java.util.List;

import com.agbreder.compiler.exception.AGBCastException;
import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.parser.node.type.AGBTypeNode;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBSwitchNode extends AGBCommandNode {

	private AGBRValueNode condition;

	private List<AGBCaseNode> cases;

	private AGBBlockNode elcmd;

	private int elsePc;

	private int endPc;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param cond
	 * @param cases
	 * @param elcmd
	 */
	public AGBSwitchNode(AGBCommandNode parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		this.getCondition().header(context);
		for (int n = 0; n < this.getCases().size(); n++) {
			this.getCases().get(n).header(context);
		}
		if (this.getElcmd() != null) {
			this.getElcmd().header(context);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		this.getCondition().body(context);
		for (int n = 0; n < this.getCases().size(); n++) {
			this.getCases().get(n).body(context);
		}
		if (this.getElcmd() != null) {
			this.getElcmd().body(context);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		this.getCondition().link(context);
		for (int n = 0; n < this.cases.size(); n++) {
			this.cases.get(n).link(context);
		}
		if (this.elcmd != null) {
			this.elcmd.link(context);
		}
		checkCaseType(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException, AGBException {
		int counter = output.getCounter();
		this.getCondition().build(output);
		List<AGBCaseNode> cases = this.getCases();
		int caseSize = cases.size();
		for (int n = 0; n < caseSize; n++) {
			AGBCaseNode caseNode = cases.get(n);
			output.opStackDupAbs(0);
			caseNode.getValue().build(output);
			output.opEqual(this.getCondition());
			output.opJumpTrue(caseNode.getBeginPc());
		}
		output.opStackPop(1);
		if (this.getElcmd() != null) {
			output.opJumpInt(this.elsePc);
		} else {
			output.opJumpInt(this.endPc);
		}
		for (int n = 0; n < caseSize; n++) {
			AGBCaseNode caseNode = cases.get(n);
			caseNode.setBeginPc(output.getPc());
			output.opStackPop(1);
			output.setCounter(counter);
			caseNode.getCommand().build(output);
			output.opJumpInt(this.endPc);
		}
		if (this.getElcmd() != null) {
			this.elsePc = output.getPc();
			output.setCounter(counter);
			this.getElcmd().build(output);
		}
		this.endPc = output.getPc();
	}

	/**
	 * @param context
	 * @throws AGBException
	 * @throws AGBCastException
	 */
	private void checkCaseType(AGBCompileContext context) throws AGBException, AGBCastException {
		AGBTypeNode type = this.getCondition().getType();
		for (AGBCaseNode caseNode : this.getCases()) {
			if (!caseNode.getValue().getType().canCast(context, type)) {
				throw new AGBCastException(caseNode.getValue().getType(), type, caseNode.getValue().getToken());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReturned() {
		if (this.elcmd == null) {
			return false;
		}
		for (AGBCaseNode node : this.cases) {
			if (!node.isReturned()) {
				return false;
			}
		}
		if (!this.elcmd.isReturned()) {
			return false;
		}
		return true;
	}

	/**
	 * @return the condition
	 */
	public AGBRValueNode getCondition() {
		return condition;
	}

	/**
	 * @return the cases
	 */
	public List<AGBCaseNode> getCases() {
		return cases;
	}

	/**
	 * @return the elcmd
	 */
	public AGBBlockNode getElcmd() {
		return elcmd;
	}

	/**
	 * @param condition
	 *            the condition to set
	 */
	public void setCondition(AGBRValueNode condition) {
		this.condition = condition;
	}

	/**
	 * @param cases
	 *            the cases to set
	 */
	public void setCases(List<AGBCaseNode> cases) {
		this.cases = cases;
	}

	/**
	 * @param elcmd
	 *            the elcmd to set
	 */
	public void setElcmd(AGBBlockNode elcmd) {
		this.elcmd = elcmd;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("switch");
		sb.append(" ");
		sb.append(this.getCondition().toString());
		sb.append(" ");
		sb.append(this.getCases().toString());
		return sb.toString();
	}

}
