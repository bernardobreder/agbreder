package com.agbreder.compiler.token;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Construtor de Tokens
 * 
 * @author bernardobreder
 */
public class AGBLexical {

	/** Tipos de Token */
	private static final Map<String, Integer> TYPES = new HashMap<String, Integer>();

	static {
		AGBTokenType[] values = AGBTokenType.values();
		for (int n = 0; n < values.length; n++) {
			AGBTokenType token = values[n];
			TYPES.put(token.getName(), token.getId());
		}
	}

	/**
	 * Realiza o analisador lexical
	 * 
	 * @param path
	 * @param input
	 * @return lista de Token
	 * @throws IOException
	 */
	public static List<AGBToken> execute(String path, InputStream input) throws IOException {
		List<AGBToken> list = new ArrayList<AGBToken>();
		AGBLexicalStream stream = new AGBLexicalStream(input);
		{
			while (!stream.isEof()) {
				char c = stream.get();
				if (c <= ' ') {
					stream.read();
					continue;
				}
				stream.mark();
				if (isIdentify(c)) {
					list.add(readId(stream, path));
				} else if (isString(c)) {
					list.add(readString(stream, path));
				} else if (isResource(c)) {
					list.add(readResource(stream, path));
				} else if (isNumber(c)) {
					list.add(readNumber(stream, path));
				} else if (c == '/' && stream.get(1) == '/') {
					readCommentLine(stream, path);
				} else if (c == '/' && stream.get(1) == '*') {
					readCommentBlock(stream, path);
				} else {
					list.add(readSymbol(stream, path));
				}
			}
		}
		return list;
	}

	/**
	 * Indica se é um n�mero
	 * 
	 * @param c
	 * @return um n�mero
	 */
	private static boolean isNumber(char c) {
		return c >= '0' && c <= '9';
	}

	/**
	 * Indica se � uma String
	 * 
	 * @param c
	 * @return uma String
	 */
	private static boolean isString(char c) {
		return c == '\"';
	}

	/**
	 * Indica se � uma String
	 * 
	 * @param c
	 * @return uma String
	 */
	private static boolean isResource(char c) {
		return c == '\'';
	}

	/**
	 * Indica se � um identificador
	 * 
	 * @param c
	 * @return identificador
	 */
	private static boolean isIdentify(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
	}

	/**
	 * @param stream
	 * @param path
	 * @return token
	 */
	private static AGBToken readSymbol(AGBLexicalStream stream, String path) {
		stream.read();
		String image = stream.substring();
		int type = AGBTokenType.SYMBOL.getId();
		Integer tokenType = TYPES.get(image);
		if (tokenType != null) {
			type = tokenType;
		}
		return new AGBToken(path, image, type, stream.getLine(), stream.getColumn());
	}

	/**
	 * @param stream
	 * @param path
	 * @return token
	 */
	private static AGBToken readNumber(AGBLexicalStream stream, String path) {
		boolean dot = false;
		stream.read();
		for (;;) {
			char c = stream.get();
			if (!(isNumber(c) || c == '.')) {
				break;
			}
			if (c == '.') {
				if (dot) {
					break;
				} else {
					dot = true;
				}
			}
			stream.read();
		}
		String image = stream.substring();
		return new AGBToken(path, image, AGBTokenType.NUMBER.getId(), stream.getLine(), stream.getColumn() - image.length() + 1);
	}

	/**
	 * @param stream
	 * @param path
	 * @return token
	 */
	private static AGBToken readString(AGBLexicalStream stream, String path) {
		StringBuilder sb = new StringBuilder();
		stream.read();
		for (; !stream.isEof();) {
			char c = stream.get();
			if (c == '\\') {
				char c1 = stream.get(1);
				switch (c1) {
				case '\\':
					c = '\\';
					stream.read();
					break;
				case 't':
					c = '\t';
					stream.read();
					break;
				case 'n':
					c = '\n';
					stream.read();
					break;
				case 'r':
					c = '\r';
					stream.read();
					break;
				case '\"':
					c = '\"';
					stream.read();
					break;
				}
			} else if (c == '\"') {
				stream.read();
				break;
			}
			sb.append(c);
			stream.read();
		}
		String image = sb.toString();
		return new AGBToken(path, image, AGBTokenType.STRING.getId(), stream.getLine(), stream.getColumn() - image.length() - 1);
	}

	/**
	 * @param stream
	 * @param path
	 * @return token
	 */
	private static AGBToken readResource(AGBLexicalStream stream, String path) {
		StringBuilder sb = new StringBuilder();
		stream.read();
		for (; !stream.isEof();) {
			char c = stream.get();
			if (c == '\'') {
				stream.read();
				break;
			}
			sb.append(c);
			stream.read();
		}
		String image = sb.toString();
		return new AGBToken(path, image, AGBTokenType.RESOURCE.getId(), stream.getLine(), stream.getColumn() - image.length() - 1);
	}

	/**
	 * @param stream
	 * @param path
	 * @return token
	 */
	private static AGBToken readId(AGBLexicalStream stream, String path) {
		stream.read();
		for (;;) {
			char c = stream.get();
			if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || isNumber(c) || c == '_')) {
				break;
			}
			stream.read();
		}
		String image = stream.substring();
		int type = AGBTokenType.ID.getId();
		Integer tokenType = TYPES.get(image);
		if (tokenType != null) {
			type = tokenType;
		}
		return new AGBToken(path, image, type, stream.getLine(), stream.getColumn() - image.length() + 1);
	}

	/**
	 * @param stream
	 * @param path
	 * @return token
	 */
	private static AGBToken readCommentLine(AGBLexicalStream stream, String path) {
		stream.read();
		stream.read();
		for (; !stream.isEof();) {
			char c = stream.get();
			if (c == '\n') {
				stream.read();
				break;
			}
			stream.read();
		}
		return null;
	}

	/**
	 * @param stream
	 * @param path
	 * @return token
	 */
	private static AGBToken readCommentBlock(AGBLexicalStream stream, String path) {
		stream.read();
		stream.read();
		for (; !stream.isEof();) {
			char c = stream.get();
			if (c == '*' && stream.get(1) == '/') {
				stream.read();
				stream.read();
				break;
			}
			stream.read();
		}
		return null;
	}

}