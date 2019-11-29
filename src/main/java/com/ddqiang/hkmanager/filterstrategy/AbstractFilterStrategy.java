package com.ddqiang.hkmanager.filterstrategy;

import java.util.List;

public abstract class AbstractFilterStrategy implements FilterStrategy{
    @Override
    public boolean isInExcludeExts(String extention, List<String> extsDb) {
        return false;
    }

    @Override
    public boolean isInContained(String name, List<String> names) {
        return false;
    }
}
