package books;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Иришка
 * Date: 16.04.13
 */
public class SmartTest extends Test implements Cloneable, Comparable {
    //множество всех групп ответов, проверяемых в тесте
    private List<AnswerGroup> answerGroups = new ArrayList<AnswerGroup>();

    //"качество теста" (сумма вероятностей выпада всех групп ответов)
    private double quality = 0.0;

    private boolean[] answerGroupsCheckStates;

    public SmartTest(int n) {
        super(n);
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
    public int compareTo(Object o) {
        SmartTest anotherTest = (SmartTest) o;
        return Double.compare(quality, anotherTest.quality);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Test newTest = (Test) super.clone();
        newTest.answers = answers.clone();
        return newTest;
    }

    public double getQuality() {
        return quality;
    }

    public void setQuality(double quality) {
        this.quality = quality;
    }

    public List<AnswerGroup> getAnswerGroups() {
        return answerGroups;
    }

    public void setAnswerGroups(List<AnswerGroup> answerGroups) {
        this.answerGroups = answerGroups;
    }

    public void addAnswerGroup(AnswerGroup answerGroup) {
        boolean added = answerGroups.add(answerGroup);

        if (added) {
            quality += answerGroup.getProbability();
        }
    }

    public void removeAnswerGroup(AnswerGroup answerGroup) {
        boolean removed = answerGroups.remove(answerGroup);

        if (removed) {
            quality -= answerGroup.getProbability();

            assert quality >= 0;
        }
    }

    public boolean isAnswerGroupUnchecked(int n) {
        return answerGroupsCheckStates[n];
    }

    public void setAnswerGroupChecked(int n, boolean isNew) {
        answerGroupsCheckStates[n] = isNew;
    }

    public boolean[] getAnswerGroupsCheckStates() {
        return answerGroupsCheckStates;
    }

    public void setAnswerGroupsCheckStates(boolean[] answerGroupsCheckStates) {
        this.answerGroupsCheckStates = answerGroupsCheckStates;
    }
}
