package com.agbreder.compiler.token;

/**
 * Token
 * 
 * @author bernardobreder
 */
public class AGBToken {

	private final String path;

	private final String image;

	private final int type;

	private final int line;

	private final int column;

	/**
	 * Construtor
	 * 
	 * @param path
	 * @param image
	 * @param type
	 * @param line
	 * @param column
	 */
	public AGBToken(String path, String image, int type, int line, int column) {
		super();
		this.path = path;
		this.image = image;
		this.type = type;
		this.line = line;
		this.column = column;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return image;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((image == null) ? 0 : image.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AGBToken other = (AGBToken) obj;
		if (image == null) {
			if (other.image != null)
				return false;
		} else if (!image.equals(other.image))
			return false;
		return true;
	}

}
