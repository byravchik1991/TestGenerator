package books;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: Иришка
 * Date: 12.04.13
 */
public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(new File("input.txt"));

            int n = scanner.nextInt();

            int[] sizes = new int[n];
            for (int i = 0; i < n; i++) {
                sizes[i] = scanner.nextInt();
            }

            int b = scanner.nextInt();

            SimpleTestGenerator testGenerator = new SimpleTestGenerator(n, sizes, b);
            Test[] tests = testGenerator.generateTestsForSingleAnswer();

            for (Test test : tests) {
                System.out.println(Arrays.toString(test.getAnswers()));
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
