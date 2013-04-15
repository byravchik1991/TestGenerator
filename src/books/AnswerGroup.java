package books;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Иришка
 * Date: 13.04.13
 */
public class AnswerGroup implements Cloneable {
    private Set<Answer> answers;

    private double probability = 1;

    public AnswerGroup(int size) {
        answers = new HashSet<Answer>(size);
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
        //return answers.contains(answer);
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
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

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        AnswerGroup newAnswerGroup = (AnswerGroup) super.clone();

        Set newAnswers = new HashSet(answers.size());

        for (Answer answer : answers) {
            newAnswers.add(answer.clone());
        }
        newAnswerGroup.answers = newAnswers;

        // newAnswerGroup.answers = (Set<Answer>) ((HashSet)answers).clone();
        return newAnswerGroup;
    }
}
