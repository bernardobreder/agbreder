package breder.xml;
public class Token {

	private final String image;

	private final int type;

	private final int line;

	private final int column;

	public Token(String image, int type, int line, int column) {
		super();
		this.image = image;
		this.type = type;
		this.line = line;
		this.column = column;
	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the line
	 */
	public int getLine() {
		return line;
	}

	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

}
