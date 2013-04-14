package books;

/**
 * Created with IntelliJ IDEA.
 * User: Иришка
 * Date: 12.04.13
 */
public class SimpleTestGenerator {
    private int questionCount;
    private int answersCount[];
    private int maxTestCount;

    private int[][] answersMap;

    public SimpleTestGenerator(int questionCount, int[] answersCount, int maxTestCount) {
        this.questionCount = questionCount;
        this.answersCount = answersCount;
        this.maxTestCount = maxTestCount;

        answersMap = new int[questionCount][];

        for (int i = 0; i < questionCount; i++) {
            answersMap[i] = new int[answersCount[i]];
        }
    }

    public Test generateNextTest() {
        Test test = new Test(questionCount);

        for (int i = 0; i < questionCount; i++) {

        }

        return null;
    }

    public Test[] generateTestsForSingleAnswer() {
        int testCount = getTestCountForSingleAnswer();
        Test[] tests = new Test[testCount];

        for (int i = 0; i < testCount; i++) {
            Test test = new Test(questionCount);

            for (int j = 0; j < questionCount; j++) {
                test.setAnswer(findNextAnswer(j), j);
            }

            tests[i] = test;
            updateAnswersMapByNewTest(test);
        }

        return tests;
    }

    private int getTestCountForSingleAnswer() {
        return ArrayHelper.findMaxValue(answersCount);
    }

    private int findNextAnswer(int questionNumber) {
        return ArrayHelper.findIndexOfMinValue(answersMap[questionNumber]);
    }

    private void updateAnswersMapByNewTest(Test test) {
        for (int i = 0; i < test.getQuestionsCount(); i++) {
            answersMap[i][test.getAnswer(i)] = answersMap[i][test.getAnswer(i)] + 1;
        }
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public int[] getAnswersCount() {
        return answersCount;
    }
}
