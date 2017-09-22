package breder.xml;
public class XmlAttNode {

	private final String name;

	private final String value;

	public XmlAttNode(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return name + "=" + value;
	}

}
