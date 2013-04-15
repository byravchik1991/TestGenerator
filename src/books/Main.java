package books;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
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

            SmartTestGenerator testGenerator = new SmartTestGenerator(sizes, b);
            List<Test> tests = testGenerator.generateTests();

            for (Test test : tests) {
                System.out.println(test);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
