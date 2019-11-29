package com.ddqiang.hkmanager.filterstrategy;

import java.util.List;

public interface FilterStrategy {
    public boolean isInExcludeExts(String extention, List<String> extsDb);
    public boolean isInContained(String name, List<String> names);
}
