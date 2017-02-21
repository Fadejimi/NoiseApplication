package com.example.adegbulugbe.noiseapplication.model;

/**
 * Created by Fadejimi on 1/14/17.
 */

public class NavDrawerItem {
    private boolean showNotify;
    private String title;

    public NavDrawerItem() {

    }

    public NavDrawerItem(boolean showNotify, String title) {
        this.showNotify = showNotify;
        this.title = title;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    public boolean isShowNotify(){
        return showNotify;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
