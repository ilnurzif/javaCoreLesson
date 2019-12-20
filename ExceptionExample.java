

public class ExceptionExample {
    private static final int size = 4;

    public static void main(String[] args) {
        String[][] a = {{"1", "2", "3","4"},{"1", "2", "3","4"},{"1", "2", "3","4"},{"1", "2", "3","4"}};
      //  String[] a = {{"1", "2", "3","4"}};
        try {
            myMethod(a);
        } catch (MyArraySizeException e) {
            e.printStackTrace();
        } catch (MyArrayDataException e) {
            e.printStackTrace();
        } catch (msgArrayNotSupportedArray msgArrayNotSupportedArray) {
            msgArrayNotSupportedArray.printStackTrace();
        }
    }

    public static void myMethod(Object objArr) throws MyArraySizeException, MyArrayDataException, msgArrayNotSupportedArray {
        if (!(objArr instanceof String[][]))
            throw new msgArrayNotSupportedArray();
        try
         {if (((String[][]) objArr)[0].length != size || ((String[][]) objArr)[1].length != size)
             throw new MyArraySizeException();
         }
        catch (ArrayIndexOutOfBoundsException e)
         {e.printStackTrace();}

        String[][] arr = (String[][]) objArr;
        int summ = 0;
        int a;
        for (int i = 0; i < size; i++) {
         for (int j = 0; j < size; j++) {
          try
           {a=Integer.parseInt(arr[i][j]);}
          catch (NumberFormatException e)
            { throw new MyArrayDataException(i, j);}
          summ += a;
          }
        }
        System.out.print("Summ="+summ);
    }
}

class  MyArraySizeException extends Exception {
    public MyArraySizeException() {
        super(MsgUtility.msgArrayDataException);
    }
}

class MyArrayDataException extends NumberFormatException {
  public MyArrayDataException(int col, int row) {
      super(MsgUtility.msgArrayDataException+"["+col+"]["+row+"]");
  }
}

class msgArrayNotSupportedArray extends Exception {
    public msgArrayNotSupportedArray() {
        super(MsgUtility.msgArrayNotSupportedArray);
    }
}

class MsgUtility {
 public static final String msgArrayNotSupportedArray="Объект не соответсвует типу данных String[][]";
 public static final String msgArrayDataException="Не удалось преобразовать к типу int элемент массива находящийся в ячейке: ";
 public static final String msgSizeException="Некорректный размер массива";
}



