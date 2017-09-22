package breder.xml;
import java.util.Arrays;

public class XmlTagNode extends XmlNode {

	private final String name;

	private final XmlAttNode[] atts;

	private final XmlNode[] nodes;

	public XmlTagNode(String name, XmlAttNode[] atts, XmlNode[] nodes) {
		super();
		this.name = name;
		this.atts = atts;
		this.nodes = nodes;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the atts
	 */
	public XmlAttNode[] getAtts() {
		return atts;
	}

	/**
	 * @return the nodes
	 */
	public XmlNode[] getNodes() {
		return nodes;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "XmlTagNode [name=" + name + ", atts=" + Arrays.toString(atts) + ", nodes=" + Arrays.toString(nodes) + "]";
	}

}
