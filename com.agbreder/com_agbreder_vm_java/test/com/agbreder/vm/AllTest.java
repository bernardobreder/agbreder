package com.agbreder.vm;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Testador da Máquina virtual
 * 
 * 
 * @author Bernardo Breder
 */
@RunWith(Suite.class)
@SuiteClasses({ RootLangOpcodeTest.class, RootLangVmTest.class })
public class AllTest {

}
