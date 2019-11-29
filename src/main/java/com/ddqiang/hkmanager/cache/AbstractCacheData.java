package com.ddqiang.hkmanager.cache;

import java.util.List;

public abstract class AbstractCacheData implements CacheData{
    private List<String> excludeExts;

    private List<String> excludeNames;

    private List<String> existsFileNames;

    @Override
    public void init() {

    }

    public List<String> getExcludeExts() {
        return excludeExts;
    }

    public List<String> getExcludeNames() {
        return excludeNames;
    }

    public List<String> getExistsFileNames() {
        return existsFileNames;
    }

    public void setExcludeExts(List<String> excludeExts) {
        this.excludeExts = excludeExts;
    }

    public void setExcludeNames(List<String> excludeNames) {
        this.excludeNames = excludeNames;
    }

    public void setExistsFileNames(List<String> existsFileNames) {
        this.existsFileNames = existsFileNames;
    }
}
