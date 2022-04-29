package uk.tees.b1162802.boro.data.model;

public class News {
    String newsTitle;
    String newsUrl;

    public News(String newsTitle, String newsUrl) {
        this.newsTitle = newsTitle;
        this.newsUrl = newsUrl;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }
}
