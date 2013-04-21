package books;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Иришка
 * Date: 13.04.13
 */
public class SmartTestGenerator extends AbstractTestGenerator {
    //всевозможные тесты, которые можно сгенерировать на данном множестве вопросов
    private Set<SmartTest> possibleTests;

    //вероятности выбора каждого ответа в вопросе
    private Map<Integer, Double> probabilityMap;

    //максимальное количество вопросов в группе, вероятность которой мы еще учитываем
    private int answerGroupMaxSize;

    public SmartTestGenerator(int[] sizes) throws CloneNotSupportedException {
        this(sizes, sizes.length);
    }

    public SmartTestGenerator(int[] sizes, int answerGroupMaxSize) throws CloneNotSupportedException {
        super(sizes);

        this.answerGroupMaxSize = answerGroupMaxSize;

        initializeProbabilityMap();

        generatePossibleTests();
    }

    public Set<SmartTest> getPossibleTests() {
        return possibleTests;
    }

    public int getAnswerGroupMaxSize() {
        return answerGroupMaxSize;
    }

    public void setAnswerGroupMaxSize(int answerGroupMaxSize) {
        this.answerGroupMaxSize = answerGroupMaxSize;
    }

    private void initializeProbabilityMap() {
        probabilityMap = new HashMap<Integer, Double>(questionCount);

        for (int i = 0; i < sizes.length; i++) {
            double probability = 1.0 / sizes[i];
            probabilityMap.put(i, probability);
        }
    }

    //генерация всех возможных тестов на данном множестве вопросов
    private void generatePossibleTests() throws CloneNotSupportedException {
        int possibleTestsCount = getPossibleTestsNumber();
        Logger.getLogger(this.getClass().getName()).info("Генерация всех возможных " + possibleTestsCount + " тестов");

        if (possibleTestsCount < 0) {
            throw new IllegalStateException("Размерность задачи слишком велика");
        }

        possibleTests = new HashSet<SmartTest>();
        SmartTest test = new SmartTest(questionCount);
        possibleTests.add(test);
        generateNewTestsRecursive(test, 0);

        Logger.getLogger(this.getClass().getName()).info(
                "Инициализация сочетаний из " + answerGroupMaxSize + " ответов для каждого теста");

        int testsGenerated = 0;
        SmartTest template = createTemplateSmartTest(answerGroupMaxSize);
        for (SmartTest currentTest : possibleTests) {
            fillTemplate(currentTest, template);
            testsGenerated++;
            if (testsGenerated % 1000 == 0) {
                Logger.getLogger(this.getClass().getName()).info("Обработано " + testsGenerated + " тестов");
            }
        }
    }

    private void generateNewTestsRecursive(SmartTest test, int questionNumber) throws CloneNotSupportedException {
        if (questionNumber == questionCount) {
            return;
        }

        int answersCount = sizes[questionNumber];

        SmartTest[] tests = new SmartTest[answersCount];
        tests[0] = test;

        for (int i = 0; i < answersCount; i++) {
            SmartTest currentTest = tests[i];

            if (currentTest == null) {
                currentTest = (SmartTest) test.clone();
                currentTest.setAnswer(i, questionNumber);
                possibleTests.add(currentTest);
            } else {
                currentTest.setAnswer(i, questionNumber);
            }

            generateNewTestsRecursive(currentTest, questionNumber + 1);
        }
    }

    //возвращает общее количество тестов, которые можно провести на данном множестве вопросов
    public int getPossibleTestsNumber() {
        int number = 1;

        for (int i = 0; i < sizes.length; i++) {
            number *= sizes[i];
        }

        return number;
    }

    //заполнение всех групп ответов в тесте согласно шаблону
    private void fillTemplate(SmartTest currentTest, SmartTest template) throws CloneNotSupportedException {
        currentTest.setAnswerGroups(new HashSet<AnswerGroup>(template.getAnswerGroups().size()));

        for (AnswerGroup templateAnswerGroup : template.getAnswerGroups()) {
            AnswerGroup answerGroup = new AnswerGroup(templateAnswerGroup.getAnswers().size());

            for (Answer templateAnswer : templateAnswerGroup.getAnswers()) {
                Answer answer = new Answer(
                        templateAnswer.getQuestionNumber(),
                        currentTest.getAnswer(templateAnswer.getQuestionNumber()));

                answer.setProbability(templateAnswer.getProbability());

                answerGroup.addAnswer(answer);
            }

            currentTest.addAnswerGroup(answerGroup);
        }
    }

    /*создает шаблон теста, где сгенерированы все группы вопросов,
    но для каждого вопроса указан только его номер, и не указан номер ответа*/
    private SmartTest createTemplateSmartTest(int importantDepth) throws CloneNotSupportedException {
        SmartTest smartTest = new SmartTest(questionCount);

        for (int i = 0; i < questionCount - 1; i++) {
            generateAnswerGroupsRecursive(new AnswerGroup(0), 1, smartTest, importantDepth, i);
        }

        return smartTest;
    }

    private void generateAnswerGroupsRecursive(
            AnswerGroup parentAnswerGroup,
            int elementsCount,
            SmartTest test,
            int importantDepth,
            int questionNumber) throws CloneNotSupportedException {

        if ((elementsCount > questionCount) || (elementsCount > importantDepth)) {
            return;
        }

        for (int i = questionNumber; i < questionCount; i++) {
            Answer answer = new Answer(i, 0);

            if (!parentAnswerGroup.contains(answer)) {
                answer.setProbability(probabilityMap.get(i));

                AnswerGroup answerGroup = (AnswerGroup) parentAnswerGroup.clone();

                answerGroup.addAnswer(answer);
                test.addAnswerGroup(answerGroup);

                generateAnswerGroupsRecursive(
                        answerGroup,
                        elementsCount + 1,
                        test,
                        importantDepth,
                        questionNumber);
            }
        }
    }

    @Override
    protected Test findNextBestTest() {
        return Collections.max(possibleTests);
    }

    @Override
    protected void updateTestGeneratorByNewTest(Test acceptedTest) {
        possibleTests.remove(acceptedTest);

        for (SmartTest test : possibleTests) {
            for (AnswerGroup acceptedAnswerGroup : ((SmartTest) acceptedTest).getAnswerGroups()) {
                if (test.getAnswerGroups().contains(acceptedAnswerGroup)) {
                    test.removeAnswerGroup(acceptedAnswerGroup);
                }
            }
        }
    }
}
