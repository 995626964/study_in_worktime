import java.util.ArrayList;
import java.util.List;

public class ListTest {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);

//        list.forEach(item->{
//            list.remove(item);
//        });

//        for(int i=0;i<list.size();i++){
//            list.remove(i);
//        }
        Integer n = 2;
        list.remove(n);

        System.out.println(list);
    }

}
