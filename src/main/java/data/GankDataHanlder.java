package data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import model.GankIssue;
import model.GankItem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.CommonTool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 干货数据处理类，处理缓存，处理更新，处理保存到json或者excel文件
 * <p/>
 * hujiawei 16/5/15
 */
public class GankDataHanlder {

    static Logger logger = LoggerFactory.getLogger(GankDataHanlder.class);

    public static void main(String[] args) {
        GankDataHanlder hanlder = new GankDataHanlder();
        List<GankItem> items = null;
        try {
            items = hanlder.loadGankItems();
            System.out.println("gank items count=" + items.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从文件中加载所有的干货数据
     */
    public List<GankItem> loadGankItems() throws Exception {
        List<GankIssue> localIssues = getIssues();//得到本地的干货日报列表
        GankIOAPI gankIOAPI = new GankIOAPI();
        List<GankIssue> remoteIssues = gankIOAPI.loadGankIOIssues();//远端的最新的日报列表

        //远端的日报列表有更新了，那么就加载新的数据并添加到本地缓存的数据中，最后再保存起来
        if (localIssues != null && remoteIssues != null && remoteIssues.size() > localIssues.size()) {
            logger.info("remote issues list update");
            List<GankIssue> newIssues = new ArrayList<>();
            for (GankIssue issue : remoteIssues) {
                if (!localIssues.contains(issue)) {
                    newIssues.add(issue);
                }
            }

            logger.info("load updated issues " + newIssues.toString());
            gankIOAPI.loadGankIOItems(newIssues);

            //本地的加上新获取的 
            Collections.reverse(newIssues);//先倒序一下
            localIssues.addAll(newIssues);

            logger.info("write issues to json file");
            JSON.writeJSONStringTo(localIssues, new FileWriter(DataHelper.GANKIO_JSON), SerializerFeature.PrettyFormat);

            //logger.info("write issues to excel file");//数据量过大会导致保存失败，所以如果出错则取消保存到excel文件中
            //writeItems2Excel(issues, DataHelper.GANKIO_EXCEL);//写入到excel表中，便于查看
        }
        //这里可以做一些其他的处理，例如item的内容处理、item的过滤或者删除内容为空的item等，暂时保留该方法

        return getItems(localIssues);
    }

    /**
     * 得到本地的干货的日报的列表
     */
    public List<GankIssue> getIssues() throws Exception {
        File file = new File(DataHelper.GANKIO_JSON);
        return JSONArray.parseArray(CommonTool.getFileContent(file), GankIssue.class);
    }

    /**
     * 得到所有的item数据
     */
    private List<GankItem> getItems(List<GankIssue> issueList) throws Exception {
        List<GankItem> itemList = new ArrayList<GankItem>();
        if (issueList != null) {
            for (GankIssue issue : issueList) {
                itemList.addAll(issue.getItems());
            }
        }
        return itemList;
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
