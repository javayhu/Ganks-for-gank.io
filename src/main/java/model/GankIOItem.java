package model;

import java.util.ArrayList;
import java.util.List;

/**
 * 周报中的一个item
 * <p>
 * 编号：id <- md5(url)
 * 网址：url
 * 短址：shortUrl
 * 标题：title
 * 类型：type，可能是 article，library，news，video，app，design，tool，screencast，job，sponsored，event等
 * 摘要：summary <- 可能没有，没有就自动提取
 * 来源：source <- android weekly issue #100
 * 内容：content
 * 标签：tags
 * <p>
 * hujiawei 16/4/27
 */
public class GankIOItem {

    private String id;
    private String url;
    private String shortUrl;//-> 不再需要
    private String title;
    private String summary;//-> 不再需要
    private String content;
    private String source;
    private String type;
    private List<String> tags;

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
