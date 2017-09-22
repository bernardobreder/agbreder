package com.bp2pb.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;

import org.junit.Assert;
import org.junit.Test;

import com.bp2pb.server.database.App;
import com.bp2pb.server.database.SqlLiteConnection;
import com.bp2pb.server.database.SqlLiteConnectionTest;

/**
 * Testador de classe
 * 
 * @author bernardobreder
 * 
 */
public class BP2PBProcessorTest {

	@Test
	public void testDeploy() throws Exception {
		SqlLiteConnection db = new SqlLiteConnectionTest();
		BP2PBProcessor process = new BP2PBProcessor(db);
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			process.execute("get test", bytes);
			DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes.toByteArray()));
			Assert.assertEquals(0, input.readByte());
		}
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			process.execute(String.format("deploy test %s %s", Base64.encode(new byte[1]), Base64.encode(new byte[2])), bytes);
			DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes.toByteArray()));
			Assert.assertEquals(1, input.readInt());
			Assert.assertEquals(-1, input.read());
		}
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			process.execute("get test", bytes);
			DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes.toByteArray()));
			Assert.assertEquals(1, input.readByte());
		}
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			process.execute(String.format("deploy test %s %s", Base64.encode(new byte[1]), Base64.encode(new byte[2])), bytes);
			DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes.toByteArray()));
			Assert.assertEquals(2, input.readInt());
			Assert.assertEquals(-1, input.read());
		}
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			process.execute("get test", bytes);
			DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes.toByteArray()));
			Assert.assertEquals(1, input.readByte());
			Assert.assertEquals(2, input.readInt());
		}
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			process.execute(String.format("deploy test %s %s", Base64.encode(new byte[1]), Base64.encode(new byte[2])), bytes);
			DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes.toByteArray()));
			Assert.assertEquals(3, input.readInt());
			Assert.assertEquals(-1, input.read());
		}
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			process.execute("get test", bytes);
			DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes.toByteArray()));
			Assert.assertEquals(1, input.readByte());
			Assert.assertEquals(3, input.readInt());
		}
	}

	@Test
	public void testGet() throws Exception {
		SqlLiteConnectionTest db = new SqlLiteConnectionTest();
		db.insertApp(new App(1, "test", 1, new byte[1], new byte[2], new byte[3]));
		BP2PBProcessor process = new BP2PBProcessor(db);
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			process.execute("get empty", bytes);
			DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes.toByteArray()));
			Assert.assertEquals(0, input.readByte());
			Assert.assertEquals(-1, input.read());
		}
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			process.execute("get test", bytes);
			DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes.toByteArray()));
			Assert.assertEquals(1, input.readByte());
			Assert.assertEquals(1, input.readInt());
			Assert.assertArrayEquals(new byte[1], Base64.decode(input.readUTF()));
			Assert.assertArrayEquals(new byte[2], Base64.decode(input.readUTF()));
			Assert.assertArrayEquals(new byte[3], Base64.decode(input.readUTF()));
			Assert.assertEquals(-1, input.read());
		}
	}
	
	@Test
	public void testUpdate() throws Exception {
		SqlLiteConnectionTest db = new SqlLiteConnectionTest();
		db.insertApp(new App(1, "test", 1, new byte[1], new byte[2], new byte[3]));
		BP2PBProcessor process = new BP2PBProcessor(db);
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			process.execute("update test 0", bytes);
			DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes.toByteArray()));
			Assert.assertEquals(1, input.readByte());
			Assert.assertEquals(1, input.readInt());
			Assert.assertArrayEquals(new byte[1], Base64.decode(input.readUTF()));
			Assert.assertArrayEquals(new byte[2], Base64.decode(input.readUTF()));
			Assert.assertArrayEquals(new byte[3], Base64.decode(input.readUTF()));
			Assert.assertEquals(-1, input.read());
		}
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			process.execute("update test 1", bytes);
			DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes.toByteArray()));
			Assert.assertEquals(0, input.readByte());
			Assert.assertEquals(-1, input.read());
		}
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			process.execute("update test 10", bytes);
			DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes.toByteArray()));
			Assert.assertEquals(0, input.readByte());
			Assert.assertEquals(-1, input.read());
		}
	}
	
	@Test
	public void testMixture() throws Exception {
		SqlLiteConnectionTest db = new SqlLiteConnectionTest();
		BP2PBProcessor process = new BP2PBProcessor(db);
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			process.execute("get test", bytes);
			DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes.toByteArray()));
			Assert.assertEquals(0, input.readByte());
		}
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			process.execute(String.format("deploy test %s %s", Base64.encode(new byte[1]), Base64.encode(new byte[2])), bytes);
			DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes.toByteArray()));
			Assert.assertEquals(1, input.readInt());
			Assert.assertEquals(-1, input.read());
		}
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			process.execute("get test", bytes);
			DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes.toByteArray()));
			Assert.assertEquals(1, input.readByte());
			Assert.assertEquals(1, input.readInt());
		}
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			process.execute("update test 0", bytes);
			DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes.toByteArray()));
			Assert.assertEquals(1, input.readByte());
			Assert.assertEquals(1, input.readInt());
			Assert.assertArrayEquals(new byte[1], Base64.decode(input.readUTF()));
			Assert.assertArrayEquals(new byte[2], Base64.decode(input.readUTF()));
			Assert.assertArrayEquals(new byte[0], Base64.decode(input.readUTF()));
			Assert.assertEquals(-1, input.read());
		}
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			process.execute("update test 1", bytes);
			DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes.toByteArray()));
			Assert.assertEquals(0, input.readByte());
			Assert.assertEquals(-1, input.read());
		}
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			process.execute(String.format("deploy test %s %s", Base64.encode(new byte[1]), Base64.encode(new byte[2])), bytes);
			DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes.toByteArray()));
			Assert.assertEquals(2, input.readInt());
			Assert.assertEquals(-1, input.read());
		}
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			process.execute("update test 0", bytes);
			DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes.toByteArray()));
			Assert.assertEquals(1, input.readByte());
			Assert.assertEquals(2, input.readInt());
			Assert.assertArrayEquals(new byte[1], Base64.decode(input.readUTF()));
			Assert.assertArrayEquals(new byte[2], Base64.decode(input.readUTF()));
			Assert.assertArrayEquals(new byte[0], Base64.decode(input.readUTF()));
			Assert.assertEquals(-1, input.read());
		}
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			process.execute("update test 1", bytes);
			DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes.toByteArray()));
			Assert.assertEquals(1, input.readByte());
			Assert.assertEquals(2, input.readInt());
			Assert.assertArrayEquals(new byte[1], Base64.decode(input.readUTF()));
			Assert.assertArrayEquals(new byte[2], Base64.decode(input.readUTF()));
			Assert.assertArrayEquals(new byte[0], Base64.decode(input.readUTF()));
			Assert.assertEquals(-1, input.read());
		}
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			process.execute("update test 2", bytes);
			DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes.toByteArray()));
			Assert.assertEquals(0, input.readByte());
			Assert.assertEquals(-1, input.read());
		}
	}

}
