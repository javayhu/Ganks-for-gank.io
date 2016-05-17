package web;

import data.GankHub;
import model.GankItem;
import org.apache.lucene.document.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import tool.CountInfo;
import tool.JsonTransformer;
import tool.StatTool;
import tool.VelocityTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

/**
 * web服务
 */
public class WebServer {

    private Logger logger = LoggerFactory.getLogger(WebServer.class);
    private GankHub gankHub;

    public static void main(String[] args) {
        WebServer server = new WebServer();
        server.startServer();
    }

    /**
     * 启动服务器
     */
    private void startServer() {
        CountInfo countInfo = StatTool.count();

        //设置端口
        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }
        //web
        staticFileLocation("/public");

        //index page
        get("/", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("stat", countInfo);

            // The vm files are located under the resources directory
            return new ModelAndView(model, "public/html/index.vm");
        }, new VelocityTemplateEngine());

        //search action
        post("/search", (request, response) -> search(request.queryParams("keyword")), new JsonTransformer());

        gankHub = new GankHub();
        gankHub.startService();

        logger.info("server started");
    }

    /**
     * 搜索
     *
     * @param keyword 搜索词
     */
    private Object search(String keyword) {
        logger.info("search:" + keyword);
        List<GankItem> gankItems = new ArrayList<GankItem>();
        try {
            List<Document> documents = gankHub.search(keyword);
            documents.forEach(document -> {
                GankItem item = new GankItem();
                item.setTags(null);
                item.setUrl(document.getField(GankHub.FIELD_URL).stringValue());
                item.setTitle(document.getField(GankHub.FIELD_TITLE).stringValue());
                item.setSource(document.getField(GankHub.FIELD_SOURCE).stringValue());
                gankItems.add(item);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return gankItems;
    }

    //如何关闭呢

}