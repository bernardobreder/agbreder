package breder.xml;
public class XmlTextNode extends XmlNode {

	private final String text;

	public XmlTextNode(String text) {
		this.text = text;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "XmlTextNode [text=" + text + "]";
	}

}
