import java.util.Arrays;
import java.util.HashMap;

/**
 * hujiawei 16/4/28
 */
public class SimpleTest {

    public static void main(String[] args) {

        //字符串 常量池
        String str = "hello";
        String str2 = str;
        str2 = "world";

        System.out.println(str + " " + str2);

        //数组 引用类型
        int[] a = new int[]{1, 2, 3, 4};
        int[] b = a;
        b[0] = 5;

        System.out.println(Arrays.toString(a) + " " + Arrays.toString(b));

        //hashmap 引用类型
        HashMap map = new HashMap<String, String>();
        map.put("hello", "world");
        map.put("made", "china");
        HashMap map2 = map;
        map2.put("hello", "java");

        System.out.println(map + " " + map2);

        //ArrayList<String> list = new ArrayList<>();
        //list.add("hello");
        //list.add("world");
        //list.add("china");
        //
        //for (int i = 0; i < list.size(); i++) {
        //    String s = list.get(i);
        //    if (s.equalsIgnoreCase("world")) {
        //        s = "java";//修改是没有用的
        //        list.set(i, s);//必须加上这句才算修改
        //    }
        //}
        //
        //for (String s : list) {
        //    System.out.println(s);
        //}

    }

}
