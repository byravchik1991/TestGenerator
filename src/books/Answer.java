package books;

/**
 * Created with IntelliJ IDEA.
 * User: Иришка
 * Date: 13.04.13
 */
public class Answer implements Cloneable {
    private int questionNumber;
    private int answerNumber;

    private double probability;

    public Answer(int questionNumber, int answerNumber) {
        this.questionNumber = questionNumber;
        this.answerNumber = answerNumber;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public int getAnswerNumber() {
        return answerNumber;
    }

    public void setAnswerNumber(int answerNumber) {
        this.answerNumber = answerNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer answer = (Answer) o;

        if (answerNumber != answer.answerNumber) return false;
        if (questionNumber != answer.questionNumber) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = questionNumber;
        result = 31 * result + answerNumber;
        return result;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
