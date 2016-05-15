package tool;

/**
 * 各种类型的item的数量信息，StatTool的count方法返回的结果
 *
 * @author hujiawei
 */
public class CountInfo {
    private int itemCount = 0;//item总数
    private int emptyItemCount = 0;//内容为空的item总数
    private int androidCount = 0;//android文章数目
    private int iosCount = 0;//ios文章数目
    private int appCount = 0;//前端文章数目
    private int otherCount = 0;//瞎推荐和扩展资源，包括前端文章

    @Override
    public String toString() {
        return "CountInfo{" +
                "itemCount=" + itemCount +
                ", emptyItemCount=" + emptyItemCount +
                ", androidCount=" + androidCount +
                ", iosCount=" + iosCount +
                ", appCount=" + appCount +
                ", otherCount=" + otherCount +
                '}';
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getEmptyItemCount() {
        return emptyItemCount;
    }

    public void setEmptyItemCount(int emptyItemCount) {
        this.emptyItemCount = emptyItemCount;
    }

    public int getAndroidCount() {
        return androidCount;
    }

    public void setAndroidCount(int androidCount) {
        this.androidCount = androidCount;
    }

    public int getIosCount() {
        return iosCount;
    }

    public void setIosCount(int iosCount) {
        this.iosCount = iosCount;
    }

    public int getAppCount() {
        return appCount;
    }

    public void setAppCount(int appCount) {
        this.appCount = appCount;
    }

    public int getOtherCount() {
        return otherCount;
    }

    public void setOtherCount(int otherCount) {
        this.otherCount = otherCount;
    }
}