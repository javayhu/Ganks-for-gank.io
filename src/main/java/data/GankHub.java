package data;

import model.GankItem;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜索核心控制类，用于构建搜索索引，对外提供了一个针对某个关键词进行搜索的功能
 * <p/>
 * hujiawei 16/5/15
 */
public class GankHub {

    private Logger logger = LoggerFactory.getLogger(GankHub.class);

    private Analyzer analyzer;//分词器
    private Directory directory;//索引内容目录
    private DirectoryReader reader;//索引内容读取器
    private IndexSearcher searcher;//搜索器

    public static final String FIELD_URL = "url";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_SOURCE = "source";
    public static final String FIELD_CONTENT = "content";

    public GankHub() {
        analyzer = new StandardAnalyzer();// Analyzer word segmentation
        directory = new RAMDirectory();// Store the index in memory
    }

    /**
     * 启动服务
     */
    public void startService() {
        GankDataHanlder hanlder = new GankDataHanlder();
        try {
            List<GankItem> items = hanlder.loadGankItems();
            buildSearchIndex(items);
            logger.info("gankhub start service");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 全文检索
     */
    public List<Document> search(String keyword) throws Exception {
        logger.info("gankhub search" + keyword);
        // Parse a simple query that searches for "keyword"
        QueryParser parser = new QueryParser(FIELD_TITLE, analyzer);//默认是基于标题的
        Query query = parser.parse(keyword);

        List<Document> documents = new ArrayList<>();
        TopDocs topdocs = null;
        if ((topdocs = searcher.search(query, 10)) != null) {
            ScoreDoc[] hitdocs = topdocs.scoreDocs;//NullPointerException
            for (ScoreDoc hitdoc : hitdocs) {
                documents.add(searcher.doc(hitdoc.doc));
            }
        }

        return documents;
    }

    /**
     * 停止服务
     */
    public void stopService() {
        try {
            reader.close();
            directory.close();
            logger.info("gankhub stop service");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建搜索索引
     */
    private void buildSearchIndex(List<GankItem> items) throws IOException, ParseException {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(directory, config);

        for (GankItem item : items) {
            Document doc = new Document();
            if (null != item.getUrl()) {
                doc.add(new Field(FIELD_URL, item.getUrl(), TextField.TYPE_STORED));
            }
            if (null != item.getTitle()) {
                doc.add(new Field(FIELD_TITLE, item.getTitle(), TextField.TYPE_STORED));
            }
            if (null != item.getContent()) {
                doc.add(new Field(FIELD_CONTENT, item.getContent(), TextField.TYPE_NOT_STORED));
            }
            if (null != item.getSource()) {
                doc.add(new Field(FIELD_SOURCE, item.getSource(), TextField.TYPE_STORED));
            }
            writer.addDocument(doc);
        }
        writer.close();

        reader = DirectoryReader.open(directory);
        searcher = new IndexSearcher(reader);
    }

    public static void main(String[] args) {
        GankHub gankHub = new GankHub();
        try {
            gankHub.startService();
            gankHub.search("动画");
            gankHub.stopService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
