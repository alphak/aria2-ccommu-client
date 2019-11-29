package com.ddqiang.hkmanager.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {
    public final static Logger logger = LoggerFactory.getLogger(Utils.class);

    public static String getFileExtention(String filename){
        if(filename == null || "".equals(filename.trim()))
            return "";
        String [] tmp= filename.trim().split("\\\\");
        String name = tmp[tmp.length-1];
        return name.substring(name.lastIndexOf("."));
    }

    public static String getFilenameWithExt(String filename){
        if(filename == null || "".equals(filename.trim()))
            return "";
        String [] tmp= filename.trim().split("\\\\");
        String name = tmp[tmp.length-1];
        return name;
    }

    public static String getFilenameWithOutExt(String filename){
        if(filename == null || "".equals(filename.trim()))
            return "";
        String [] tmp= filename.trim().split("\\\\");
        String name = tmp[tmp.length-1];
        return name.substring(0,name.lastIndexOf("."));
    }

    public static String normalize(String fileName) {
        if(fileName == null || "".equals(fileName)){
            return "";
        }
        String ret = fileName.replaceFirst("^.*@+", "");
        ret = ret.replaceFirst("^[0-9]+\\.+", "hjd2048.com-666");
        ret = ret.replaceFirst("^\\[FHD\\]+", "");
        ret = ret.replaceFirst("^hjd2048.com[-_][0-9]{0,4}", "");
        ret = ret.replaceFirst("^[0-9]+MAAN", "MAAN");
        ret = ret.replaceFirst("^[0-9]+MIUM", "MIUM");
        ret = ret.replaceFirst("^[0-9]+ORETD", "ORETD");
        ret = ret.replaceFirst("^[0-9]+ARA", "ARA");
        ret = ret.replaceFirst("^[0-9]+ETQT", "ETQT");
        ret = ret.replaceFirst("^[0-9]+EVA", "EVA");
        ret = ret.replaceFirst("^[0-9]+GANA", "GANA");
        ret = ret.replaceFirst("^[0-9]+KIRAY", "KIRAY");
        ret = ret.replaceFirst("^[0-9]+KITAIKE", "KITAIKE");
        ret = ret.replaceFirst("^[0-9]+LUXU", "LUXU");
        ret = ret.replaceFirst("^[0-9]+MANE", "MANE");
        ret = ret.replaceFirst("^[0-9]+ORE", "ORE");
        ret = ret.replaceFirst("^[0-9]+NTK", "NTK");
        ret = ret.replaceFirst("^[0-9]+OREC", "OREC");
        ret = ret.replaceFirst("^[0-9]+OREGR", "OREGR");
        ret = ret.replaceFirst("^[0-9]+ORETD", "ORETD");
        ret = ret.replaceFirst("^[0-9]+OREX", "OREX");
        ret = ret.replaceFirst("^[0-9]+SIMM", "SIMM");
        ret = ret.replaceFirst("^[0-9]+STRO", "STRO");
        ret = ret.replaceFirst("^[0-9]+PER", "PER");
        ret = ret.replaceFirst("^[0-9]+HEN", "HEN");
        ret = ret.replaceFirst("^[0-9]+URE", "URE");
        ret = ret.replaceFirst("^[0-9]+URF", "URF");
        ret = ret.replaceFirst("^[0-9]+UTSU", "UTSU");
        ret = ret.replaceFirst("^[0-9]+KNB", "KNB");
        ret = ret.replaceFirst("^[0-9]+OPER", "OPER");
        ret = ret.replaceFirst("^[0-9]+MMGH", "MMGH");
        return ret;
    }
}
