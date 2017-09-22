package com.agbreder.compiler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.agbreder.compiler.grammer.Grammer;
import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.node.RNode;
import com.agbreder.compiler.node.command.RMethod;
import com.agbreder.compiler.util.ByteFixOutputStream;
import com.agbreder.compiler.util.CountOutputStream;

/**
 * Compilador
 * 
 * 
 * @author Bernardo Breder
 */
public class AGBCompiler {

  /** Diretório de saida */
  private File outputDir = new File(".");
  /** Fontes compiladas */
  private List<RNode> sourceFiles = new ArrayList<RNode>();
  /** Contexto */
  private ContextNode context = new ContextNode();
  /** Flag que indica para apenas compilar e não licar */
  private boolean justCompile = false;

  /**
   * Construtor
   */
  public AGBCompiler() {
  }

  /**
   * Compila de forma fácil
   * 
   * @param output
   * @param sources
   * @throws Exception
   */
  public static void compile(OutputStream output, List<InputStream> sources)
    throws Exception {
    AGBCompiler c = new AGBCompiler();
    for (InputStream input : sources) {
      c.addSource(input);
    }
    try {
      c.start(output);
    }
    finally {
      output.close();
    }
  }

  /**
   * Adiciona uma fonte
   * 
   * @param input
   * @throws Exception
   */
  public void addSource(InputStream input) throws Exception {
    this.sourceFiles.add(new Grammer(input).init());
  }

  /**
   * Inicia a compilação
   * 
   * @param output
   * @throws Exception
   */
  public void start(OutputStream output) throws Exception {
    for (RNode node : this.sourceFiles) {
      node.head(context);
    }
    for (RNode node : this.sourceFiles) {
      node.compile(context);
    }
    if (this.isJustCompile()) {
    }
    else {
      for (RNode node : this.sourceFiles) {
        node.link(context);
      }
      CountOutputStream outputTest = new CountOutputStream();
      build(outputTest);
      ByteFixOutputStream outputBinary =
        new ByteFixOutputStream(outputTest.getLength());
      build(outputBinary);
      output.write(outputBinary.getBytes());
    }
  }

  /**
   * Inicia a construção
   * 
   * @param output
   * @throws IOException
   * @throws Exception
   */
  private void build(OutputStream output) throws Exception {
    AGBCompilerOpcode opcodes = new AGBCompilerOpcode(output);
    opcodes.opDefNumInit(this.context.getNumPool().size());
    opcodes.opDefStrInit(this.context.getStrPool().size());
    for (Double value : this.context.getNumPool()) {
      opcodes.opDefNum(value);
    }
    for (String value : this.context.getStrPool()) {
      opcodes.opDefStr(value);
    }
    opcodes.decCount(1).opStackPush(1);
    for (RNode node : this.sourceFiles) {
      node.build(opcodes);
      opcodes.resetCount();
    }
    opcodes.opVmHalf();
    for (RMethod method : context.getMethods()) {
      method.build(opcodes);
    }
  }

  /**
   * Retorna
   * 
   * @return outputDir
   */
  public File getOutputDir() {
    return outputDir;
  }

  /**
   * @param outputDir
   */
  public void setOutputDir(File outputDir) {
    this.outputDir = outputDir;
  }

  /**
   * Retorna
   * 
   * @return justCompile
   */
  public boolean isJustCompile() {
    return justCompile;
  }

  /**
   * @param justCompile
   */
  public void setJustCompile(boolean justCompile) {
    this.justCompile = justCompile;
  }

}
