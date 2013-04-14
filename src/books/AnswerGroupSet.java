package books;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Иришка
 * Date: 13.04.13
 */
public class AnswerGroupSet {
    private Set<AnswerGroup> answerGroups = new HashSet<AnswerGroup>();

    private double quality;

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
}
