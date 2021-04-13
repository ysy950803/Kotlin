import java.util.Arrays;

public class Test {

    public static void main(String[] args) {
//        System.out.println(Arrays.toString("A\\B".split(TestKt.EXT_CATEGORIES_SEPARATOR)));

        int editCode = 0b10001001;
        System.out.println(isEditable(editCode, 3));
    }

    static boolean isEditable(int editCode, int index) {
        return ((editCode >> index) & 1) == 1;
    }
}
