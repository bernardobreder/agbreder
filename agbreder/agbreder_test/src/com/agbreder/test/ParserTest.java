package com.agbreder.test;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.junit.Test;

import com.agbreder.compiler.parser.AGBParser;
import com.agbreder.compiler.parser.node.AGBCommandNode;
import com.agbreder.compiler.parser.node.AGBNode;
import com.agbreder.compiler.token.AGBLexical;
import com.agbreder.compiler.util.FileUtil;

/**
 * Testador de Compilador
 * 
 * @author bernardobreder
 */
public class ParserTest extends AbstractTest {

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception {
		File dir = new File("../agbreder_bdk/src");
		List<File> files = FileUtil.list(dir, "agb");
		for (File file : files) {
			AGBParser parser = new AGBParser(file.toString(), AGBLexical.execute(file.toString(), new FileInputStream(file)));
			List<AGBCommandNode> nodes = parser.execute();
			for (AGBNode node : nodes) {
				node.toString();
			}
		}
	}

}
