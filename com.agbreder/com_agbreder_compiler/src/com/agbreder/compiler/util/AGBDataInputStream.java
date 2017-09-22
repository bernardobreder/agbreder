package com.agbreder.compiler.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.agbreder.compiler.node.RStruct;
import com.agbreder.compiler.node.command.RBlock;
import com.agbreder.compiler.node.command.RExpression;
import com.agbreder.compiler.node.command.RFor;
import com.agbreder.compiler.node.command.RForN;
import com.agbreder.compiler.node.command.RIf;
import com.agbreder.compiler.node.command.RMethod;
import com.agbreder.compiler.node.command.RNative;
import com.agbreder.compiler.node.command.RRepeat;
import com.agbreder.compiler.node.command.RSwitch;
import com.agbreder.compiler.node.command.RWhile;
import com.agbreder.compiler.node.expression.lvalue.LIdentify;
import com.agbreder.compiler.node.expression.rvalue.RIdentify;
import com.agbreder.compiler.node.expression.rvalue.array.RArrayGet;
import com.agbreder.compiler.node.expression.rvalue.array.RArrayLength;
import com.agbreder.compiler.node.expression.rvalue.array.RArrayNew;
import com.agbreder.compiler.node.expression.rvalue.array.RArraySet;
import com.agbreder.compiler.node.expression.rvalue.binary.RAnd;
import com.agbreder.compiler.node.expression.rvalue.binary.RAssign;
import com.agbreder.compiler.node.expression.rvalue.binary.RDeclare;
import com.agbreder.compiler.node.expression.rvalue.binary.RDiv;
import com.agbreder.compiler.node.expression.rvalue.binary.REqual;
import com.agbreder.compiler.node.expression.rvalue.binary.REqualGreater;
import com.agbreder.compiler.node.expression.rvalue.binary.REqualLower;
import com.agbreder.compiler.node.expression.rvalue.binary.RGreater;
import com.agbreder.compiler.node.expression.rvalue.binary.RLower;
import com.agbreder.compiler.node.expression.rvalue.binary.RMul;
import com.agbreder.compiler.node.expression.rvalue.binary.RNotEqual;
import com.agbreder.compiler.node.expression.rvalue.binary.ROr;
import com.agbreder.compiler.node.expression.rvalue.binary.RSub;
import com.agbreder.compiler.node.expression.rvalue.binary.RSum;
import com.agbreder.compiler.node.expression.rvalue.primitive.RArray;
import com.agbreder.compiler.node.expression.rvalue.primitive.RBoolean;
import com.agbreder.compiler.node.expression.rvalue.primitive.RNull;
import com.agbreder.compiler.node.expression.rvalue.primitive.RNumber;
import com.agbreder.compiler.node.expression.rvalue.primitive.RString;
import com.agbreder.compiler.node.expression.rvalue.ternary.RTernary;
import com.agbreder.compiler.node.expression.rvalue.unary.RCall;
import com.agbreder.compiler.node.expression.rvalue.unary.RCast;
import com.agbreder.compiler.node.expression.rvalue.unary.RLeftCall;
import com.agbreder.compiler.node.expression.rvalue.unary.RPosDec;
import com.agbreder.compiler.node.expression.rvalue.unary.RPosInc;
import com.agbreder.compiler.node.expression.rvalue.unary.RPreDec;
import com.agbreder.compiler.node.expression.rvalue.unary.RPreInc;

/**
 * Saída para objetos
 * 
 * 
 * @author Bernardo Breder
 */
public class AGBDataInputStream extends DataInputStream {

  /** Código de Serialização */
  public static final char METHOD_ID = 1;
  /** Código de Serialização */
  public static final char BLOCK_ID = 2;
  /** Código de Serialização */
  public static final char EXPRESSION_ID = 3;
  /** Código de Serialização */
  public static final char FOR_ID = 4;
  /** Código de Serialização */
  public static final char FOR_N_ID = 5;
  /** Código de Serialização */
  public static final char IF_ID = 6;
  /** Código de Serialização */
  public static final char NATIVE_ID = 7;
  /** Código de Serialização */
  public static final char REPEAT_ID = 8;
  /** Código de Serialização */
  public static final char SWITCH_ID = 9;
  /** Código de Serialização */
  public static final char WHILE_ID = 10;
  /** Código de Serialização */
  public static final char ASSIGN_ID = 11;
  /** Código de Serialização */
  public static final char DECLARE_ID = 12;
  /** Código de Serialização */
  public static final char LIDENTIFY_ID = 13;
  /** Código de Serialização */
  public static final char RIDENTIFY_ID = 14;
  /** Código de Serialização */
  public static final char AND_ID = 15;
  /** Código de Serialização */
  public static final char OR_ID = 16;
  /** Código de Serialização */
  public static final char EQUAL_ID = 17;
  /** Código de Serialização */
  public static final char NOT_EQUAL_ID = 18;
  /** Código de Serialização */
  public static final char EQUAL_GREATER_ID = 19;
  /** Código de Serialização */
  public static final char EQUAL_LOWER_ID = 20;
  /** Código de Serialização */
  public static final char GREATER_ID = 21;
  /** Código de Serialização */
  public static final char LOWER_ID = 22;
  /** Código de Serialização */
  public static final char SUM_ID = 23;
  /** Código de Serialização */
  public static final char SUB_ID = 24;
  /** Código de Serialização */
  public static final char MUL_ID = 25;
  /** Código de Serialização */
  public static final char DIV_ID = 26;
  /** Código de Serialização */
  public static final char ARRAY_GET_ID = 27;
  /** Código de Serialização */
  public static final char ARRAY_LENGTH_ID = 28;
  /** Código de Serialização */
  public static final char ARRAY_NEW_ID = 29;
  /** Código de Serialização */
  public static final char ARRAY_SET_ID = 30;
  /** Código de Serialização */
  public static final char ARRAY_ID = 31;
  /** Código de Serialização */
  public static final char BOOLEAN_ID = 32;
  /** Código de Serialização */
  public static final char NULL_ID = 33;
  /** Código de Serialização */
  public static final char NUMBER_ID = 34;
  /** Código de Serialização */
  public static final char STRING_ID = 35;
  /** Código de Serialização */
  public static final char TERNARY_ID = 36;
  /** Código de Serialização */
  public static final char CALL_ID = 37;
  /** Código de Serialização */
  public static final char CAST_ID = 38;
  /** Código de Serialização */
  public static final char LEFT_CALL_ID = 39;
  /** Código de Serialização */
  public static final char POS_DEC_ID = 40;
  /** Código de Serialização */
  public static final char POS_INC_ID = 41;
  /** Código de Serialização */
  public static final char PRE_DEC_ID = 42;
  /** Código de Serialização */
  public static final char PRE_INC_ID = 43;
  /** Código de Serialização */
  public static final char TOKEN_ID = 44;

  /**
   * Construtor
   * 
   * @param in
   */
  public AGBDataInputStream(InputStream in) {
    super(in);
  }

  /**
   * Cria um objeto
   * 
   * @param input
   * @return objeto
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  public <E extends RStruct> E create(AGBDataInputStream input)
    throws IOException {
    char type = input.readChar();
    switch (type) {
      case METHOD_ID:
        return (E) new RMethod(input);
      case BLOCK_ID:
        return (E) new RBlock(input);
      case EXPRESSION_ID:
        return (E) new RExpression(input);
      case FOR_ID:
        return (E) new RFor(input);
      case FOR_N_ID:
        return (E) new RForN(input);
      case IF_ID:
        return (E) new RIf(input);
      case NATIVE_ID:
        return (E) new RNative(input);
      case REPEAT_ID:
        return (E) new RRepeat(input);
      case SWITCH_ID:
        return (E) new RSwitch(input);
      case WHILE_ID:
        return (E) new RWhile(input);
      case ASSIGN_ID:
        return (E) new RAssign(input);
      case DECLARE_ID:
        return (E) new RDeclare(input);
      case LIDENTIFY_ID:
        return (E) new LIdentify(input);
      case RIDENTIFY_ID:
        return (E) new RIdentify(input);
      case AND_ID:
        return (E) new RAnd(input);
      case OR_ID:
        return (E) new ROr(input);
      case EQUAL_ID:
        return (E) new REqual(input);
      case NOT_EQUAL_ID:
        return (E) new RNotEqual(input);
      case EQUAL_GREATER_ID:
        return (E) new REqualGreater(input);
      case EQUAL_LOWER_ID:
        return (E) new REqualLower(input);
      case GREATER_ID:
        return (E) new RGreater(input);
      case LOWER_ID:
        return (E) new RLower(input);
      case SUM_ID:
        return (E) new RSum(input);
      case SUB_ID:
        return (E) new RSub(input);
      case MUL_ID:
        return (E) new RMul(input);
      case DIV_ID:
        return (E) new RDiv(input);
      case ARRAY_GET_ID:
        return (E) new RArrayGet(input);
      case ARRAY_LENGTH_ID:
        return (E) new RArrayLength(input);
      case ARRAY_NEW_ID:
        return (E) new RArrayNew(input);
      case ARRAY_SET_ID:
        return (E) new RArraySet(input);
      case ARRAY_ID:
        return (E) new RArray(input);
      case BOOLEAN_ID:
        return (E) new RBoolean(input);
      case NULL_ID:
        return (E) new RNull(input);
      case NUMBER_ID:
        return (E) new RNumber(input);
      case STRING_ID:
        return (E) new RString(input);
      case TERNARY_ID:
        return (E) new RTernary(input);
      case CALL_ID:
        return (E) new RCall(input);
      case CAST_ID:
        return (E) new RCast(input);
      case LEFT_CALL_ID:
        return (E) new RLeftCall(input);
      case POS_DEC_ID:
        return (E) new RPosDec(input);
      case POS_INC_ID:
        return (E) new RPosInc(input);
      case PRE_DEC_ID:
        return (E) new RPreDec(input);
      case PRE_INC_ID:
        return (E) new RPreInc(input);
    }
    throw new IllegalArgumentException();
  }

}
