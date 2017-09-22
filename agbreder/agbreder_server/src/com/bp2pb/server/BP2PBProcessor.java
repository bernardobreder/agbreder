package com.bp2pb.server;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.StringTokenizer;

import com.bp2pb.server.database.App;
import com.bp2pb.server.database.SqlLiteConnection;

/**
 * Processador de uma requisição
 * 
 * @author bernardobreder
 * 
 */
public class BP2PBProcessor {

	/** Sql */
	private final SqlLiteConnection conn;

	/**
	 * Construtor
	 * 
	 * @param conn
	 */
	public BP2PBProcessor(SqlLiteConnection conn) {
		super();
		this.conn = conn;
	}

	public void execute(String request, OutputStream stream) throws SQLException, IOException {
		DataOutputStream dataOutput = new DataOutputStream(stream);
		// StringTokenizer tokens = new StringTokenizer(request, " ");
		// this.execute(tokens, dataOutput);
		File file = new File("bin/Main.class");
		dataOutput.writeInt((int) file.length());
		FileInputStream input = new FileInputStream(file);
		for (int n; ((n = input.read()) != -1);) {
			dataOutput.write(n);
		}
		dataOutput.flush();
	}

	/**
	 * Executa uma requisição
	 * 
	 * @param tokens
	 * @param output
	 * @throws SQLException
	 * @throws IOException
	 */
	private void execute(StringTokenizer tokens, DataOutputStream output) throws SQLException, IOException {
		if (!tokens.hasMoreTokens()) {
			output.writeUTF("command not found");
		} else {
			String token = tokens.nextToken();
			if (token.equals("get")) {
				this.executeGet(tokens, output);
			} else if (token.equals("update")) {
				this.executeUpdate(tokens, output);
			} else if (token.equals("deploy")) {
				this.executeDeploy(tokens, output);
			} else {
				output.writeUTF("command not found");
			}
		}
	}

	/**
	 * Recupera a aplicação
	 * 
	 * @param tokens
	 * @param output
	 * @throws SQLException
	 * @throws IOException
	 */
	private void executeGet(StringTokenizer tokens, DataOutputStream output) throws SQLException, IOException {
		String name = tokens.nextToken();
		App app = this.conn.getApp(name);
		if (app != null) {
			output.writeByte(1);
			output.writeInt(app.getRevision());
			output.writeUTF(Base64.encode(app.getIcon()));
			output.writeUTF(Base64.encode(app.getApp()));
			output.writeUTF(Base64.encode(app.getDatabase()));
		} else {
			output.writeByte(0);
		}
	}

	/**
	 * Atualiza uma aplicação
	 * 
	 * @param tokens
	 * @param output
	 * @throws SQLException
	 * @throws IOException
	 */
	private void executeUpdate(StringTokenizer tokens, DataOutputStream output) throws SQLException, IOException {
		String name = tokens.nextToken();
		int revision = new Integer(tokens.nextToken());
		App app = this.conn.getApp(name);
		if (revision >= app.getRevision()) {
			output.writeByte(0);
		} else {
			output.writeByte(1);
			output.writeInt(app.getRevision());
			output.writeUTF(Base64.encode(app.getIcon()));
			output.writeUTF(Base64.encode(app.getApp()));
			output.writeUTF(Base64.encode(app.getDatabase()));
		}
	}

	/**
	 * Publica uma aplicação existente ou não.
	 * 
	 * @param tokens
	 * @param output
	 * @throws SQLException
	 * @throws IOException
	 */
	private void executeDeploy(StringTokenizer tokens, DataOutputStream output) throws SQLException, IOException {
		String name = tokens.nextToken();
		byte[] icon = Base64.decode(tokens.nextToken());
		byte[] app = Base64.decode(tokens.nextToken());
		App oldApp = this.conn.getApp(name);
		int revision;
		if (oldApp != null) {
			revision = oldApp.getRevision() + 1;
			this.conn.updateApp(new App(-1, name, revision, icon, app, null));
		} else {
			revision = 1;
			this.conn.insertApp(new App(-1, name, revision, icon, app, new byte[0]));
		}
		output.writeInt(revision);
	}

}
