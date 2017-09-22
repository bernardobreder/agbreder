package com.bp2pb.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import breder.util.util.input.InputStreamUtil;

/**
 * Página básica
 * 
 * @author bernardobreder
 */
public class HtmlPageServlet extends HtmlServlet {
	
	private static final String NBSP = "&#160;";
	
	/** Cache das páginas */
	private static final Map<String, byte[]> CACHE =
		new WeakHashMap<String, byte[]>();
	
	private static final String[] HEADERS = new String[] { "", "*", "**", "***",
			"****", "*****" };
	
	private static final Map<String, String> CHARACTERS =
		new HashMap<String, String>();
	
	static {
		CHARACTERS.put("<", "&#60;");
		CHARACTERS.put(">", "&#62;");
		CHARACTERS.put("\"", "&#34;");
		CHARACTERS.put("'", "&#39;");
		CHARACTERS.put("\t", NBSP + NBSP + NBSP + NBSP + NBSP + NBSP + NBSP + NBSP);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws Exception
	 */
	@Override
	protected void action(HttpServletRequest req, HttpServletResponse resp)
		throws Exception {
		String uri = req.getServletPath();
		if (uri.endsWith(".html")) {
			uri = uri.substring(0, uri.length() - ".html".length()) + ".txt";
		}
		byte[] cache = CACHE.get(uri);
		if (cache == null) {
			HtmlPage htmlpage = new HtmlPage(new HashSet<String>(), "");
			InputStream resource =
				this.getServletContext().getResourceAsStream("/WEB-INF/pag" + uri);
			if (resource != null) {
				try {
					String content =
						new String(InputStreamUtil.getBytes(resource), "utf-8");
					htmlpage = this.action(content);
				} finally {
					resource.close();
				}
			}
			{
				String header = this.getHeaderPage();
				header = header.replace("{{keywords}}", toString(htmlpage.describer));
				if (req.getHeader("user-agent").toLowerCase().contains("mobile")) {
					header = header.replace("{{style}}", "pub/small.css");
				} else {
					header = header.replace("{{style}}", "pub/large.css");
				}
				String tail = this.getTailPage();
				String page = header + htmlpage.page + tail;
				page = page.replaceAll("\n", " ").replace('\t', ' ');
				while (page.indexOf('\t') >= 0) {
					page = page.replace("  ", " ");
				}
				while (page.indexOf("  ") >= 0) {
					page = page.replace("  ", " ");
				}
				while (page.indexOf("> <") >= 0) {
					page = page.replace("> <", "><");
				}
				cache = page.getBytes("utf-8");
				if (req.getLocalPort() == 80) {
					CACHE.put(uri, cache);
				}
			}
		}
		resp.getOutputStream().write(cache);
	}
	
	/**
	 * Retorna as descrições
	 * 
	 * @param describer
	 * @return
	 */
	private String toString(Set<String> set) {
		Set<String> words = new HashSet<String>();
		for (String value : set) {
			String[] values = value.trim().split(" ");
			for (String item : values) {
				words.add(item);
			}
		}
		List<String> list = new ArrayList<String>(words.size());
		for (String value : words) {
			String[] lines = value.split("\n");
			for (String line : lines) {
				line = line.trim().toLowerCase();
				if (line.length() > 0) {
					char char0 = line.charAt(0);
					if (char0 >= 'a' && char0 <= 'z') {
						list.add(line);
					}
				}
			}
		}
		Collections.sort(list);
		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < list.size(); n++) {
			String value = list.get(n);
			sb.append(value + " ");
		}
		return sb.toString().trim();
	}
	
	/**
	 * Retorna o rodapé da pagina
	 * 
	 * @return pagina
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private String getTailPage() throws UnsupportedEncodingException, IOException {
		return new String(InputStreamUtil.getBytes(this.getServletContext()
			.getResourceAsStream("/WEB-INF/htm/tail.html")), "utf-8");
	}
	
	/**
	 * Retorna o rodapé da pagina
	 * 
	 * @return pagina
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private String getHeaderPage() throws UnsupportedEncodingException,
		IOException {
		String page =
			new String(InputStreamUtil.getBytes(this.getServletContext()
				.getResourceAsStream("/WEB-INF/htm/header.html")), "utf-8");
		return page;
	}
	
	/**
	 * Processa a página
	 * 
	 * @param text
	 * @return
	 */
	private HtmlPage action(String text) {
		Set<String> describer = new HashSet<String>();
		StringBuilder sb = new StringBuilder(text.length());
		String[] lines = text.split("\n");
		for (String line : lines) {
			sb.append("<div class='p'>");
			if (line.length() == 0) {
				line = NBSP;
			}
			for (String key : CHARACTERS.keySet()) {
				line = line.replaceAll(key, CHARACTERS.get(key));
			}
			{
				for (int n = 5; n >= 1; n--) {
					String header = HEADERS[n];
					int begin = line.indexOf(header);
					while (begin >= 0) {
						if (begin == 0 || line.charAt(begin - 1) != '\\') {
							int end = line.indexOf(header, begin + n);
							if (end >= 0) {
								String word = line.substring(begin + n, end);
								describer.add(word);
								line =
									String.format("%s<span class='p%d'>%s</span>%s",
										line.substring(0, begin), n, word, line.substring(end + n));
							}
						}
						begin = line.indexOf(header, begin + n);
					}
				}
			}
			{
				int begin = line.indexOf('|');
				while (begin >= 0) {
					int left = line.indexOf("|", begin + 1);
					if (left >= 0) {
						int right = line.indexOf("|", left + 1);
						if (right >= 0) {
							int end = line.indexOf("|", right + 1);
							String cmd = line.substring(begin + 1, left).trim();
							String att = line.substring(left + 1, right);
							String link = line.substring(right + 1, end);
							if (cmd.equals("a")) {
								describer.add(att);
								line =
									String.format("%s<a href='%s'>%s</a>%s",
										line.substring(0, begin), link, att,
										line.substring(end + 1));
							} else if (cmd.equals("img")) {
								line =
									String.format("%s<center><img src='%s'/></center>%s",
										line.substring(0, begin), att, line.substring(end + 1));
							}
						}
					}
					begin = line.indexOf('|');
				}
			}
			sb.append(line);
			sb.append("</div>");
		}
		return new HtmlPage(describer, sb.toString());
	}
	
	private static class HtmlPage {
		
		public Set<String> describer;
		
		public String page;
		
		public HtmlPage(Set<String> describer, String page) {
			super();
			this.describer = describer;
			this.page = page;
		}
		
	}
	
}
