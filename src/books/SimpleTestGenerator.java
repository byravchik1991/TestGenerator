package books;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Иришка
 * Date: 12.04.13
 */
public class SimpleTestGenerator extends AbstractTestGenerator {
    //содержит информацию о том, сколько раз был выбран каждый вариант ответа в каждом вопросе
    private int[][] answersMap;

    private Set<Test> acceptedTests = new HashSet<Test>();

    private Random random = new Random();

    public SimpleTestGenerator(int[] sizes) {
        super(sizes);

        initializeAnswerMap(sizes);
    }

    private void initializeAnswerMap(int[] sizes) {
        answersMap = new int[questionCount][];

        for (int i = 0; i < questionCount; i++) {
            answersMap[i] = new int[sizes[i]];
        }
    }

    @Override
    protected Test findNextBestTest() {
        Test test = new Test(questionCount);
        fillTestWithAnswers(test);

        while (acceptedTests.contains(test)) {
            test = new Test(questionCount);
            fillTestWithAnswers(test);
        }

        return test;
    }

    private void fillTestWithAnswers(Test test) {
        for (int j = 0; j < questionCount; j++) {
            if (random.nextInt(2) == 0) {
                test.setAnswer(findIndexOfMinValue(answersMap[j]), j);
            } else {
                test.setAnswer(random.nextInt(sizes[j]), j);
            }
        }
    }

    @Override
    protected void updateTestGeneratorByNewTest(Test test) {
        for (int i = 0; i < test.getQuestionsCount(); i++) {
            answersMap[i][test.getAnswer(i)] = answersMap[i][test.getAnswer(i)] + 1;
        }

        acceptedTests.add(test);
    }

    private int findIndexOfMinValue(int[] array) {
        int minAnswerNumber = 0;
        int min = array[minAnswerNumber];

        for (int i = 0; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
                minAnswerNumber = i;
            }
        }

        return minAnswerNumber;
    }
}
