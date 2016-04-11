package com.education.smsencrypt.utils.listItemObject;

import android.content.Context;

/**
 * Created by Eno on 3/27/2016.
 */
public class ListItemRecieveSMS {

    private String content;
    private String from;
    private String date;
    private String firtIcon;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    private Context context;

    public String getFirtIcon() {
        return firtIcon;
    }

    public void setFirtIcon(String firtIcon) {
        this.firtIcon = firtIcon;
    }

    public ListItemRecieveSMS(Context context) {
        this.context = context;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
