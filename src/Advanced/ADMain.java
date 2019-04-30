package Advanced;

public class ADMain {

    public static void main(String[] args) {
        System.out.println(ExtJVMName.TOP_LEVEL);
        ExtJVMName.showTopLevel();

        Extension.Singleton.INSTANCE.doSomething();
        Extension.Singleton.makeSomething();

        System.out.println(Extension.TAG);
        Extension.staticFunc();
        Extension.Companion.getSTATIC_OBJ();
    }
}
