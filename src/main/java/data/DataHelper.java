package data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 数据辅助工具类
 * <p>
 * hujiawei 16/4/27
 */
public class DataHelper {

    static Logger logger = LoggerFactory.getLogger(DataHelper.class);

    public static final String GANKIO_JSON = "src/main/resources/data/gankio.json";
    public static final String GANKIO_EXCEL = "src/main/resources/data/gankio.xlsx";

    /**
     * 执行dragnetTool.py脚本获取指定url网页中的内容
     * 脚本中设置了10秒的timeout，如果不设置会导致有些请求耗时太长，甚至有些会停止
     * <p>
     * 本地需要安装dragnet: https://pypi.python.org/pypi/dragnet
     * <p>
     * pip install numpy
     * pip install --upgrade cython
     * pip install lxml
     * pip install dragnet
     *
     * @param url 指定网页
     */
    public static String extractContent(String url) throws IOException {
        logger.info("Extract content from url: " + url);
        String result = null;
        // using the Runtime exec method
        Runtime runtime = Runtime.getRuntime();
        File file = new File("src/main/java/data/dragnetTool.py");
        //logger.info(file.getAbsoluteFile().toString());
        Process p = runtime.exec("python " + file.getAbsolutePath() + " " + url);

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

        // read the output from the command
        logger.info("Here is the standard output of the command:");
        if ((result = stdInput.readLine()) != null) {
            logger.info(result);
            return result;//TODO --> result的后续处理？ \u00A0 不间断空格
        }

        //issus#103
        //http://www.pawegio.com/2014/05/20/sending-android-logs-with-live-templates-in-intellij-idea-and-android-studio/

        // read any errors from the attempted command
        logger.info("Here is the standard error of the command (if any):");
        while ((result = stdError.readLine()) != null) {
            logger.error(result);
            //result = null;//如果获取内容出错的话，就返回null
        }
        return null;
    }

    /**
     * 判断是否需要忽略该url --> 提取出来，放在文件中进行配置
     * <p>
     * 有些网址无法访问，有些访问时会被重定向。下面列举的域名是从andrdoidweekly.net中保留下来的
     * <p>
     * antonioleiva.com 可能有结果，可能出错，也可能会导致解析停止？ eg. http://antonioleiva.com/collapsing-toolbar-layout/
     * https://www.androidexperiments.com/ 只有一个android logo
     * http://www.donnfelker.com/hi-performance-json-parsing-in-android/ 403 Forbidden 403 Forbidden nginx
     * <p>
     * requests.exceptions.ConnectionError: HTTPConnectionPool(host='johnpetitto.com', port=80): Max retries exceeded with url: /no-more-realm/ (Caused by NewConnectionError('<requests.packages.urllib3.connection.HTTPConnection object at 0x106014190>: Failed to establish a new connection: [Errno 8] nodename nor servname provided, or not known',))
     * requests.exceptions.ConnectionError: HTTPSConnectionPool(host='medium.com', port=443): Max retries exceeded with url: /m/global-identity?redirectUrl=https%3A%2F%2Fblog.prototypr.io%2Fmotion-design-is-the-future-of-ui-fc83ce55c02f (Caused by NewConnectionError('<requests.packages.urllib3.connection.VerifiedHTTPSConnection object at 0x106115990>: Failed to establish a new connection: [Errno 60] Operation timed out',))
     *
     * @param url url
     */
    public static boolean isIgnoredUrl(String url) {
        String[] urls = new String[]{"google.com", "youtube.com", "facebook.com", "twitter.com", "blogspot", "goo.gl",
                "medium.com", "wordpress.com", "antonioleiva.com", "tools.android.com", "developer.android.com"};
        for (String s : urls) {
            if (url.contains(s)) return true;
        }
        return false;
    }

    /**
     * 判断是否需要忽略这种类型的item
     * <p>
     * 有些类型的item并不需要提取出来
     *
     * @param type 类型
     */
    public static boolean isIgnoredType(String type) {
        String[] types = new String[]{"休息视频", "福利"};
        for (String s : types) {
            if (type.contains(s)) return true;
        }
        return false;
    }

    /**
     * 保存网页内容
     *
     * @param fileName 保存的文件
     * @param content  页面的内容
     */
    public static void savePage(String fileName, byte[] content) {
        File file = new File(fileName);
        if (file.exists()) {
            return;//文件如果存在的话那就不保存了
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(content);
            logger.info("成功保存文档 " + fileName);
        } catch (IOException e) {
            logger.info("保存文档失败 " + fileName);
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.flush();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            //System.out.println(isIgnoredType("Videos & Podcasts"));
            //System.out.println(isIgnoredType("Jobs"));
            //System.out.println(isIgnoredType("Books"));
            //System.out.println(isIgnoredUrl("https://google.com/"));

            System.out.println(DataHelper.extractContent("https://github.com/seomoz/dragnet"));
            //System.out.println(DataHelper.extractContent("https://moz.com/devblog/dragnet-content-extraction-from-diverse-feature-sets/"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
