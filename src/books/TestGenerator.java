package books;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Иришка
 * Date: 14.04.13
 */
public interface TestGenerator {
    Set<Test> generateTests(int testCount);
}
