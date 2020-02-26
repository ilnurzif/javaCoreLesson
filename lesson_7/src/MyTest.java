import com.sun.jmx.remote.internal.ArrayQueue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class MyTest {
   public static void start(Class c) {
        Method[] methods = c.getDeclaredMethods();
        executeAnnot(methods, BeforeSuite.class, true);
        executeAnnot(sortMethod(methods), Test.class, false);
        executeAnnot(methods, AfterSuite.class, true);
    }

    private static void executeAnnot(Method[] methods, Class c, Boolean single) {
        boolean methodrunning = false;
        for (Method m : methods) {
            if (m.isAnnotationPresent(c)) {
                try {
                    if (methodrunning && single)
                        throw new RuntimeException("Аннотация " + c.getName() + " должна присутствовать в классе только один раз");
                    m.invoke(null);
                    methodrunning = true;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Comparator<Method> testComparator = new Comparator<Method>(){
        @Override
        public int compare(Method m1, Method m2) {
            return (int) (m1.getAnnotation(Test.class).priority()- m2.getAnnotation(Test.class).priority());
        }
    };

    private static Method[] sortMethod(Method[] methods) {
        Queue<Method> queue = new PriorityQueue<Method>(3, testComparator);
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].isAnnotationPresent(Test.class))
                queue.add(methods[i]);
        }
        Method[] ms = new Method[queue.size()];
        int i = 0;
        while (!queue.isEmpty())
            ms[i++] = queue.poll();
        return ms;
    }
}
