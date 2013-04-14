package tests;

import books.AnswerGroup;
import books.AnswerGroupSet;
import books.SimpleTestGenerator;
import books.SmartTestGenerator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Иришка
 * Date: 12.04.13
 */
public class SimpleTest {

    @Test
    public void testSingleAnswers() {
        SimpleTestGenerator testGenerator = createTestGenerator();

        books.Test[] tests = testGenerator.generateTestsForSingleAnswer();

/*        for (books.Test test : tests) {
            System.out.println(Arrays.toString(test.getAnswers()));
        }*/

        int testCount = 5;
        assertTrue(tests.length == testCount);

        List[] answers = new List[testGenerator.getQuestionCount()];
        for (int i = 0; i < testGenerator.getQuestionCount(); i++) {
            answers[i] = new ArrayList(testGenerator.getAnswersCount()[i]);

            for (int j = 0; j < testGenerator.getAnswersCount()[i]; j++) {
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
    public void testPossibleTests() throws CloneNotSupportedException {
        SmartTestGenerator testGenerator2 = createTestGenerator2();

/*        System.out.println(testGenerator2.getPossibleTests().size() + " " + testGenerator2.getPossibleTestsNumber());
        for (books.Test test : testGenerator2.getPossibleTests()) {
            System.out.println(test);
        }*/

        assertTrue(testGenerator2.getPossibleTests().size() == testGenerator2.getPossibleTestsNumber());
    }

    @Test
    public void testAnswerGroups() throws CloneNotSupportedException {
        SmartTestGenerator testGenerator2 = createTestGenerator2();

        books.Test test = testGenerator2.getPossibleTests().iterator().next();
        AnswerGroupSet answerGroupSet = test.getAnswerGroupSet();

        assertTrue(answerGroupSet.getAnswerGroups().size() == 15);
    }

    @Test
    public void testOneMistake() throws CloneNotSupportedException {
        SmartTestGenerator testGenerator2 = createTestGenerator2();

        Set<books.Test> tests = testGenerator2.generateTests(5);

/*        for (books.Test test : tests) {
            System.out.println(Arrays.toString(test.getAnswers()));
        }*/

        int testCount = 5;
        assertTrue(tests.size() == testCount);

        List[] answers = new List[testGenerator2.getQuestionCount()];
        for (int i = 0; i < testGenerator2.getQuestionCount(); i++) {
            answers[i] = new ArrayList(testGenerator2.getSizes()[i]);

            for (int j = 0; j < testGenerator2.getSizes()[i]; j++) {
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
    public void testTwoMistakes() throws CloneNotSupportedException {
        SmartTestGenerator testGenerator2 = createTestGenerator2();
        Set<books.Test> tests = testGenerator2.generateTests(20);

        List<AnswerGroup> answers = new ArrayList<AnswerGroup>(71);

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

    private SimpleTestGenerator createTestGenerator() {
        int n = 4;
        int[] sizes = new int[n];
        sizes[0] = 4;
        sizes[1] = 2;
        sizes[2] = 3;
        sizes[3] = 5;

        return new SimpleTestGenerator(n, sizes, 0);
    }

    private SmartTestGenerator createTestGenerator2() throws CloneNotSupportedException {
        int n = 4;
        int[] sizes = new int[n];
        sizes[0] = 4;
        sizes[1] = 2;
        sizes[2] = 3;
        sizes[3] = 5;

        return new SmartTestGenerator(sizes);
    }
}
