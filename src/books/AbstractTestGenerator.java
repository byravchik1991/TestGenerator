package books;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Иришка
 * Date: 16.04.13
 */
public abstract class AbstractTestGenerator implements TestGenerator {
    //количество вопросов в тесте
    protected int questionCount;

    //количество ответов в каждом вопросе
    protected int[] sizes;

    //сгенерированные тесты
    protected List<Test> acceptedTests;

    protected AbstractTestGenerator(int[] sizes) {
        this.questionCount = sizes.length;
        this.sizes = sizes;
        this.acceptedTests = new ArrayList<Test>();
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public int[] getSizes() {
        return sizes;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public void setSizes(int[] sizes) {
        this.sizes = sizes;
    }

    @Override
    public List<Test> generateTests(int testCount) {
        Logger.getLogger(this.getClass().getName()).info("Selection of " + testCount + " tests");

        for (int i = 0; i < testCount; i++) {
            acceptedTests.add(generateNextTest());
        }

        return acceptedTests;
    }

    //находит следующий наиболее выгодный тест
    protected abstract Test findNextBestTest();

    //готовит генератов тестов к выбору следующего лучшего теста
    protected abstract void updateTestGeneratorByNewTest(Test test);

    public Test generateNextTest() {
        Test test = findNextBestTest();
        updateTestGeneratorByNewTest(test);

        return test;
    }
}
