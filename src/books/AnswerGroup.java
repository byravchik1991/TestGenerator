package books;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Иришка
 * Date: 13.04.13
 */


public class AnswerGroup implements Cloneable {
    //сочетание ответов, проверяемых в тесте
    private List<Answer> answers;

    //вероятность выбора такой комбинации ответов
    private double probability = 1;

    public AnswerGroup(int size) {
        answers = new ArrayList<Answer>(size);
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
        probability *= answer.getProbability();
    }

    public boolean contains(Answer answer) {
        for (Answer currentAnswer : answers) {
            if (currentAnswer.getQuestionNumber() == answer.getQuestionNumber()) {
                return true;
            }
        }

        return false;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnswerGroup answerGroup = (AnswerGroup) o;

        if (!answers.equals(answerGroup.answers)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return answers.hashCode();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        AnswerGroup newAnswerGroup = (AnswerGroup) super.clone();

        List newAnswers = new ArrayList(answers.size());

        for (Answer answer : answers) {
            newAnswers.add(answer.clone());
        }
        newAnswerGroup.answers = newAnswers;

        return newAnswerGroup;
    }
}
