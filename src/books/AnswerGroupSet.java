package books;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Иришка
 * Date: 13.04.13
 */
public class AnswerGroupSet implements Cloneable {
    private Set<AnswerGroup> answerGroups;

    private double quality = 0.0;

    public AnswerGroupSet(int groupsNumber) {
        answerGroups = new HashSet<AnswerGroup>(groupsNumber);
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

    public boolean contains(AnswerGroup answerGroup) {
        return answerGroups.contains(answerGroup);
    }

    public double getQuality() {
        return quality;
    }

    public void setQuality(double quality) {
        this.quality = quality;
    }

    public Set<AnswerGroup> getAnswerGroups() {
        return answerGroups;
    }

    public void setAnswerGroups(Set<AnswerGroup> answerGroups) {
        this.answerGroups = answerGroups;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        AnswerGroupSet newSet = (AnswerGroupSet) super.clone();

        // newSet.quality = 0.0;
        Set<AnswerGroup> newAnswerGroups = new HashSet<AnswerGroup>(answerGroups.size());

        for (AnswerGroup answerGroup : answerGroups) {
            newAnswerGroups.add((AnswerGroup) answerGroup.clone());
        }

        newSet.answerGroups = newAnswerGroups;

        return newSet;
    }
}
