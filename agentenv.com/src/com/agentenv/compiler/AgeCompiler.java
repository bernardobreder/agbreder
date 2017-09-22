package com.agentenv.compiler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.agentenv.compiler.lexical.AgeLexical;
import com.agentenv.compiler.lexical.AgeToken;
import com.agentenv.compiler.linker.AgeContext;
import com.agentenv.compiler.linker.AgeLinker;
import com.agentenv.compiler.node.cmd.AgeReturnNode;
import com.agentenv.compiler.node.expr.right.AgeRightValueInterface;
import com.agentenv.compiler.syntax.AgeExpresssionSyntax;
import com.agentenv.compiler.syntax.AgeSyntaxException;
import com.agentenv.compiler.util.EmptryOutputStream;
import com.agentenv.compiler.util.ListSet;

/**
 * Compilador da linguagem Age
 * 
 * @author bernardobreder
 * 
 */
public class AgeCompiler {

	/**
	 * Compila o código fonte
	 * 
	 * @param output
	 * @param input
	 * @throws IOException
	 * @throws AgeSyntaxException
	 */
	public static void compile(OutputStream output, InputStream input) throws IOException, AgeSyntaxException {
		String filename = input.toString();
		List<AgeToken> tokens = AgeLexical.execute(filename, input);
		AgeExpresssionSyntax syntax = new AgeExpresssionSyntax(filename, tokens);
		AgeRightValueInterface value = syntax.execute();
		AgeReturnNode node = new AgeReturnNode(value);
		AgeContext context = new AgeContext();
		EmptryOutputStream fakeOutput = new EmptryOutputStream();
		AgeLinker fakeLinker = new AgeLinker(new DataOutputStream(fakeOutput));
		node.build(context, fakeLinker);
		DataOutputStream out = new DataOutputStream(output);
		{
			ListSet<String> set = context.getNumbers();
			int size = set.size();
			out.writeInt(size);
			for (int n = 0; n < size; n++) {
				out.writeUTF(set.get(n));
			}
		}
		out.writeInt(fakeOutput.size() / 4);
		AgeLinker realLinker = new AgeLinker(out);
		node.build(context, realLinker);
		output.close();
	}

}
