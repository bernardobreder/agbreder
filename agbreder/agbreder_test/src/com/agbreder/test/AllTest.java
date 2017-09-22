package com.agbreder.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Testador de tudo
 * 
 * @author Bernardo Breder
 */
@RunWith(Suite.class)
@SuiteClasses({ LightArrayListTest.class, TokenTest.class, ParserTest.class,
		DesassemblerTest.class, PrimitiveTest.class, ClassTest.class,
		ThrowClassTest.class, ObjectTest.class, StringTest.class, ArrayTest.class,
		BDKTest.class })
public class AllTest {
	
}
