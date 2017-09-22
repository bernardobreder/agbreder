package com.agbreder.plugin.swt;

public class GBC {

	private int line;

	private int column;

	private int lineSpan;

	private int columnSpan;

	private Fill fill;

	public GBC(int lin, int col) {
		this.line = lin;
		this.column = col;
		this.lineSpan = 1;
		this.columnSpan = 1;
		this.fill = Fill.NONE;
	}

	public GBC horizontal() {
		this.fill = Fill.HORIZONTAL;
		return this;
	}

	public GBC vertical() {
		this.fill = Fill.VERTICAL;
		return this;
	}

	public GBC both() {
		this.fill = Fill.BOTH;
		return this;
	}

	public GBC spans(int line, int col) {
		this.lineSpan = line;
		this.columnSpan = col;
		return this;
	}

	public GBC lineSpan(int count) {
		this.lineSpan = count;
		return this;
	}

	public GBC columnSpan(int count) {
		this.columnSpan = count;
		return this;
	}

	public boolean is(Fill fill) {
		return this.fill == fill;
	}

	public Fill getFill() {
		return fill;
	}

	public int getLineSpan() {
		return lineSpan;
	}

	public int getColumnSpan() {
		return columnSpan;
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}

	public static enum Fill {
		NONE, HORIZONTAL, VERTICAL, BOTH;
	}

}
