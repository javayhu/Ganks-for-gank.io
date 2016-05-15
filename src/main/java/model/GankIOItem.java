package model;

import java.util.ArrayList;
import java.util.List;

/**
 * 一条干货
 * <p>
 * hujiawei 16/4/27
 */
public class GankIOItem {

    private String id;//编号
    private String url;//网址
    private String shortUrl;//短网址
    private String title;//标题
    private String summary;//摘要
    private String content;//内容
    private String source;//来源
    private String type;//类型
    private List<String> tags;//标签

    public GankIOItem() {
        tags = new ArrayList<String>();
    }

    @Override
    public String toString() {
        return "GankIOItem{" +
                "tags=" + tags +
                ", type='" + type + '\'' +
                ", source='" + source + '\'' +
                ", content='" + content + '\'' +
                ", summary='" + summary + '\'' +
                ", title='" + title + '\'' +
                ", shortUrl='" + shortUrl + '\'' +
                ", url='" + url + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
