import com.sun.org.apache.xpath.internal.operations.Or;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] srcStr={"one","tho","three"};

        // Проверка метода change_arr
        change_arr(srcStr,0,2);
        System.out.println("check work change_arr method = " + Arrays.toString(srcStr));

        // Проверка метода copyToArrList
        List arStrList = copyToArrList(srcStr);
        System.out.println("check work copyToArrList method = " + arStrList.toString());

        // Проверка метода compare
        Box<Apple> appleBox= new Box<>();
        appleBox.add(new Apple(), 3);
        Box<Orange> orangeBox=new Box<>();
        orangeBox.add(new Orange(),2);
        System.out.println("check work compare method = " + appleBox.compare(orangeBox));

        // Проверка метода intersperse предназначееного для пересыпания из одной коробки в другую
        Box<Orange> orangeBox2=new Box<>();
        orangeBox.intersperse(orangeBox2);
        System.out.println("check work intersperse method =" + orangeBox2.toString());
    }

    public static <T> void change_arr(T arr[], int srcIndex, int dstIndex) {
        T tempel=arr[dstIndex];
        arr[dstIndex]=arr[srcIndex];
        arr[srcIndex]=tempel;
    }

    public static <T> List<T> copyToArrList(T[] scrArr) {
        return Arrays.asList(scrArr);
    }
}

