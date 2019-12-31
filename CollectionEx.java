import java.util.*;

public class CollectionEx {
    static ArrListTest arrTest;
    public static void main(String[] args) {
        arrTest=new ArrListTest();
        arrTest.printUniqueWord();
        arrTest.wordCount();

        PhoneBook phoneBook=new PhoneBook();
        phoneBook.add("Иванов","89264554554");
        phoneBook.add("Петров","892634534535");
        phoneBook.add("Суриков","89269999999");
        phoneBook.add("Левашов","8926400000");
        phoneBook.add("Петров","892634538888");

        phoneBook.get("Петров");
    }
}

class ArrListTest {
    private List<String> arrList = new ArrayList<>();
    private HashSet<String> hashSet = new HashSet<>();
    private HashMap<String, Integer> hashMap = new HashMap<>();

    public ArrListTest() {
        Collections.addAll(arrList, new String[]{"cat", "dog", "pen", "dog", "cat", "phone", "plan", "select", "plan"});
        hashSet = new HashSet<>(arrList);
    }

    public void printUniqueWord() {
        for (String str : hashSet) {
            Utility.printMsg(str);
        }
    }

    public void wordCount() {
     for (String str: arrList) {
          hashMap.put(str, hashMap.containsKey(str)? hashMap.get(str)+1: 1);
      }
        Utility.printMsg("Частота");
        for (String str: hashMap.keySet()
             ) {
           Utility.printMsg(str+" - "+ hashMap.get(str)+" раз");
         }
        }
}

class Utility {
    public static void printMsg(String msg) {
        System.out.println(msg);
    }
}
///////////////////// 

class PhoneBook {
    HashMap<String, String> phonebook=new HashMap<>();
    public void add(String fio, String phonenumber){
        phonebook.put(phonenumber,fio);
    }

    public void get(String fio) {
        boolean findres=false;
      for (String str: phonebook.keySet()) {
        if (phonebook.get(str).equals(fio))
          { Utility.printMsg(fio+" - "+str);
           findres=true;}
        }
      if (!findres) Utility.printMsg("Unknown");
    }
}