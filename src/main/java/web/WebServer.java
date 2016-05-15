package web;

import model.GankIOItem;
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

    public static void main(String[] args) {
        CountInfo countInfo = StatTool.count();

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

        //search
        get("/search/:term", (request, response) -> search(request.params(":term")), new JsonTransformer());

    }

    /**
     * 搜索
     *
     * @param term 搜索词
     */
    private static Object search(String term) {
        List<GankIOItem> result = new ArrayList<GankIOItem>();
        List<GankIOItem> itemList = StatTool.getItems();
        for (GankIOItem item : itemList) {
            if (item.getTitle().toLowerCase().contains(term.toLowerCase())) {
                result.add(item);
            }
        }
        return result;
    }

}