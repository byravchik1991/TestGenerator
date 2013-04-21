package books;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Иришка
 * Date: 14.04.13
 */
public interface TestGenerator {
    //генерирует testCount подходящих тестов
    List<Test> generateTests(int testCount);

    //генерирует следующий подходящий тест
    Test generateNextTest();
}
