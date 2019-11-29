package com.ddqiang.hkmanager.cache;

public class BaseFileItem {
    boolean downloaded;

    public BaseFileItem(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public BaseFileItem() {
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }
}
