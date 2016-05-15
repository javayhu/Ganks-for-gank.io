package model;

import java.util.ArrayList;
import java.util.List;

/**
 * 干货日报
 * <p>
 * hujiawei 16/4/28
 */
public class GankIOIssue {

    private int num;//编号
    private String id;//id -> 不再需要
    private String url;//网址
    private String title;//标题 -> 不再需要
    private String file;//文件路径 -> 不再需要
    private List<GankIOItem> items;//日报中的item列表

    public GankIOIssue() {
        this.items = new ArrayList<GankIOItem>();
    }

    @Override
    public String toString() {
        return "GankIOIssue{" +
                "num=" + num +
                ", id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", file='" + file + '\'' +
                ", items=" + items +
                '}';
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public List<GankIOItem> getItems() {
        return items;
    }

    public void setItems(List<GankIOItem> items) {
        this.items = items;
    }
}
