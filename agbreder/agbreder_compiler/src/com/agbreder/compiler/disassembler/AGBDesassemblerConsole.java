package com.agbreder.compiler.disassembler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import com.agbreder.compiler.AGBCompiler;
import com.agbreder.compiler.AGBCompilerConsole;
import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.exception.BytecodeDesassemblerException;
import com.agbreder.compiler.exception.DesassemblerException;
import com.agbreder.compiler.util.FileUtil;

/**
 * Console de Desassembler
 * 
 * @author bernardobreder
 */
public class AGBDesassemblerConsole {

	/**
	 * Inicializador
	 * 
	 * @param args
	 * @throws IOException
	 * @throws AGBException
	 */
	public static void main(String[] args) throws AGBException, IOException {
		if (true) {
			ex("class Main do static this main () do Main a = new Main() function str (num, num) func = function str (num a, num b) (Main c = a) do |log.prints,|object.classname,c|| return null end func(1,2) return this end end");
		} else {
			AGBCompilerConsole.main(new String[] { "-o", "../agbreder_vm/binary.agbc", "../agbreder_bdk/src" });
			File file = new File("../agbreder_vm/binary.agbc");
			if (file.exists()) {
				execute(new FileInputStream(file));
			}
		}
		// {
		// InputStream input =
		// new FileInputStream("/Users/bernardobreder/Downloads/arial.ttf");
		// ByteArrayOutputStream output = new ByteArrayOutputStream();
		// for (int n; ((n = input.read()) != -1);) {
		// output.write((char) n);
		// }
		// input.close();
		// String encode = Base64.encode(output.toByteArray());
		// while (encode.length() > 0) {
		// int len = Math.min(1024, encode.length());
		// String line = encode.substring(0, len);
		// encode = encode.substring(len);
		// System.out.println("\""+line+"\"\\");
		// }
		// System.out.println(";");
		// }
	}

	/**
	 * Execute
	 * 
	 * @param code
	 * @throws IOException
	 * @throws AGBException
	 */
	public static void ex(String code) throws IOException, AGBException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ByteArrayInputStream input = new ByteArrayInputStream(code.getBytes("utf8"));
		AGBCompiler.compile(output, input);
		execute(new ByteArrayInputStream(output.toByteArray()));
	}

	/**
	 * @param writer
	 * @param output
	 * @param input
	 * @throws IOException
	 * @throws DesassemblerException
	 * @throws BytecodeDesassemblerException
	 */
	public static void execute(InputStream input) throws IOException, DesassemblerException, BytecodeDesassemblerException {
		PrintWriter writer = new PrintWriter(System.out);
		{
			AGBDesassembler desassembler = new AGBDesassembler(input);
			try {
				desassembler.readHeader();
				desassembler.readBody();
			} finally {
				desassembler.close();
			}
			{
				List<String> list = desassembler.getNumbers();
				writer.println("numbers: {");
				for (String value : list) {
					writer.printf("\t%s\n", value);
				}
				writer.println("}");
			}
			{
				List<String> list = desassembler.getStrings();
				writer.println("strings: {");
				for (String value : list) {
					writer.printf("\t%s\n", value);
				}
				writer.println("}");
			}
			{
				List<AGBInstruction> bytecodes = desassembler.getBytecodes();
				List<AGBClass> list = desassembler.getClasses();
				writer.println("classes: {");
				for (int n = 0, o = 0; n < list.size(); n++) {
					AGBClass classnode = list.get(n);
					writer.printf("\t%d: %s(%d) {\n", n, classnode.getName(), classnode.getExtendIndex());
					writer.printf("\t\tfields: %d\n", classnode.getFields());
					for (int p = 0; p < classnode.getMethods().size(); p++, o++) {
						AGBMethod method = classnode.getMethods().get(p);
						writer.printf("\t\t%d: ", o);
						if (method.isStatic()) {
							writer.printf("static ");
						}
						writer.printf("%s: {\n", method.getName());
						int pc = method.getPc();
						int index = 0;
						int dindex = 0;
						for (int m = 0; m < bytecodes.size(); m++, dindex++) {
							if (method.getPc() == index) {
								break;
							}
							index += bytecodes.get(m).size();
						}
						while (pc != method.getPcs()) {
							AGBInstruction inst = bytecodes.get(dindex++);
							writer.printf("\t\t\t%d: %s\n", pc, inst);
							pc += inst.size();
						}
						writer.println("\t\t}");
					}
					writer.println("\t}");
				}
				writer.println("}");
			}
		}
		writer.close();
	}

}
