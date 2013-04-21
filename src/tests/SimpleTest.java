package tests;

import books.AbstractTestGenerator;
import books.RandomTestGenerator;
import books.SimpleTestGenerator;
import books.SmartTestGenerator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Иришка
 * Date: 12.04.13
 */
public class SimpleTest {

    @Test
    public void testOneMistakeSimple() {
        SimpleTestGenerator testGenerator = createSimpleTestGenerator();

        List<books.Test> tests = testGenerator.generateTests(10);

        testSingleAnswers(testGenerator, tests);
    }

    private void testSingleAnswers(AbstractTestGenerator testGenerator, List<books.Test> tests) {
/*        for (books.Test test : tests) {
            System.out.println(Arrays.toString(test.getAnswers()));
        }*/

        List[] answers = new List[testGenerator.getQuestionCount()];
        for (int i = 0; i < testGenerator.getQuestionCount(); i++) {
            answers[i] = new ArrayList(testGenerator.getSizes()[i]);

            for (int j = 0; j < testGenerator.getSizes()[i]; j++) {
                answers[i].add(j, j);
            }
        }

        for (books.Test test : tests) {
            for (int i = 0; i < test.getQuestionsCount(); i++) {
                if (answers[i].contains(test.getAnswer(i))) {
                    answers[i].remove(answers[i].indexOf(test.getAnswer(i)));
                }
            }
        }

        boolean empty = true;

        for (List answer : answers) {
            empty &= answer.isEmpty();
        }

        assertTrue(empty);
    }

    @Test
    public void testOneMistakeSmart() throws CloneNotSupportedException {
        SmartTestGenerator testGenerator = createSmartTestGenerator();

        List<books.Test> tests = testGenerator.generateTests(5);

        testSingleAnswers(testGenerator, tests);
    }

    @Test
    public void testOneMistakeRandom() throws CloneNotSupportedException {
        RandomTestGenerator testGenerator = createRandomTestGenerator();

        List<books.Test> tests = testGenerator.generateTests(25);

/*        for (books.Test test : tests) {
            System.out.println(test);
        }*/

        testSingleAnswers(testGenerator, tests);
    }

    @Test
    public void testTwoMistakesSmart() throws CloneNotSupportedException {
        SmartTestGenerator testGenerator = createSmartTestGenerator();
        List<books.Test> tests = testGenerator.generateTests(20);

/*        for (books.Test test : tests) {
            System.out.println(test);
        }*/

        testTwoMistakes(testGenerator, tests);
    }

    @Test
    public void testTwoMistakesSimple() throws CloneNotSupportedException {
        SimpleTestGenerator testGenerator = createSimpleTestGenerator();
        List<books.Test> tests = testGenerator.generateTests(100);

/*        for (books.Test test : tests) {
            System.out.println(test);
        }*/

        testTwoMistakes(testGenerator, tests);
    }

    @Test
    public void testTwoMistakesRandom() throws CloneNotSupportedException {
        RandomTestGenerator testGenerator = createRandomTestGenerator();
        List<books.Test> tests = testGenerator.generateTests(100);

/*        for (books.Test test : tests) {
            System.out.println(test);
        }*/

        testTwoMistakes(testGenerator, tests);
    }

    private void testTwoMistakes(AbstractTestGenerator testGenerator2, List<books.Test> tests) {
        boolean everythingOk = true;
        for (int i = 0; i < testGenerator2.getSizes()[0]; i++) {

            for (int j = 0; j < testGenerator2.getSizes()[1]; j++) {
                boolean ok = false;
                for (books.Test test : tests) {
                    if ((test.getAnswer(0) == i) && (test.getAnswer(1) == j)) {
                        ok = true;
                        break;
                    }
                }

                everythingOk &= ok;
            }

            for (int j = 0; j < testGenerator2.getSizes()[2]; j++) {
                boolean ok = false;
                for (books.Test test : tests) {
                    if ((test.getAnswer(0) == i) && (test.getAnswer(2) == j)) {
                        ok = true;
                        break;
                    }
                }

                everythingOk &= ok;
            }

            for (int j = 0; j < testGenerator2.getSizes()[3]; j++) {
                boolean ok = false;
                for (books.Test test : tests) {
                    if ((test.getAnswer(0) == i) && (test.getAnswer(3) == j)) {
                        ok = true;
                        break;
                    }
                }

                everythingOk &= ok;
            }

        }

        for (int i = 0; i < testGenerator2.getSizes()[1]; i++) {

            for (int j = 0; j < testGenerator2.getSizes()[2]; j++) {
                boolean ok = false;
                for (books.Test test : tests) {
                    if ((test.getAnswer(1) == i) && (test.getAnswer(2) == j)) {
                        ok = true;
                        break;
                    }
                }

                everythingOk &= ok;
            }

            for (int j = 0; j < testGenerator2.getSizes()[3]; j++) {
                boolean ok = false;
                for (books.Test test : tests) {
                    if ((test.getAnswer(1) == i) && (test.getAnswer(3) == j)) {
                        ok = true;
                        break;
                    }
                }

                everythingOk &= ok;
            }
        }

        for (int i = 0; i < testGenerator2.getSizes()[2]; i++) {

            for (int j = 0; j < testGenerator2.getSizes()[3]; j++) {
                boolean ok = false;
                for (books.Test test : tests) {
                    if ((test.getAnswer(2) == i) && (test.getAnswer(3) == j)) {
                        ok = true;
                        break;
                    }
                }

                everythingOk &= ok;
            }
        }

        assertTrue(everythingOk);
    }

    private SimpleTestGenerator createSimpleTestGenerator() {
        int[] sizes = createSizesArray();

        return new SimpleTestGenerator(sizes);
    }

    private SmartTestGenerator createSmartTestGenerator() throws CloneNotSupportedException {
        int[] sizes = createSizesArray();

        return new SmartTestGenerator(sizes);
    }

    private int[] createSizesArray() {
        int n = 4;
        int[] sizes = new int[n];
        sizes[0] = 4;
        sizes[1] = 2;
        sizes[2] = 3;
        sizes[3] = 5;
        return sizes;
    }

    private RandomTestGenerator createRandomTestGenerator() throws CloneNotSupportedException {
        int[] sizes = createSizesArray();

        return new RandomTestGenerator(sizes);
    }
}
