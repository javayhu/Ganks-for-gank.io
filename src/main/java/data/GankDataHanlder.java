package data;

import com.alibaba.fastjson.JSONArray;
import model.GankIssue;
import model.GankItem;
import tool.CommonTool;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * 干货数据处理类
 * <p/>
 * hujiawei 16/5/15
 */
public class GankDataHanlder {

    public static final String DATA_PATH = "src/main/resources/data/";

    //public static final String GANKIO_JSON = "src/main/resources/data/gankio.json";

    public static void main(String[] args) {
        GankDataHanlder hanlder = new GankDataHanlder();
        List<GankItem> items = hanlder.loadGankItems();
        System.out.println(items.size());
    }

    /**
     * 从文件中加载所有的干货数据
     */
    public List<GankItem> loadGankItems() {
        List<GankItem> items = getItems();

        //for (GankIOItem item : items) {
        //    System.out.println(item.getTitle() + " " + item.getUrl());
        //}

        //这里可以做一些其他的处理，例如item的内容处理、item的过滤或者删除内容为空的item等，暂时保留该方法

        return items;
    }

    /**
     * 得到干货的周报或者日报的列表
     */
    public List<GankIssue> getIssues(String filePath) {
        List<GankIssue> issueList = null;
        try {
            issueList = JSONArray.parseArray(CommonTool.getFileContent(new File(filePath)), GankIssue.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return issueList;
    }

    /**
     * 得到所有的item数据
     */
    private List<GankItem> getItems() {
        List<GankItem> itemList = new ArrayList<GankItem>();
        File[] dataFiles = new File(DATA_PATH).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith("json");
            }
        });
        for (File file : dataFiles) {
            List<GankIssue> issueList = getIssues(file.getPath());
            if (issueList != null) {
                for (GankIssue issue : issueList) {
                    itemList.addAll(issue.getItems());
                }
            }
        }

        return itemList;
    }

}
