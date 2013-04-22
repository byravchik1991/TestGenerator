package books;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Иришка
 * Date: 12.04.13
 */
public class Main {
    public static void main(String[] args) throws CloneNotSupportedException {
        try {
            Scanner scanner = new Scanner(new File("input.txt"));

            int n = scanner.nextInt();

            int[] sizes = new int[n];
            for (int i = 0; i < n; i++) {
                sizes[i] = scanner.nextInt();
            }

            int b = scanner.nextInt();

            String testGeneratorType = null;
            if (args.length > 0) {
                testGeneratorType = args[0];
            }

            AbstractTestGenerator testGenerator;
            if ("simple".equals(testGeneratorType)) {
                testGenerator = new SimpleTestGenerator(sizes);

            } else if ("random".equals(testGeneratorType)) {
                testGenerator = new RandomTestGenerator(sizes);

            } else {
                if (args.length > 1) {
                    int precision = Integer.parseInt(args[1]);
                    if ((precision > n) || (precision < 1)) {
                        Logger.getLogger("Test Generator").severe("Accuracy should be in the range from 1 to " + n);
                        return;
                    }
                    testGenerator = new SmartTestGenerator(sizes, precision);

                } else {
                    testGenerator = new SmartTestGenerator(sizes);
                }
            }

            List<Test> tests = testGenerator.generateTests(b);
            PrintWriter printWriter = new PrintWriter(new File("output.txt"));

            for (Test test : tests) {
                printWriter.println(test);
            }

            printWriter.close();

        } catch (FileNotFoundException e) {
            Logger.getLogger("Test Generator").severe(e.getMessage());

        } catch (NumberFormatException e) {
            Logger.getLogger("Test Generator").severe(e.getMessage());
        }
    }
}
