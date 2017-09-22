package com.agbreder.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.util.AGBArgumentConsole;

/**
 * Console de Compilador
 * 
 * @author bernardobreder
 */
public class AGBCompilerHelper {

	private final List<File> inputs = new ArrayList<File>();

	private final Set<String> classes = new HashSet<String>();

	private File outputFile = new File("binary.agbc");

	private final List<File> sources = new ArrayList<File>();

	/**
	 * Adiciona um folder ao source
	 * 
	 * @param file
	 * @param sourceClass
	 * @throws AGBException
	 */
	private void add(File file, Set<String> sourceClass) throws AGBException {
		if (file.isFile()) {
			String name = file.getName();
			if (sourceClass.contains(name)) {
				throw new AGBException(String.format("The class '%s' is duplicated in the same source '%s'", name, file.toString()));
			}
			if (!this.classes.contains(name)) {
				sourceClass.add(name);
				this.classes.add(name);
				inputs.add(file);
			}
		} else {
			File[] files = file.listFiles();
			if (files != null) {
				for (File item : files) {
					if (!item.isHidden()) {
						this.add(item, sourceClass);
					}
				}
			}
		}
	}

	/**
	 * Inicia a compilação
	 * 
	 * @return base64
	 * @throws AGBException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void start() throws FileNotFoundException, IOException, AGBException {
		for (File source : this.sources) {
			Set<String> sourceClass = new HashSet<String>();
			this.add(source, sourceClass);
		}
		File[] files = inputs.toArray(new File[inputs.size()]);
		InputStream[] inputs = new InputStream[files.length];
		for (int n = 0; n < files.length; n++) {
			inputs[n] = new LazyFileInputStream(files[n]);
		}
		FileOutputStream output = new FileOutputStream(getOutputFile());
		AGBCompiler.compile(output, inputs);
	}

	/**
	 * Inicializador
	 * 
	 * @param args
	 * @return base64
	 * @throws AGBException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException, AGBException {
		AGBArgumentConsole arguments = new AGBArgumentConsole(args);
		List<String> sources = arguments.gets();
		AGBCompilerHelper console = new AGBCompilerHelper();
		for (String source : sources) {
			console.addSource(new File(source));
		}
		if (arguments.has("o")) {
			console.setOutputFile(new File(arguments.get("o")));
		}
		console.start();
	}

	public void addSource(File source) {
		this.sources.add(source);
	}

	/**
	 * @return the outputFile
	 */
	public File getOutputFile() {
		return outputFile;
	}

	/**
	 * @param outputFile
	 *            the outputFile to set
	 */
	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}

}
