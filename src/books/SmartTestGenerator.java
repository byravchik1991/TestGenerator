package books;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Иришка
 * Date: 13.04.13
 */
public class SmartTestGenerator implements TestGenerator {
    private int questionCount;
    private int[] sizes;

    private Set<Test> possibleTests;
    private List<Test> acceptedTests;

    private Map<Integer, Double> probabilityMap;

    private int testsCount;

    public SmartTestGenerator(int[] sizes, int testsCount) throws CloneNotSupportedException {
        this.questionCount = sizes.length;
        this.sizes = sizes;
        this.testsCount = testsCount;

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
        int possibleTestsCount = getPossibleTestsNumber();
        Logger.getLogger(this.getClass().getName()).info("Генерация всех возможных " + possibleTestsCount + " тестов");

        if (possibleTestsCount < 0) {
            throw new IllegalStateException("Размерность задачи слишком велика");
        }

        possibleTests = new HashSet<Test>();
        Test test = new Test(questionCount);
        possibleTests.add(test);
        generateNewTestsRecursive(test, 0);

        int answerGroupsCount = getNumberOfTestGroups();
        int importantDepth = getDimensionOfLastImportantAnswerGroup(testsCount);
        Logger.getLogger(this.getClass().getName()).info(
                "Инициализация всех " + importantDepth + " сочетаний ответов, проверяемых тестами из " + questionCount);

        int tests = 0;
        AnswerGroupSet template = initializeAnswerGroupSet(answerGroupsCount, importantDepth);
        for (Test currentTest : possibleTests) {
            fillTemplate(currentTest, template);
            tests++;
            if (tests % 1000 == 0) {
                Logger.getLogger(this.getClass().getName()).info("Обработано " + tests + " тестов");
            }
        }
    }

    private void fillTemplate(Test currentTest, AnswerGroupSet template) throws CloneNotSupportedException {
        AnswerGroupSet answerGroupSet = new AnswerGroupSet(template.getAnswerGroups().size());

        for (AnswerGroup templateAnswerGroup : template.getAnswerGroups()) {
            AnswerGroup answerGroup = new AnswerGroup(templateAnswerGroup.getAnswers().size());

            for (Answer templateAnswer : templateAnswerGroup.getAnswers()) {
                Answer answer = new Answer(
                        templateAnswer.getQuestionNumber(),
                        currentTest.getAnswer(templateAnswer.getQuestionNumber()));

                answer.setProbability(templateAnswer.getProbability());

                answerGroup.addAnswer(answer);
            }

            answerGroupSet.addAnswerGroup(answerGroup);
        }

        currentTest.setAnswerGroupSet(answerGroupSet);
    }

    private AnswerGroupSet initializeAnswerGroupSet(int answerGroupsCount, int importantDepth) throws CloneNotSupportedException {
        AnswerGroupSet answerGroupSet = new AnswerGroupSet(answerGroupsCount);

        for (int i = 0; i < questionCount - 1; i++) {
            generateAnswerGroupsRecursive(new AnswerGroup(0), 1, answerGroupSet, importantDepth, i);
        }

        return answerGroupSet;
    }

    private void generateAnswerGroupsRecursive(
            AnswerGroup parentAnswerGroup,
            int elementsCount,
            AnswerGroupSet answerGroupSet,
            int importantDepth,
            int questionNumber) throws CloneNotSupportedException {

        if ((elementsCount > questionCount) || (elementsCount > importantDepth)) {
            return;
        }

        for (int i = questionNumber; i < questionCount; i++) {
            //for (Integer i : questionNumbersToIterate) {
            Answer answer = new Answer(i, 0);

            if (!parentAnswerGroup.contains(answer)) {
                // if (!parentAnswerGroup.getQuestionNumbers().contains(i)) {
                // Answer answer = new Answer(i, test.getAnswer(i));
                answer.setProbability(probabilityMap.get(i));

                AnswerGroup answerGroup = /*new AnswerGroup(elementsCount); // */(AnswerGroup) parentAnswerGroup.clone();
/*                for (Answer parentGroupAnswer : parentAnswerGroup.getAnswers()) {
                    answerGroup.addAnswer((Answer) parentGroupAnswer.clone());
                }*/

                answerGroup.addAnswer(answer);
                answerGroupSet.addAnswerGroup(answerGroup);

                generateAnswerGroupsRecursive(
                        answerGroup,
                        elementsCount + 1,
                        answerGroupSet,
                        importantDepth,
                        questionNumber);
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

    public List<Test> generateTests() {
        assert testsCount <= possibleTests.size();

        Logger.getLogger(this.getClass().getName()).info("Выбор " + testsCount + " самых подходящих тестов");

        acceptedTests = new ArrayList<Test>(testsCount);

        for (int i = 0; i < testsCount; i++) {
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

    public int getNumberOfTestGroups() {
        int sum = questionCount;
        for (int i = 2; i < questionCount; i++) {
            sum += (int) (sum * ((questionCount - i + 1) / ((double) i)));
        }

        return sum;
    }

  /*  private int cnk(int n, int k) {
        return (int) (factorial(n)/(((double) factorial(k)) * factorial(n-k)));
    }

    private long factorial(int k) {
        long factorial = 1;

        for(int i=2; i<=k; i++) {
            factorial *= i;
        }

        return  factorial;
    }*/

    private int getRequiredTestsCountForCheckAnswerGroup(int groupDimension) {
        Set<AnswerGroup> answerGroups = new HashSet<AnswerGroup>();

        for (Test test : possibleTests) {
            for (AnswerGroup answerGroup : test.getAnswerGroupSet().getAnswerGroups()) {
                if (answerGroup.getAnswers().size() == groupDimension) {
                    answerGroups.add(answerGroup);
                }
            }
        }

        int groupCheckedPerTest = questionCount / groupDimension;

        return answerGroups.size() / groupCheckedPerTest;
    }

    private int getDimensionOfLastImportantAnswerGroup(int testsCount) {
/*        Map<Integer, Integer> testsCountMap = new HashMap<Integer, Integer>(questionCount);

        int bestCloseFactor = Math.abs(getRequiredTestsCountForCheckAnswerGroup(1) - testsCount);
        int bestDimension = 1;
        for (int i = 2; i <= questionCount; i++) {
            int closeFactor = Math.abs(getRequiredTestsCountForCheckAnswerGroup(i) - testsCount);

            if ((closeFactor < bestCloseFactor) || ((closeFactor == bestCloseFactor) && (bestDimension < i)))  {
                bestCloseFactor = closeFactor;
                bestDimension = i;
            }
        }

        return  bestDimension + 1;*/
        return questionCount / 2 + 1;
    }

    public int getTestsCount() {
        return testsCount;
    }

    public void setTestsCount(int testsCount) {
        this.testsCount = testsCount;
    }
}
