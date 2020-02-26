public class MyTested {
    @BeforeSuite
    public static void beforeMethod() {
        System.out.println("beforeMethod");
    }

    @Test(priority=134)
    public static void test1() {
        System.out.println("test1");
    }

    @Test(priority=2)
    public static void test2() {
        System.out.println("test2");
    }

    @Test(priority=3)
    public static void test3() {
        System.out.println("test3");
    }

    @AfterSuite
    public static void afterMethod() {
        System.out.println("afterMethod");
    }

}
