import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.zip.DeflaterOutputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Test {

	public static void main(String[] args) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		Options options = new Options();
		options.addOption("t", false, "display current time");
		options.addOption(OptionBuilder.hasArg().withLongOpt("file").withDescription("echo the arguments").create("f"));
		options.addOption("compress", false, "compress the result");
		args = new String[] { "-file", "WEB-INF/classes/Test.class", "-compress" };
		CommandLineParser parser = new GnuParser();
		try {
			CommandLine cmd = parser.parse(options, args, false);
			DataOutputStream data;
			if (cmd.hasOption("compress")) {
				data = new DataOutputStream(new DeflaterOutputStream(output));
			} else {
				data = new DataOutputStream(output);
			}
			if (cmd.hasOption("file")) {
				PrintWriter writer = new PrintWriter(data);
				String[] values = cmd.getOptionValues("file");
				for (String value : values) {
					try {
						FileInputStream input = new FileInputStream(value);
						for (int n; ((n = input.read()) != -1);) {
							data.writeByte(n);
						}
						input.close();
					} catch (IOException e) {
					}
				}
				writer.close();
			}
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			new HelpFormatter().printHelp("agbreder", options);
		}
		// System.out.println(new String(output.toByteArray()));
		System.out.println(output.size());
	}
}
