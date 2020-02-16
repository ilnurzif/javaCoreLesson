public class Main {
    private static final Object mon = new Object();
    private static final int printCount = 5;
    private static char currentChar='A' ;
    private static char[] arr={'A','B','C'};

    public static void main(String[] args) {
         Main main=new Main();
        for (int i = 0; i < arr.length; i++) {
            int g=(i % arr.length);
            int d=((i+1) % arr.length);
           new Thread(()->main.printChar(arr[g],arr[d])).start();
        }
    }

    private void printChar(char printchar, char nextchar) {
        synchronized (mon) {
            try {
                for (int i = 0; i < printCount; i++) {
                    while (currentChar != printchar) {
                        mon.wait();
                    }
                    printMethod(currentChar);
                    currentChar=nextchar;
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void printMethod(char msg) {
        System.out.print(msg);
    }

}
