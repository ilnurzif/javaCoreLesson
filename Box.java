import java.util.ArrayList;
import java.util.List;

public class Box<T extends Fruit> implements Comparable<Box>{
    private List<T> fruitList;

    public Box() {
        fruitList = new ArrayList();
    }

    public void add(T fruit) {
        fruitList.add(fruit);
    }

    public void add(T fruit, int count) {
        for (int i = 0; i < count; i++) {
            fruitList.add(fruit);
        }
    }

    public boolean compare(Box box) {
        return compareTo(box) == 0;
    }

    public void intersperse(Box<T> box) {
        for (T fruit:
              fruitList) {
           box.add(fruit);
        }
        fruitList.clear();
    }

    public float getWeight() {
        float sum = 0;
        for (T fruit :
                fruitList) {
            sum += fruit.getWeight();
        }
        return sum;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < fruitList.size()-1; i++) {
            sb.append("["+i+"],");
        }
        sb.append("["+(fruitList.size()-1)+"]");

        return "Box{" +fruitList.get(0).getClass().getName()+
                " ={" + sb.toString() +
                '}';
    }

    @Override
    public int compareTo(Box box) {
        float eps=  0.001f;
        return box.getWeight()-getWeight()<=eps?0:1;
    }

}
