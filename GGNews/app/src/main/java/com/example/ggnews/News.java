package com.example.ggnews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class News {

    @SerializedName("id")
    private String id;
    @SerializedName("ctime")
    private String ctime;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("source")
    private String source;
    @SerializedName("picUrl")
    private String picUrl;
    @SerializedName("url")
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPicUrl() {
        if (picUrl.equals(""))
        return "https://th.bing.com/th/id/R.d673952007be76b266aa965c5de5c5c2?rik=NZBSmmQG7vuV1g&riu=http%3a%2f%2fwww.kuaipng.com%2fUploads%2fpic%2fwater%2f13450%2fgoods_water_13450_698_698_.png&ehk=pEIvhJ7NBlyPvRbGLwivy%2bONiD%2bFQ6Xerwx5qHNP9iM%3d&risl=&pid=ImgRaw&r=0";
        else if(!picUrl.startsWith("http")) return "https://" + picUrl;
        else return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getContentUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
