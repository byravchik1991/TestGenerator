package books;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Иришка
 * Date: 12.04.13
 */
public class Test {
    //количество вопросов в тесте
    protected int questionsCount;

    //ответы (номера ответов для каждого вопроса)
    protected int[] answers;

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
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();

        for (int i = 0; i < questionsCount; i++) {
            stringBuffer.append(answers[i]).append(" ");
        }

        return stringBuffer.toString();
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
}
