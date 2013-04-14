package books;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Иришка
 * Date: 12.04.13
 */
public class Test implements Cloneable, Comparable {
    private int questionsCount;
    private int[] answers;

    private AnswerGroupSet answerGroupSet;

    public Test(int n) {
        this.questionsCount = n;
        answers = new int[n];
    }

    public int getAnswer(int questionNumber) {
        assert questionNumber <= questionsCount;
        assert questionNumber >= 0;

        return answers[questionNumber];
    }

    public void setAnswer(int answer, int questionNumber) {
        assert questionNumber <= questionsCount;
        assert questionNumber >= 0;

        answers[questionNumber] = answer;
    }

    public int getQuestionsCount() {
        return questionsCount;
    }

    public int[] getAnswers() {
        return answers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Test test = (Test) o;

        if (questionsCount != test.questionsCount) return false;
        if (!Arrays.equals(answers, test.answers)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = questionsCount;
        result = 31 * result + Arrays.hashCode(answers);
        return result;
    }

    @Override
    public String toString() {
        return Arrays.toString(answers);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Test newTest = (Test) super.clone();
        newTest.answers = answers.clone();
        return newTest;
    }

    @Override
    public int compareTo(Object o) {
        Test anotherTest = (Test) o;
        return Double.compare(answerGroupSet.getQuality(), anotherTest.answerGroupSet.getQuality());
    }

    public AnswerGroupSet getAnswerGroupSet() {
        return answerGroupSet;
    }

    public void setAnswerGroupSet(AnswerGroupSet answerGroupSet) {
        this.answerGroupSet = answerGroupSet;
    }
}
