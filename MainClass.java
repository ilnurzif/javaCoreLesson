import java.util.Arrays;

public class MainClass {
    static final int size = 10000000;
    static float[]  arr;

    static {
      arr = new float[size];
      for (int i = 0; i<size  ; i++)
        arr[i]=1;
    }

    static class ThreadCalc extends Thread {
       float[] arr;
       public ThreadCalc(float[] arr) {
         this.arr=arr;
       }

       @Override
        public void run() {
           for (int i = 0; i < arr.length; i++) {
               arr[i] = (float) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
           }
        }
    }

    public static void main(String[] args) {
       System.out.println("not thread method work time = " + simpleCalc());
       System.out.println("use 2 threads method work time = " + threadCalc());
    }

    public static long simpleCalc() {
        float[] arrtemp = new float[size];
        System.arraycopy(arr, 0, arrtemp, 0, size-1);
        long a = System.currentTimeMillis();
        for (int i = 0; i<size  ; i++) {
            arrtemp[i] = (float)(arrtemp[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        return System.currentTimeMillis()-a;
    }

    public static long threadCalc() {
        int h = size / 2;
        float[] arr1 = new float[h];
        float[] arr2 = new float[h];
        long a = System.currentTimeMillis();
        System.arraycopy(arr, 0, arr1, 0, h);
        System.arraycopy(arr, h, arr2, 0, h);

        Thread thread1 = new ThreadCalc(arr1);
        Thread thread2 = new ThreadCalc(arr2);
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.arraycopy(arr1, 0, arr, 0, arr1.length);
        System.arraycopy(arr2, 0, arr, h, arr2.length);

        return System.currentTimeMillis() - a;
       }
  }
