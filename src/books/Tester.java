package books;

import java.util.Random;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Иришка
 * Date: 21.04.13
 */
public class Tester {
    public static void main(String[] args) throws CloneNotSupportedException {
        if (args.length < 4) {
            Logger.getLogger("Test Generator Tester").severe("Неверное число аргументов при запуске");
            return;
        }

        int taskSize = Integer.parseInt(args[0]);
        int answerGroupSize = Integer.parseInt(args[1]);
        int testsCount = Integer.parseInt(args[2]);
        String testGeneratorType = args[3];

        if (taskSize <= 0) {
            Logger.getLogger("Test Generator Tester").severe("Размерность задачи должна быть больше нуля");
            return;
        }

        if (answerGroupSize < 1 || answerGroupSize > taskSize) {
            Logger.getLogger("Test Generator Tester").severe(
                    "Количетство ответов в группе должно быть от 1 до " + taskSize);
            return;
        }

        if (testsCount <= 0) {
            Logger.getLogger("Test Generator Tester").severe("Число тестов быть больше нуля");
            return;
        }

        if ("simple".equals(testGeneratorType)) {
            AbstractTestGenerator simpleTestGenerator = new SimpleTestGenerator(getSizesArray(taskSize));
            double simpleTests = getTestsCountForFindingAnswerGroup(answerGroupSize, simpleTestGenerator, testsCount);
            Logger.getLogger("Test Generator Tester").info("Simple: " + simpleTests);

        } else if ("random".equals(testGeneratorType)) {
            AbstractTestGenerator randomTestGenerator = new RandomTestGenerator(getSizesArray(taskSize));
            double randomTests = getTestsCountForFindingAnswerGroup(answerGroupSize, randomTestGenerator, testsCount);
            Logger.getLogger("Test Generator Tester").info("Random: " + randomTests);

        } else {
            AbstractTestGenerator smartTestGenerator = new SmartTestGenerator(getSizesArray(taskSize));
            double smartTests = getTestsCountForFindingAnswerGroup(answerGroupSize, smartTestGenerator, testsCount);
            Logger.getLogger("Test Generator Tester").info("Smart: " + smartTests);
        }
    }

    private static double getTestsCountForFindingAnswerGroup(
            int answerGroupSize,
            AbstractTestGenerator testGenerator,
            int repeatCount) throws CloneNotSupportedException {

        int testsCount = 0;
        Random r = new Random(System.currentTimeMillis());

        for (int i = 0; i < repeatCount; i++) {
            if (testGenerator instanceof SmartTestGenerator) {
                testGenerator = new SmartTestGenerator(testGenerator.getSizes());
            } else if (testGenerator instanceof SimpleTestGenerator) {
                testGenerator = new SimpleTestGenerator(testGenerator.getSizes());
            } else {
                testGenerator = new RandomTestGenerator(testGenerator.getSizes());
            }

            Test test = testGenerator.generateNextTest();
            testsCount++;

            AnswerGroup answerGroup = new AnswerGroup(answerGroupSize);

            for (int j = 0; j < answerGroupSize; j++) {
                int questionNumber = r.nextInt(test.getQuestionsCount());
                int answerNumber = r.nextInt(testGenerator.getSizes()[questionNumber]);
                Answer answer = new Answer(questionNumber, answerNumber);

                while (answerGroup.contains(answer)) {
                    questionNumber = r.nextInt(test.getQuestionsCount());
                    answerNumber = r.nextInt(testGenerator.getSizes()[questionNumber]);
                    answer = new Answer(questionNumber, answerNumber);
                }

                answerGroup.addAnswer(answer);
            }

            while (!containsAnswerGroup(test, answerGroup)) {
                test = testGenerator.generateNextTest();
                testsCount++;
            }
        }

        return ((double) testsCount / repeatCount);
    }

    private static boolean containsAnswerGroup(Test test, AnswerGroup answerGroup) {
        for (Answer answer : answerGroup.getAnswers()) {
            if (test.getAnswer(answer.getQuestionNumber()) != answer.getAnswerNumber()) {
                return false;
            }
        }

        return true;
    }

    private static int[] getSizesArray(int n) {
        int[] sizes = new int[n];

        for (int i = 0; i < n; i++) {
            sizes[i] = n;
        }

        return sizes;
    }
}
