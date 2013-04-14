package books;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Иришка
 * Date: 13.04.13
 */
public class SmartTestGenerator implements TestGenerator {
    private int questionCount;
    private int[] sizes;

    private Set<Test> possibleTests;
    private Set<Test> acceptedTests;

    private Map<Integer, Double> probabilityMap;

    public SmartTestGenerator(int[] sizes) throws CloneNotSupportedException {
        this.questionCount = sizes.length;
        this.sizes = sizes;

        initializeProbabilityMap();

        generatePossibleTests();
    }

    private void initializeProbabilityMap() {
        probabilityMap = new HashMap<Integer, Double>(questionCount);

        for (int i = 0; i < sizes.length; i++) {
            double probability = 1.0 / sizes[i];
            probabilityMap.put(i, probability);
        }
    }

    private void generatePossibleTests() throws CloneNotSupportedException {
        possibleTests = new HashSet<Test>(getPossibleTestsNumber());

        Test test = new Test(questionCount);
        possibleTests.add(test);
        generateNewTestsRecursive(test, 0);

        for (Test currentTest : possibleTests) {
            initializeAnswerGroupSet(currentTest);
        }
    }

    private void initializeAnswerGroupSet(Test test) throws CloneNotSupportedException {
        AnswerGroupSet answerGroupSet = new AnswerGroupSet();

        for (int i = 0; i < test.getQuestionsCount(); i++) {
            generateAnswerGroupsRecursive(test, new AnswerGroup(), 1, answerGroupSet);
        }

        test.setAnswerGroupSet(answerGroupSet);
    }

    private void generateAnswerGroupsRecursive(
            Test test,
            AnswerGroup parentAnswerGroup,
            int elementsCount,
            AnswerGroupSet answerGroupSet) throws CloneNotSupportedException {

        if (elementsCount > questionCount) {
            return;
        }

        for (int i = 0; i < test.getQuestionsCount(); i++) {
            Answer answer = new Answer(i, test.getAnswer(i));

            if (!parentAnswerGroup.contains(answer)) {
                answer.setProbability(probabilityMap.get(i));

                AnswerGroup answerGroup = (AnswerGroup) parentAnswerGroup.clone();
                answerGroup.addAnswer(answer);
                answerGroupSet.addAnswerGroup(answerGroup);

                generateAnswerGroupsRecursive(test, answerGroup, elementsCount + 1, answerGroupSet);
            }
        }
    }

    private void generateNewTestsRecursive(Test test, int questionNumber) throws CloneNotSupportedException {
        if (questionNumber == questionCount) {
            return;
        }

        int answersCount = sizes[questionNumber];

        Test[] tests = new Test[answersCount];
        tests[0] = test;

        for (int i = 0; i < answersCount; i++) {
            Test currentTest = tests[i];

            if (currentTest == null) {
                currentTest = (Test) test.clone();
                currentTest.setAnswer(i, questionNumber);
                possibleTests.add(currentTest);
            } else {
                currentTest.setAnswer(i, questionNumber);
            }

            generateNewTestsRecursive(currentTest, questionNumber + 1);
        }
    }

    public int getPossibleTestsNumber() {
        int number = 1;

        for (int i = 0; i < sizes.length; i++) {
            number *= sizes[i];
        }

        return number;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public int[] getSizes() {
        return sizes;
    }

    public Set<Test> getPossibleTests() {
        return possibleTests;
    }

    private Test findNextBestTest() {
        return Collections.max(possibleTests);
    }

    public Set<Test> generateTests(int testCount) {
        assert testCount <= possibleTests.size();

        acceptedTests = new HashSet<Test>(testCount);

        for (int i = 0; i < testCount; i++) {
            Test test = findNextBestTest();
            acceptedTests.add(test);
            possibleTests.remove(test);
            updateTestsQuality(test);
        }

        return acceptedTests;
    }

    private void updateTestsQuality(Test acceptedTest) {
        for (Test test : possibleTests) {
            for (AnswerGroup acceptedAnswerGroup : acceptedTest.getAnswerGroupSet().getAnswerGroups()) {
                if (test.getAnswerGroupSet().getAnswerGroups().contains(acceptedAnswerGroup)) {
                    test.getAnswerGroupSet().removeAnswerGroup(acceptedAnswerGroup);
                }
            }
        }
    }
}
