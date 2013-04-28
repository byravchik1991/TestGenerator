package books;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Иришка
 * Date: 13.04.13
 */
public class SmartTestGenerator extends AbstractTestGenerator {
    //всевозможные тесты, которые можно сгенерировать на данном множестве вопросов
    private Set<SmartTest> possibleTestsSet;
    private List<SmartTest> possibleTestsList;

    //вероятности выбора каждого ответа в вопросе
    private Map<Integer, Double> probabilityMap;

    //максимальное количество вопросов в группе, вероятность которой мы еще учитываем
    private int answerGroupMaxSize;

    private long possibleTestsCount;

    private SmartTest template;

    private int[] transitions;

    private ExecutorService service;

    public static final int MIN_TESTS_COUNT_TO_EXECUTE = 50;


    public SmartTestGenerator(int[] sizes) throws CloneNotSupportedException {
        this(sizes, sizes.length, 0);
    }

    public SmartTestGenerator(int[] sizes, int answerGroupMaxSize) throws CloneNotSupportedException {
        this(sizes, answerGroupMaxSize, 0);
    }

    public SmartTestGenerator(int[] sizes, int answerGroupMaxSize, long possibleTestsCount) throws CloneNotSupportedException {
        super(sizes);

        service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        this.answerGroupMaxSize = answerGroupMaxSize;
        this.possibleTestsCount = possibleTestsCount;

        initializeProbabilityMap();

        generatePossibleTests();
        possibleTestsList = new ArrayList<SmartTest>(possibleTestsSet);
        possibleTestsSet = null;
        System.gc();
    }

    public Set<SmartTest> getPossibleTestsSet() {
        return possibleTestsSet;
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
        boolean randomGeneration = possibleTestsCount != 0;

        if (possibleTestsCount == 0) {
            possibleTestsCount = getPossibleTestsNumber(sizes);
        }

        if (possibleTestsCount > Integer.MAX_VALUE) {
            throw new IllegalStateException("Dimension of the problem is too big");
        }

        Logger.getLogger(this.getClass().getName()).info("Creation of " + possibleTestsCount + " possible tests");
        possibleTestsSet = new HashSet<SmartTest>((int) possibleTestsCount);

        if (!randomGeneration) {
            SmartTest test = new SmartTest(questionCount);
            possibleTestsSet.add(test);
            generateNewTestsRecursive(test, 0);

        } else {
            Random r = new Random();

            while (possibleTestsSet.size() != possibleTestsCount) {
                SmartTest smartTest = new SmartTest(questionCount);

                for (int i = 0; i < questionCount; i++) {
                    smartTest.setAnswer(r.nextInt(sizes[i]), i);
                }

                possibleTestsSet.add(smartTest);
            }
        }

        createTemplateSmartTest(answerGroupMaxSize);

        Logger.getLogger(this.getClass().getName()).info(
                "Number of answer combinations = " + template.getAnswerGroups().size() +
                        " (depth = " + answerGroupMaxSize + ")");

        for (SmartTest currentTest : possibleTestsSet) {
            currentTest.setAnswerGroupsCheckStates(new boolean[template.getAnswerGroups().size()]);

            for (int i = 0; i < template.getAnswerGroups().size(); i++) {
                currentTest.setAnswerGroupChecked(i, true);
            }

            currentTest.setQuality(template.getQuality());
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
                possibleTestsSet.add(currentTest);

            } else {
                currentTest.setAnswer(i, questionNumber);
            }

            generateNewTestsRecursive(currentTest, questionNumber + 1);
        }
    }

    //возвращает общее количество тестов, которые можно провести на данном множестве вопросов
    public static long getPossibleTestsNumber(int[] sizes) {
        long number = 1;

        for (int i = 0; i < sizes.length; i++) {
            number *= sizes[i];
        }

        return number;
    }

    //заполнение всех групп ответов в тесте согласно шаблону
    private void fillTemplate(SmartTest currentTest, SmartTest template) throws CloneNotSupportedException {
        currentTest.setAnswerGroups(new ArrayList<AnswerGroup>(template.getAnswerGroups().size()));

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
    private void createTemplateSmartTest(int importantDepth) throws CloneNotSupportedException {
        template = new SmartTest(questionCount);

        for (int i = 0; i < questionCount - 1; i++) {
            generateAnswerGroupsRecursive(new AnswerGroup(0), 1, importantDepth, i);
        }

        fillTransitions();
    }

    private void generateAnswerGroupsRecursive(
            AnswerGroup parentAnswerGroup,
            int elementsCount,
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
                template.addAnswerGroup(answerGroup);

                generateAnswerGroupsRecursive(
                        answerGroup,
                        elementsCount + 1,
                        importantDepth,
                        questionNumber);
            }
        }
    }

    @Override
    protected Test findNextBestTest() {
        return Collections.max(possibleTestsList);
    }

    /*@Override
    protected void updateTestGeneratorByNewTest(final Test acceptedTest) {
        possibleTestsSet.remove(acceptedTest);

        final List<Callable<Object>> tasks = new LinkedList<Callable<Object>>();

        for (final SmartTest test : possibleTestsSet) {
            tasks.add(*//*service.submit*//*Executors.callable(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < template.getAnswerGroups().size(); i++) {

*//*                if (!test.isAnswerGroupUnchecked(i)) {
                    continue;
                }*//*

                        AnswerGroup answerGroup = template.getAnswerGroups().get(i);

                        boolean contains = true;
                        for (Answer answer : answerGroup.getAnswers()) {
                            if (test.getAnswer(answer.getQuestionNumber())
                                    != acceptedTest.getAnswer(answer.getQuestionNumber())) {
                                contains = false;
                                break;
                            }
                        }

                        if (contains) {
                            if (test.isAnswerGroupUnchecked(i)) {
                                test.setQuality(test.getQuality() - answerGroup.getProbability());
                                test.setAnswerGroupsCheckStates(i, false);
                            }

                        } else {
                            i = transitions[i] - 1;
                        }
                    }
                }
            }));
        }

        try {
            service.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

*//*        ((SmartTest) acceptedTest).setAnswerGroups(null);
        System.gc();*//*
    }
*/
    @Override
    protected void updateTestGeneratorByNewTest(final Test acceptedTest) {
        possibleTestsList.remove(acceptedTest);

        final List<Callable<Object>> tasks = new LinkedList<Callable<Object>>();

        for (int i = 0; i < possibleTestsList.size(); i += MIN_TESTS_COUNT_TO_EXECUTE) {

            final int finalI = i;

            tasks.add(Executors.callable(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < MIN_TESTS_COUNT_TO_EXECUTE; j++) {

                        final SmartTest test = possibleTestsList.get(finalI + j);

                        for (int i = 0; i < template.getAnswerGroups().size(); i++) {

                            AnswerGroup answerGroup = template.getAnswerGroups().get(i);

                            boolean contains = true;
                            for (Answer answer : answerGroup.getAnswers()) {
                                if (test.getAnswer(answer.getQuestionNumber())
                                        != acceptedTest.getAnswer(answer.getQuestionNumber())) {
                                    contains = false;
                                    break;
                                }
                            }

                            if (contains) {
                                if (test.isAnswerGroupUnchecked(i)) {
                                    test.setQuality(test.getQuality() - answerGroup.getProbability());
                                    test.setAnswerGroupChecked(i, false);
                                }

                            } else {
                                i = transitions[i] - 1;
                            }
                        }
                    }
                }
            }));
        }

        try {
            service.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void fillTransitions() {
        transitions = new int[template.getAnswerGroups().size()];

        for (int i = 0; i < template.getAnswerGroups().size(); i++) {
            AnswerGroup answerGroup = template.getAnswerGroups().get(i);

            int j = i + 1;
            if (j >= template.getAnswerGroups().size()) {
                break;
            }

            int size = answerGroup.getAnswers().size();

            while (j < template.getAnswerGroups().size()) {

                AnswerGroup anotherGroup = template.getAnswerGroups().get(j);
                int anotherSize = answerGroup.getAnswers().size();
                int minSize = anotherSize > size ? size : anotherSize;

                boolean contains = true;

                for (int m = 0; m < minSize; m++) {
                    if (anotherGroup.getAnswers().get(m).getQuestionNumber()
                            != answerGroup.getAnswers().get(m).getQuestionNumber()) {
                        contains = false;
                        break;
                    }
                }

                if (contains) {
                    j++;
                } else {
                    break;
                }
            }

            transitions[i] = j;
        }

        transitions[template.getAnswerGroups().size() - 1] = template.getAnswerGroups().size();
    }
}
