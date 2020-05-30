package swu.xl.parsexml;

public class WebItem {
    private int id;
    private String url;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "WebItem{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
