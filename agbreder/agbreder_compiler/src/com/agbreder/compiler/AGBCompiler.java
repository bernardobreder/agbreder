package com.agbreder.compiler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.exception.AGBTokenException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.parser.AGBParser;
import com.agbreder.compiler.parser.node.AGBClassNode;
import com.agbreder.compiler.parser.node.AGBMethodNode;
import com.agbreder.compiler.parser.node.AGBNode;
import com.agbreder.compiler.token.AGBLexical;
import com.agbreder.compiler.token.AGBToken;
import com.agbreder.compiler.util.EmptryOutputStream;

/**
 * Classe de Compilador responsável por compilar um conjunto de código fonte
 * 
 * @author bernardobreder
 */
public abstract class AGBCompiler {

	/**
	 * Compila uma stream de codigo fonte
	 * 
	 * @param outputFile
	 *            arquivo de saída
	 * @param files
	 * @return base64
	 * @throws IOException
	 * @throws AGBException
	 */
	public static void compile(OutputStream foutput, InputStream... files) throws IOException, AGBException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		// Compilado num output de bytes
		{
			List<AGBNode> nodes = parse(files);
			AGBCompileContext context = new AGBCompileContext();
			index(context, nodes);
			head(context, nodes);
			body(context, nodes);
			link(context, nodes);
			OpcodeOutputStream opcodeOutput = new OpcodeOutputStream(bytes);
			try {
				opcodeOutput.writeByte(0xBB);
				{
					List<String> list = context.getNumbers();
					int size = list.size();
					opcodeOutput.writeInt(size);
					for (int n = 0; n < size; n++) {
						String value = list.get(n);
						opcodeOutput.writeUTF(value);
					}
				}
				{
					List<String> list = context.getStrings();
					int size = list.size();
					opcodeOutput.writeInt(size);
					for (int n = 0; n < size; n++) {
						String value = list.get(n);
						opcodeOutput.writeUTF(value);
					}
				}
				List<AGBClassNode> classes = context.getClasses();
				{
					int size = classes.size();
					opcodeOutput.writeInt(size);
					for (int n = 0; n < size; n++) {
						AGBClassNode node = classes.get(n);
						String value = node.getName().getImage();
						opcodeOutput.writeUTF(value);
						if (node.getExtend() != null) {
							opcodeOutput.writeInt(node.getExtend().getIndex());
						} else {
							opcodeOutput.writeInt(-1);
						}
						opcodeOutput.writeInt(node.getFields().size());
					}
				}
				int pcs = 0;
				{
					int csize = classes.size();
					opcodeOutput.writeInt(csize);
					OpcodeOutputStream output = new OpcodeOutputStream(new EmptryOutputStream());
					for (int c = 0; c < csize; c++) {
						AGBClassNode classnode = classes.get(c);
						List<AGBMethodNode> methods = classnode.getMethods();
						int msize = methods.size();
						opcodeOutput.writeInt(msize);
						for (int n = 0; n < msize; n++) {
							AGBMethodNode node = methods.get(n);
							node.build(output);
							opcodeOutput.writeUTF(node.getNameParameters());
							opcodeOutput.writeInt(node.isStatic() ? 1 : 0);
							opcodeOutput.writeInt(pcs);
							pcs = output.getPc();
							opcodeOutput.writeInt(pcs);
						}
					}
				}
				opcodeOutput.writeByte(255);
				opcodeOutput.writeInt(pcs);
				{
					List<AGBMethodNode> list = context.getMethods();
					int size = list.size();
					for (int n = 0; n < size; n++) {
						AGBMethodNode node = list.get(n);
						node.build(opcodeOutput);
					}
				}
				opcodeOutput.writeByte(255);
			} finally {
				opcodeOutput.close();
			}
		}
		// Escrevendo no arquivo
		{
			try {
				foutput.write(bytes.toByteArray());
			} finally {
				foutput.close();
			}
		}
	}

	/**
	 * @param files
	 * @return
	 * @throws IOException
	 * @throws AGBException
	 */
	private static List<AGBNode> parse(InputStream... files) throws IOException, AGBException {
		List<AGBNode> nodes = new ArrayList<AGBNode>();
		for (InputStream input : files) {
			String filename = input.toString();
			List<AGBToken> tokens = AGBLexical.execute(filename, input);
			AGBParser parser = new AGBParser(filename, tokens);
			nodes.addAll(parser.execute());
		}
		return nodes;
	}

	/**
	 * @param context
	 * @param nodes
	 * @throws AGBTokenException
	 */
	private static void index(AGBCompileContext context, List<AGBNode> nodes) throws AGBTokenException {
		for (int n = 0; n < nodes.size(); n++) {
			AGBNode node = nodes.get(n);
			if (node instanceof AGBClassNode) {
				AGBClassNode classnode = (AGBClassNode) node;
				for (int m = 0; m < classnode.getMethods().size(); m++) {
					AGBMethodNode methodnode = classnode.getMethods().get(m);
					methodnode.setIndex(context.addMethod(methodnode));
				}
				classnode.setIndex(context.addClass(classnode));
			}
		}
	}

	/**
	 * @param context
	 * @param nodes
	 * @throws AGBException
	 */
	private static void link(AGBCompileContext context, List<AGBNode> nodes) throws AGBException {
		for (int n = 0; n < nodes.size(); n++) {
			nodes.get(n).link(context);
		}
	}

	/**
	 * @param context
	 * @param nodes
	 * @throws AGBException
	 */
	private static void body(AGBCompileContext context, List<AGBNode> nodes) throws AGBException {
		for (int n = 0; n < nodes.size(); n++) {
			nodes.get(n).body(context);
		}
	}

	/**
	 * @param context
	 * @param nodes
	 * @throws AGBException
	 */
	private static void head(AGBCompileContext context, List<AGBNode> nodes) throws AGBException {
		for (int n = 0; n < nodes.size(); n++) {
			nodes.get(n).header(context);
		}
	}

}
