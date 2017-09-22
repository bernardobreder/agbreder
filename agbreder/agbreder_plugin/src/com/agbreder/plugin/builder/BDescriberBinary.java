package com.agbreder.plugin.builder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.agbreder.plugin.util.PatternUtil;

public class BDescriberBinary {

	private BufferedReader reader;

	private static final String INFO = "\\[info\\] : ";

	private static final String NUMBER_OF_SOURCE_BEGIN = INFO + "number of source : ";

	private static final String NUMBER_OF_SOURCE = NUMBER_OF_SOURCE_BEGIN + "(\\d+)";

	private static final String START_CLASS = INFO + "Starting with the source : (\\w.+)";

	private static final String IMPORT = INFO + "import : (\\w.+)";

	private static final String IMPORT_NUMBER = INFO + "number of imports : (\\d+)";

	private static final String TYPE = INFO + "type : (\\w+)";

	private static final String GENERIC = INFO + "generic : ([\\w|\\<|\\>|.| ]*)";

	private static final String GENERIC_NUMBER = INFO + "number of generics : (\\d+)";

	private static final String EXTEND = INFO + "extend : ([\\w|.]*)";

	private static final String EXTEND_NUMBER = INFO + "number of extends : (\\d+)";

	private static final String IMPLEMENT = INFO + "implement : ([\\w|.]*)";

	private static final String IMPLEMENT_NUMBER = INFO + "number of implements : (\\d+)";

	private static final String FIELD = INFO + "field : ([\\w|.]*) ([\\w|.]*)";

	private static final String FIELD_NUMBER = INFO + "number of fields : (\\d+)";

	private static final String METHOD = "[info] : method : ";

	private static final String METHOD_NUMBER = INFO + "number of methods : (\\d+)";

	private String[] classnames;

	private String[] types;

	private String[][] imports;

	private String[][] generics;

	private String[][] extend;

	private String[][] implement;

	private String[][] fieldTypes;

	private String[][] fieldNames;

	private String[][][] methodReturns;

	private String[][][] methodParams;

	private String[][] methodName;

	private String[][][] methodThrows;

	private boolean error;

	public BDescriberBinary(InputStream input) {
		this.reader = new BufferedReader(new InputStreamReader(input));
	}

	public void start() throws IOException {
		String begin = reader.readLine();
		if (begin == null || begin.length() == 0) {
			return;
		}
		try {
			this.init(begin);
		} catch (Exception e) {
			this.error = true;
		}
	}

	private void init(String line) throws IOException {
		Integer sources = new Integer(PatternUtil.get(NUMBER_OF_SOURCE, line));
		this.classnames = new String[sources];
		this.types = new String[sources];
		this.imports = new String[sources][];
		this.generics = new String[sources][];
		this.extend = new String[sources][];
		this.implement = new String[sources][];
		this.fieldTypes = new String[sources][];
		this.fieldNames = new String[sources][];
		this.methodReturns = new String[sources][][];
		this.methodName = new String[sources][];
		this.methodParams = new String[sources][][];
		this.methodThrows = new String[sources][][];
		for (int n = 0; n < sources; n++) {
			this.readSource(n);
		}
	}

	private void readSource(int sourceIndex) throws IOException {
		String line = reader.readLine();
		this.classnames[sourceIndex] = PatternUtil.get(START_CLASS, line);
		this.readImports(sourceIndex);
		this.readTypeStruct(sourceIndex);
		this.readGenerics(sourceIndex);
		this.readExtends(sourceIndex);
		this.readImplements(sourceIndex);
		this.readFields(sourceIndex);
		this.readMethods(sourceIndex);
		line = reader.readLine();
	}

	private void readImports(int sourceIndex) throws IOException {
		String line = reader.readLine();
		Integer size = new Integer(PatternUtil.get(IMPORT_NUMBER, line));
		this.imports[sourceIndex] = new String[size];
		for (int n = 0; n < size; n++) {
			line = reader.readLine();
			this.imports[sourceIndex][n] = PatternUtil.get(IMPORT, line);
		}
	}

	private void readTypeStruct(int sourceIndex) throws IOException {
		String line = reader.readLine();
		String type = PatternUtil.get(TYPE, line);
		this.types[sourceIndex] = type;
	}

	private void readGenerics(int sourceIndex) throws IOException {
		String line = reader.readLine();
		Integer size = new Integer(PatternUtil.get(GENERIC_NUMBER, line));
		this.generics[sourceIndex] = new String[size];
		for (int n = 0; n < size; n++) {
			line = reader.readLine();
			this.generics[sourceIndex][n] = PatternUtil.get(GENERIC, line);
		}
	}

	private void readExtends(int sourceIndex) throws IOException {
		String line = reader.readLine();
		Integer size = new Integer(PatternUtil.get(EXTEND_NUMBER, line));
		this.extend[sourceIndex] = new String[size];
		for (int n = 0; n < size; n++) {
			line = reader.readLine();
			this.extend[sourceIndex][n] = PatternUtil.get(EXTEND, line);
		}
	}

	private void readImplements(int sourceIndex) throws IOException {
		String line = reader.readLine();
		Integer size = new Integer(PatternUtil.get(IMPLEMENT_NUMBER, line));
		this.implement[sourceIndex] = new String[size];
		for (int n = 0; n < size; n++) {
			line = reader.readLine();
			this.implement[sourceIndex][n] = PatternUtil.get(IMPLEMENT, line);
		}
	}

	private void readFields(int sourceIndex) throws IOException {
		String line = reader.readLine();
		Integer size = new Integer(PatternUtil.get(FIELD_NUMBER, line));
		this.fieldTypes[sourceIndex] = new String[size];
		this.fieldNames[sourceIndex] = new String[size];
		for (int n = 0; n < size; n++) {
			line = reader.readLine();
			this.fieldTypes[sourceIndex][n] = PatternUtil.get(FIELD, line, 1);
			this.fieldNames[sourceIndex][n] = PatternUtil.get(FIELD, line, 2);
		}
	}

	private void readMethods(int sourceIndex) throws IOException {
		String line = reader.readLine().trim();
		Integer size = new Integer(PatternUtil.get(METHOD_NUMBER, line));
		this.methodName[sourceIndex] = new String[size];
		this.methodReturns[sourceIndex] = new String[size][];
		this.methodName[sourceIndex] = new String[size];
		this.methodParams[sourceIndex] = new String[size][];
		this.methodThrows[sourceIndex] = new String[size][];
		for (int n = 0; n < size; n++) {
			line = reader.readLine().trim();
			line = line.substring(METHOD.length()).trim();
			if (line.startsWith("public")) {
				line = line.substring("public".length()).trim();
			} else if (line.startsWith("protected")) {
				line = line.substring("protected".length()).trim();
			} else if (line.startsWith("private")) {
				line = line.substring("private".length()).trim();
			} else {
				throw new IllegalStateException();
			}
			{
				List<String> list = new ArrayList<String>();
				line = line.substring(1);
				while (line.charAt(0) != ')') {
					String type = this.readType(line);
					list.add(type);
					line = line.substring(type.length());
				}
				line = line.substring(1).trim();
				this.methodReturns[sourceIndex][n] = list.toArray(new String[list.size()]);
			}
			String name = line.substring(0, line.indexOf(' '));
			this.methodName[sourceIndex][n] = name;
			line = line.substring(name.length()).trim();
			{
				List<String> list = new ArrayList<String>();
				line = line.substring(1);
				while (line.charAt(0) != ')') {
					String type = this.readType(line);
					list.add(type);
					line = line.substring(type.length());
					if (line.charAt(0) != ')') {
						line = line.substring(1);
					}
				}
				line = line.substring(1).trim();
				this.methodParams[sourceIndex][n] = list.toArray(new String[list.size()]);
			}
		}
	}

	private String readType(String line) {
		int genericCount = 0;
		char[] chars = line.toCharArray();
		for (int n = 0; n < chars.length; n++) {
			if ((chars[n] == ',' || chars[n] == ')') && genericCount == 0) {
				return line.substring(0, n);
			} else if (chars[n] == '<') {
				genericCount++;
			} else if (chars[n] == '>') {
				genericCount--;
			}
		}
		return null;
	}

	public String getClassname(int index) {
		return classnames[index];
	}

	public boolean isError() {
		return error;
	}

}
