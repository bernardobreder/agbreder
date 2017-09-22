package com.agbreder.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Test;

import com.agbreder.compiler.disassembler.AGBDesassembler;
import com.agbreder.compiler.disassembler.AGBInstruction;
import com.agbreder.compiler.exception.BytecodeDesassemblerException;
import com.agbreder.compiler.exception.DesassemblerException;
import com.agbreder.compiler.exception.HeaderDesassemblerException;
import com.agbreder.compiler.util.Base64;

/**
 * Testa a especificação do Desassembler
 * 
 * @author bernardobreder
 */
public class DesassemblerTest extends BasicTest {

  /**
   * Testa
   * 
   * @throws Exception
   */
  @Test
  public void test() throws Exception {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    DataOutputStream output = new DataOutputStream(bytes);
    output.writeByte(0xAA);
    try {
      ex(new ByteArrayInputStream(bytes.toByteArray()));
      Assert.fail();
    }
    catch (HeaderDesassemblerException e) {
    }
  }

  /**
   * Testa
   * 
   * @throws Exception
   */
  @Test
  public void test2() throws Exception {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    DataOutputStream output = new DataOutputStream(bytes);
    // Magic Symbol
    output.writeByte(0xBB);
    // Numbers
    output.writeInt(0);
    // Strings
    output.writeInt(0);
    // Classes
    output.writeInt(0);
    // Methods
    output.writeInt(0);
    // Finish Header
    output.writeByte(0xAA);
    // Opcodes
    output.writeInt(0);
    // Finish Body
    output.writeByte(0xFF);
    try {
      ex(new ByteArrayInputStream(bytes.toByteArray()));
      Assert.fail();
    }
    catch (HeaderDesassemblerException e) {
    }
  }

  /**
   * Testa
   * 
   * @throws Exception
   */
  @Test
  public void test3() throws Exception {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    DataOutputStream output = new DataOutputStream(bytes);
    // Magic Symbol
    output.writeByte(0xBB);
    // Numbers
    output.writeInt(0);
    // Strings
    output.writeInt(0);
    // Classes
    output.writeInt(0);
    // Methods
    output.writeInt(0);
    // Finish Header
    output.writeByte(0xFF);
    // Opcodes
    output.writeInt(0);
    // Finish Body
    output.writeByte(0xAA);
    try {
      ex(new ByteArrayInputStream(bytes.toByteArray()));
      Assert.fail();
    }
    catch (HeaderDesassemblerException e) {
    }
  }

  /**
   * Testa
   * 
   * @throws Exception
   */
  @Test
  public void testBdk() throws Exception {
    ex(new FileInputStream("../agbreder_bdk/bin/binary.agbc"));
  }

  /**
   * Testa
   * 
   * @throws Exception
   */
  @Test
  public void testBase64Decode() throws Exception {
    FileInputStream input =
      new FileInputStream("../agbreder_bdk/bin/binary.txt");
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    for (int n; ((n = input.read()) != -1);) {
      output.write((char) n);
    }
    Base64.decode(new String(output.toByteArray()));
  }

  /**
   * @param input
   * @throws IOException
   * @throws DesassemblerException
   * @throws BytecodeDesassemblerException
   */
  private void ex(InputStream input) throws IOException, DesassemblerException,
    BytecodeDesassemblerException {
    AGBDesassembler desassembler = new AGBDesassembler(input);
    desassembler.readHeader();
    desassembler.readBody();
    for (AGBInstruction inst : desassembler.getBytecodes()) {
      inst.toString();
    }
  }

}
