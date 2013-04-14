package books;

/**
 * Created with IntelliJ IDEA.
 * User: Иришка
 * Date: 13.04.13
 */
public class ArrayHelper {

    public static int findMaxValue(int[] array) {
        int max = array[0];

        for (int i : array) {
            if (i > max) {
                max = i;
            }
        }

        return max;
    }

    public static int findIndexOfMinValue(int[] array) {
        int minAnswerNumber = 0;
        int min = array[minAnswerNumber];

        for (int i = 0; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
                minAnswerNumber = i;
            }
        }

        return minAnswerNumber;
    }
}
