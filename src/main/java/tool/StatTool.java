package tool;

import com.alibaba.fastjson.JSONArray;
import data.DataHelper;
import model.GankIssue;
import model.GankItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Statistics统计工具
 * <p>
 * hujiawei 16/4/28
 */
public class StatTool {

    static Logger logger = LoggerFactory.getLogger(StatTool.class);

    public static void main(String[] args) {
        try {
            StatTool.count();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 统计周报中各种类型item的数量
     */
    public static CountInfo count() {
        List<GankIssue> issueList = getIssues();

        HashMap<String, Integer> map = new HashMap<String, Integer>();
        CountInfo countInfo = new CountInfo();
        if (issueList != null) {
            for (GankIssue issue : issueList) {
                //logger.info(issue.getTitle() + " " + issue.getItems().size());
                countInfo.setItemCount(countInfo.getItemCount() + issueList.size());
                for (GankItem item : issue.getItems()) {
                    if (map.containsKey(item.getType())) {
                        map.put(item.getType(), map.get(item.getType()) + 1);
                    } else {
                        map.put(item.getType(), 1);
                    }
                    if (null == item.getContent() || "".equalsIgnoreCase(item.getContent().trim())) {
                        countInfo.setEmptyItemCount(countInfo.getEmptyItemCount() + 1);
                    }
                }
            }
        }

        logger.info(map.toString());//{App=124, 拓展资源=179, 前端=86, 瞎推荐=166, iOS=1339, Android=1341}
        countInfo.setAndroidCount(map.get("Android"));
        countInfo.setIosCount(map.get("iOS"));
        countInfo.setAppCount(map.get("App"));
        countInfo.setOtherCount(map.get("前端")+map.get("拓展资源")+map.get("瞎推荐"));
        logger.info(countInfo.toString());//
        //CountInfo{itemCount=56169, emptyItemCount=114, androidCount=1341, iosCount=1339, appCount=124, otherCount=431}

        return countInfo;
    }

    /**
     * 得到周报列表
     */
    public static List<GankIssue> getIssues() {
        List<GankIssue> issueList = null;
        try {
            issueList = JSONArray.parseArray(CommonTool.getFileContent(new File(DataHelper.GANKIO_JSON)), GankIssue.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return issueList;
    }

    /**
     * 得到所有的item数据
     */
    public static List<GankItem> getItems() {
        List<GankItem> itemList = new ArrayList<GankItem>();
        List<GankIssue> issueList = getIssues();
        if (issueList != null) {
            for (GankIssue issue : issueList) {
                itemList.addAll(issue.getItems());//.stream().collect(Collectors.toList())
            }
        }
        return itemList;
    }

}
