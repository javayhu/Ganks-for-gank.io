import com.ctriposs.sdb.DBConfig;
import com.ctriposs.sdb.SDB;

import java.io.IOException;

/**
 * Hello SessDB!
 * <p>
 * 项目来源：https://github.com/ctriposs/sessdb
 */
public class HelloSessDB {

    // directory to store sessdb data files.
    static String dbDir = "src/main/resources/hello/";

    public static void main(String[] args) {
        // new SessDB with provided DB directory,
        // this may open exiting DB if already exists
        SDB sdb = new SDB(dbDir, DBConfig.SMALL);

        try {
            // put key/value into the DB
            sdb.put("helloKey".getBytes(), "helloValue".getBytes());
            // get value from the DB by key
            byte[] valueBytes = sdb.get("helloKey".getBytes());

            System.out.println("value for helloKey is " + new String(valueBytes));

            // delete key/value from DB by key
            sdb.delete("helloKey".getBytes());

            // get non-exiting or already deleted key/value will get null value
            valueBytes = sdb.get("helloKey".getBytes());
            if (valueBytes == null) {
                System.out.println("helloKey has been deleted");
            }

            // put more key/value pairs
            for (int i = 0; i < 1024; i++) {
                sdb.put(("key" + i).getBytes(), ("value" + i).getBytes());
            }

            // get more key/value pairs
            for (int i = 0; i < 1024; i++) {
                valueBytes = sdb.get(("key" + i).getBytes());
                System.out.println(new String(valueBytes));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // make sure you close the db to shutdown the database
            // and avoid resource leaks.
            try {
                sdb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // reopen exiting DB
        sdb = new SDB(dbDir, DBConfig.SMALL);

        try {
            // get stored key/value pairs
            for (int i = 0; i < 1024; i++) {
                byte[] valueBytes = sdb.get(("key" + i).getBytes());
                System.out.println(new String(valueBytes));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // make sure you close the db to shutdown the database
            // and avoid resource leaks.
            try {
                sdb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // delete all back files of the DB
        // it you don't need the data in DB anymore
        //sdb.destory();

    }
}