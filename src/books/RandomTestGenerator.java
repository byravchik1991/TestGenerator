package books;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Иришка
 * Date: 16.04.13
 */
public class RandomTestGenerator extends AbstractTestGenerator {
    private Random random = new Random();

    public RandomTestGenerator(int[] sizes) {
        super(sizes);
    }

    @Override
    protected Test findNextBestTest() {
        Test test = new Test(questionCount);

        for (int i = 0; i < questionCount; i++) {
            test.setAnswer(random.nextInt(sizes[i]), i);
        }

        return test;
    }

    @Override
    protected void updateTestGeneratorByNewTest(Test test) {
    }
}
