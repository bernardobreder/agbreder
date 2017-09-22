package com.agbreder.compiler.parser.node;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.exception.AGBTokenException;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.token.AGBToken;
import com.agbreder.compiler.util.Base64;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBResourceNode extends AGBStringNode {

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param token
	 */
	public AGBResourceNode(AGBNode parent, AGBToken token) {
		super(parent, token);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws AGBTokenException
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		AGBToken token = this.getToken();

		File file = new File(token.getImage().replace('/', File.separatorChar));
		String image;
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream((int) file.length());
			FileInputStream input = new FileInputStream(file);
			try {
				for (int n; ((n = input.read()) != -1);) {
					output.write((char) n);
				}
				image = Base64.encode(output.toByteArray());
			} finally {
				input.close();
			}
		} catch (FileNotFoundException e) {
			throw new AGBTokenException("not found the resource at: " + file.getAbsolutePath(), token);
		} catch (IOException e) {
			throw new AGBTokenException("erro at reading file", token);
		}
		this.setToken(new AGBToken(token.getPath(), image, token.getType(), token.getLine(), token.getColumn()));
		super.header(context);
	}

}
