package com.agbreder.plugin.console;

import java.io.IOException;
import java.util.List;

import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleOutputStream;

public class BConsole {

	private IOConsole console;
	private IOConsoleOutputStream output;

	public BConsole(IOConsole console) {
		this.console = console;
		this.output = this.console.newOutputStream();
	}

	public void print(String text) throws IOException {
		this.output.write(text);
	}

	public void println(String text) throws IOException {
		this.output.write(text + "\n");
	}

	public void print(String format, Object... objects) throws IOException {
		this.print(String.format(format, objects));
	}

	public void println(String format, Object... objects) throws IOException {
		this.println(String.format(format, objects));
	}

	public void clear() {
		this.console.clearConsole();
	}

	public void close() throws IOException {
		this.output.close();
		this.console.destroy();
	}

	public void print(List<String> args) throws IOException {
		this.println("\tCommand Line : ");
		this.println(args.get(0));
		for (int n = 1; n < args.size(); n++) {
			String arg = args.get(n);
			if (arg.startsWith("-") && n != args.size() - 1 && !args.get(n + 1).startsWith("-")) {
				this.println("\t" + arg + " " + args.get(++n));
			} else {
				this.println("\t" + arg);
			}
		}
		this.println("");
	}

}
