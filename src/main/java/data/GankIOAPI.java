package data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import model.GankIssue;
import model.GankItem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * gank.io api请求类
 * <p/>
 * gank.io具有api访问接口，所以不用去爬取和解析网页，直接调用api即可，得到的数据会保存到json文件中
 * <p/>
 * hujiawei 16/5/15
 */
public class GankIOAPI {

    static Logger logger = LoggerFactory.getLogger(GankIOAPI.class);

    private static final String API_DATES = "http://gank.io/api/day/history";//发过干货的日期
    private static final String API_GANKS = "http://gank.io/api/day/";//某日的数据
    private static final String URL_DATE = "http://gank.io/";

    public static void main(String[] args) {
        GankIOAPI gankIOAPI = new GankIOAPI();
        try {
            logger.info("load issues from gank.io");
            List<GankIssue> issues = gankIOAPI.loadGankIOIssues();
            gankIOAPI.loadGankIOItems(issues);

            logger.info("write issues to json file");
            JSON.writeJSONStringTo(issues, new FileWriter(DataHelper.GANKIO_JSON), SerializerFeature.PrettyFormat);

            //logger.info("write issues to excel file");//数据量过大会导致保存失败，所以如果出错则取消保存到excel文件中
            //writeItems2Excel(issues, DataHelper.GANKIO_EXCEL);//写入到excel表中，便于查看

        } catch (UnirestException | IOException e) {
            e.printStackTrace();
        }
    }

    //获取所有GankIOIssue下的所有GankIOItem
    private void loadGankIOItems(List<GankIssue> issues) throws UnirestException {
        for (GankIssue issue : issues) {
            issue.setItems(loadGankIOItemsWithIssue(issue));
        }
    }

    //加载某个GankIOIssue下的GankIOItem
    private List<GankItem> loadGankIOItemsWithIssue(GankIssue issue) throws UnirestException {
        logger.info("load items from issue: " + issue.getTitle());
        List<GankItem> items = new ArrayList<GankItem>();
        String urlSuffix = issue.getId().replaceAll("-", "/");
        HttpResponse<JsonNode> jsonResponse = Unirest.get(API_GANKS + urlSuffix).asJson();
        JSONObject jsonObject = jsonResponse.getBody().getObject();
        JSONArray categories = jsonObject.getJSONArray("category");
        JSONObject results = jsonObject.getJSONObject("results");
        for (int i = 0; i < categories.length(); i++) {
            JSONArray array = results.getJSONArray(categories.getString(i));
            for (int j = 0; j < array.length(); j++) {
                GankItem item = parseItem(array.getJSONObject(j), issue);
                if (null != item) items.add(item);
            }
        }
        return items;
    }

    //解析item json得到GankIOItem对象
    private GankItem parseItem(JSONObject jsonObject, GankIssue issue) {
        GankItem item = new GankItem();
        item.setType(jsonObject.getString("type"));
        if (DataHelper.isIgnoredType(item.getType())) {
            return null;
        }
        item.setUrl(jsonObject.getString("url"));
        if (DataHelper.isIgnoredUrl(item.getUrl())) {
            return null;
        }
        item.setId(jsonObject.getString("_id"));
        item.setTitle(jsonObject.getString("desc"));
        item.setSource(issue.getTitle());
        try {
            item.setContent(DataHelper.extractContent(item.getUrl()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("add item: " + item.getTitle());
        return item;
    }

    //加载所有的GankIOIssue
    private List<GankIssue> loadGankIOIssues() throws UnirestException {
        List<GankIssue> issues = new ArrayList<GankIssue>();
        HttpResponse<JsonNode> jsonResponse = Unirest.get(API_DATES).asJson();
        JSONObject jsonObject = jsonResponse.getBody().getObject();
        JSONArray results = jsonObject.getJSONArray("results");

        int len = results.length();
        for (int i = 0; i < len; i++) {
            issues.add(parseIssue(results.getString(i), len - i));
        }
        return issues;
    }

    //由日期来生成一个GankIOIssue对象
    private GankIssue parseIssue(String date, int num) {
        GankIssue issue = new GankIssue();
        issue.setId(date);//id和title也可以不设置，这里将日期作为id，自定义一个名称作为title
        issue.setNum(num);//第多少期
        String urlSuffix = date.replaceAll("-", "/");
        issue.setUrl(URL_DATE + urlSuffix);//访问地址
        issue.setTitle("Gank.io #" + num + " (" + date + ")");//Gank.io #2 (2015-03-12)
        logger.info("add issue: " + issue.getTitle());
        return issue;
    }

    /**
     * 将weekly item写入到excel文件
     *
     * @param issues   周报列表
     * @param filePath excel文件路径
     */
    private static void writeItems2Excel(List<GankIssue> issues, String filePath) throws IOException {
        List<GankItem> items = new ArrayList<GankItem>();
        for (GankIssue issue : issues) {
            items.addAll(issue.getItems());
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("gankio");
        String[] heads = new String[]{"id", "source", "type", "title", "shorturl", "tags", "url", "summary"};//"content"
        Row headRow = sheet.createRow(0);

        for (int i = 0; i < heads.length; i++) {
            Cell cell = headRow.createCell(i, Cell.CELL_TYPE_STRING);
            cell.setCellValue(heads[i]);
        }

        for (int rowNum = 1; rowNum <= items.size(); rowNum++) {
            GankItem item = items.get(rowNum - 1);
            Row row = sheet.createRow(rowNum);
            for (int i = 0; i < heads.length; i++) {
                Cell cell = row.createCell(i, Cell.CELL_TYPE_STRING);
                switch (i) {
                    case 0:
                        cell.setCellValue(item.getId());
                        break;
                    case 1:
                        cell.setCellValue(item.getSource());
                        break;
                    case 2:
                        cell.setCellValue(item.getType());
                        break;
                    case 3:
                        cell.setCellValue(item.getTitle());
                        break;
                    case 4:
                        cell.setCellValue(item.getShortUrl());
                        break;
                    case 5:
                        cell.setCellValue(item.getTags().toString());
                        break;
                    case 6:
                        cell.setCellValue(item.getUrl());
                        break;
                    case 7:
                        cell.setCellValue(item.getSummary());
                        break;
                    //case 8:
                    //    cell.setCellValue(item.getContent());
                    //    break;
                }
            }
        }

        for (int i = 0; i < heads.length; i++) {
            sheet.autoSizeColumn(i);
        }

        FileOutputStream outputStream = new FileOutputStream(filePath);
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

}
