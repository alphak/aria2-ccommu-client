package com.ddqiang.hkmanager.filterstrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultFilterStrategy extends AbstractFilterStrategy{
    private final static Logger logger = LoggerFactory.getLogger(DefaultFilterStrategy.class);

    @Override
    public boolean isInExcludeExts(String extention, List<String> extsDb) {
        if (extention == null || "".equals(extention) || extsDb == null || extsDb.size() == 0) {
            return false;
        }

        boolean ret = false;
        for (String e :
                extsDb) {
            if (e.toLowerCase().equals(extention.toLowerCase())) {
                ret = true;
                break;
            }
        }

        return ret;
    }

    @Override
    public boolean isInContained(String name, List<String> names){
        if (name == null || "".equals(name) || names == null || names.size() == 0) {
            return false;
        }

        boolean ret = false;
        for (String searchStr:
             names) {
            if(name.toLowerCase().contains(searchStr.toLowerCase())){
                ret = true;
                break;
            }
        }

        return ret;
    }
}
