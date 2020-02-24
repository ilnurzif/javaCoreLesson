import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        Main mobj=new Main();
         int[] arr=mobj.recArr(new int[]{1, 2, 4, 4, 2, 3, 4, 1, 7});

        boolean res=mobj.checkNumb(new int[]{ 2, 4, 4, 2, 1, 3, 4, 7});
    }

    // 1 задание
    public int[] recArr(int[] inarr) {
        Stack<Integer> stack=new Stack<Integer>();
        for (int i = inarr.length-1; i >-1 ; i--) {
           if (inarr[i]==4) {
               int[] outarr=new int[stack.size()];
               int j=0;
               while(!stack.isEmpty())
                   outarr[j++]=stack.pop();
               return outarr;
           }
            stack.push(inarr[i]);
        }
        throw new RuntimeException("Not supported formated in array");
    }

    // 2 задание
    public boolean checkNumb(int[] inarr) {
        boolean exists4numb=false; boolean exists1numb=false;
        for (int i = 0; i < inarr.length; i++) {
            if (inarr[i]==1) exists1numb=true;
            if (inarr[i]==4) exists4numb=true;
            if (exists4numb&&exists1numb)
              return true;
        }
            return  false;
        }
}
