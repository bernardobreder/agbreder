import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Testador
 * 
 * 
 * @author Bernardo Breder
 */
@RunWith(Suite.class)
@SuiteClasses({ CompressTest.class, DecompressTest.class, CharsetTest.class,
    LexicalTest.class, SyntaxTest.class ,CompileTest.class})
public class AllTest {

}
