package tool;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;

/**
 * 公用功能工具类
 * <p>
 * hujiawei 16/4/27
 */
public class CommonTool {

    /**
     * 根据url生成编号id，这里只是简单加密下得到对应的字符串
     *
     * @param url url
     */
    public static String generatId(String url) {
        return DigestUtils.md5Hex(url);
    }

    /**
     * 将毫秒数转成具体的时间值
     *
     * @param mss 毫秒数
     */
    public static String formatTime(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        return days + " days " + hours + " hours " + minutes + " minutes " + seconds + " seconds ";
    }

    /**
     * 得到指定文件的内容，返回字符串
     *
     * @param file 文件
     */
    public static String getFileContent(File file) throws Exception {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        reader.close();
        return builder.toString();
    }

    /**
     * 保存文本到文件中
     *
     * @param content 文本内容
     * @param path    文件路径
     */
    public static void saveContent(String content, String path) throws Exception {
        File file = new File(path);
        if (!file.exists()) file.createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(content);
        writer.flush();
        writer.close();
    }

    public static void main(String[] args) {
        System.out.println(CommonTool.generatId("https://www.youtube.com/watch?v=PS-4VZi750s"));//c24aa80484e6c78bb2e86c0e63847bf7
    }

}
