package com.agbreder.compiler.parser.node;

import java.io.IOException;
import java.util.List;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.exception.AGBTokenException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.util.LightArrayList;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBNewNode extends AGBRValueNode {

	private final List<AGBValueNode> params = new LightArrayList<AGBValueNode>();

	private int index;

	private AGBMethodNode method;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param token
	 * @param params
	 * @param type
	 * @param params
	 */
	public AGBNewNode(AGBNode parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		context.getDependences().add(this.getType().getToken().getImage());
		int size = this.getParams().size();
		for (int n = 0; n < size; n++) {
			this.getParams().get(n).header(context);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		int size = this.getParams().size();
		for (int n = 0; n < size; n++) {
			this.getParams().get(n).body(context);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		{
			int size = this.getParams().size();
			for (int n = 0; n < size; n++) {
				this.getParams().get(n).link(context);
			}
		}
		{
			this.index = this.getType().getRef(context).getIndex();
		}
		AGBClassNode classnode = this.getType().getRef(context);
		{
			String name = classnode.getName().getImage();
			List<AGBMethodNode> methods = classnode.findMethod(name, false);
			int msize = methods.size();
			if (msize > 0) {
				for (int n = 0; n < msize; n++) {
					AGBMethodNode methodnode = methods.get(n);
					List<AGBVariableNode> methodparams = methodnode.getParams();
					int psize = methodparams.size();
					if (psize == this.getParams().size()) {
						boolean found = true;
						for (int p = 0; p < psize; p++) {
							AGBVariableNode paramnode = methodparams.get(p);
							AGBValueNode paramthis = this.getParams().get(p);
							if (!paramthis.getType().canCast(context, paramnode.getType())) {
								found = false;
								break;
							}
						}
						if (found) {
							this.method = methodnode;
							break;
						}
					}
				}
				if (this.method == null) {
					throw new AGBTokenException("not found the construtor", this.getType().getToken());
				}
			}
		}
		if (this.method == null) {
			classnode = classnode.getExtend();
			while (classnode != null && this.method == null) {
				String name = classnode.getName().getImage();
				List<AGBMethodNode> methods = classnode.getMethods();
				int msize = methods.size();
				for (int n = 0; n < msize; n++) {
					AGBMethodNode methodnode = methods.get(n);
					if (!methodnode.isStatic()) {
						List<AGBVariableNode> methodparams = methodnode.getParams();
						int psize = methodparams.size();
						if (psize == this.getParams().size()) {
							if (methodnode.getName().getImage().equals(name)) {
								boolean found = true;
								for (int p = 0; p < psize; p++) {
									AGBVariableNode paramnode = methodparams.get(p);
									AGBValueNode paramthis = this.getParams().get(p);
									if (!paramthis.getType().canCast(context, paramnode.getType())) {
										found = false;
										break;
									}
								}
								if (found) {
									this.method = methodnode;
									break;
								}
							}
						}
					}
				}
				classnode = classnode.getExtend();
			}
		}
		if (this.method == null && this.getParams().size() > 0) {
			throw new AGBTokenException("can not found the constructor", this.getToken());
		}
		if (this.method != null) {
			AGBCallNode.checkThrow(context, this, method);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException, AGBException {
		int counter = output.getCounter();
		{
			int size = this.getParams().size();
			for (int n = 0; n < size; n++) {
				this.getParams().get(n).build(output);
			}
		}
		output.opLoadNew(this.index);
		if (this.method != null) {
			output.opJumpCall(method.getIndex(), method.getParams().size());
		}
		output.setCounter(counter + 1);
	}

	/**
	 * @return the params
	 */
	public List<AGBValueNode> getParams() {
		return params;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("new");
		sb.append(" ");
		sb.append(this.getType());
		sb.append(this.getParams());
		return sb.toString();
	}

}
