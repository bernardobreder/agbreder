package com.agbreder.plugin.element.b;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;

import com.agbreder.plugin.element.BFile;
import com.agbreder.plugin.element.Element;
import com.agbreder.plugin.element.Parent;


public class BElement extends BFile {

	public BElement(Parent<?> parent, IFile file) {
		super(parent, file);
	}

	@Override
	protected Element[] doRefresh() {
		List<Element> list = new ArrayList<Element>();
		try {
			InputStream input = this.getResource().getContents();
			int c = input.read();
			while (c != ';') {
				StringBuilder sb = new StringBuilder();
				sb.append((char) c);
				while (c != '{') {
					sb.append((char) c);
					c = input.read();
				}
				Matcher m = Pattern.compile("([a-zA-Z]+)[.([a-zA-Z]+)]*")
						.matcher(sb.toString());
				int n = m.groupCount();
				Element root;
				while (n != 0) {
					if (n == 1) {
						root = new BClass(this);
					} else {
						root = new PackageElement(this, m.group(n));
					}
					n--;
				}
			}
		} catch (Exception e) {
		}
		return list.toArray(new Element[0]);
	}

}