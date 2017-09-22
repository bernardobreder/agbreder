package com.agbreder.compiler.token;

/**
 * Tipos de Tokens
 * 
 * @author bernardobreder
 */
public enum AGBTokenType {

	/** Token */
	ID("<id>"),
	/** Token */
	SYMBOL("<symbol>"),
	/** Token */
	NUMBER("<number>"),
	/** Token */
	STRING("<string>"),
	/** Token */
	RESOURCE("<resource>"),
	/** Token */
	NUM("num"),
	/** Token */
	BYTE("byte"),
	/** Token */
	STR("str"),
	/** Token */
	BOOL("bool"),
	/** Token */
	OBJ("obj"),
	/** Token */
	THIS("this"),
	/** Token */
	STATIC("static"),
	/** Token */
	ABSTRACT("abstract"),
	/** Token */
	EXTENDS("extends"),
	/** Token */
	CLASS("class"),
	/** Token */
	DOT("."),
	/** Token */
	COMMA(","),
	/** Token */
	SEMICOMMA(";"),
	/** Token */
	DOTDOT(":"),
	/** Token */
	LPARAM("("),
	/** Token */
	RPARAM(")"),
	/** Token */
	LBLOCK("{"),
	/** Token */
	RBLOCK("}"),
	/** Token */
	LARRAY("["),
	/** Token */
	RARRAY("]"),
	/** Token */
	IF("if"),
	/** Token */
	ELSE("else"),
	/** Token */
	SWITCH("switch"),
	/** Token */
	CASE("case"),
	/** Token */
	FOR("for"),
	/** Token */
	WHILE("while"),
	/** Token */
	REPEAT("repeat"),
	/** Token */
	RETURN("return"),
	/** Token */
	DEF("def"),
	/** Token */
	NEW("new"),
	/** Token */
	FUNCTION("function"),
	/** Token */
	SUM("+"),
	/** Token */
	SUB("-"),
	/** Token */
	MUL("*"),
	/** Token */
	DIV("/"),
	/** Token */
	NOT("!"),
	/** Token */
	MOD("%"),
	/** Token */
	AND("and"),
	/** Token */
	OR("or"),
	/** Token */
	AND_BIT("andbit"),
	/** Token */
	OR_BIT("orbit"),
	/** Token */
	EQUAL("="),
	/** Token */
	GREATER(">"),
	/** Token */
	LOWER("<"),
	/** Token */
	ASK("?"),
	/** Token */
	TRUE("true"),
	/** Token */
	BREAK("break"),
	/** Token */
	FALSE("false"),
	/** Token */
	NULL("null"),
	/** Token */
	STRUCT("struct"),
	/** Token */
	INTERFACE("interface"),
	/** Token */
	DO("do"),
	/** Token */
	END("end"),
	/** Token */
	SUPER("super"),
	/** Token */
	TRY("try"),
	/** Token */
	CATCH("catch"),
	/** Token */
	THROW("throw"),
	/** Token */
	RUNTIME("runtime"),
	/** Token */
	NATIVE("|");

	private final String name;

	/**
	 * Construtor
	 * 
	 * @param id
	 * @param name
	 */
	private AGBTokenType(String name) {
		this.name = name.intern();
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return this.ordinal() + 1;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
