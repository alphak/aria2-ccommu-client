package com.ddqiang.hkmanager.cache;

import com.ddqiang.hkmanager.utils.Utils;
import com.turn.ttorrent.common.TorrentFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileItem extends BaseFileItem {
    public final static Logger logger = LoggerFactory.getLogger(FileItem.class);

    private String fileName;
    private String fileNameNoExt;
    private long fileSize;
    private FileCategory fileCategory;
    private String extention;

    public FileItem(boolean downloaded, String fileName, String fileNameNoExt, long fileSize,
                    FileCategory fileCategory, String extention) {
        super(downloaded);
        this.fileName = fileName;
        this.fileNameNoExt = fileNameNoExt;
        this.fileSize = fileSize;
        this.fileCategory = fileCategory;
        this.extention = extention;
    }

    public FileItem(TorrentFile tf){
        super(false);
        String file_name_with_path = tf.getRelativePathAsString().toLowerCase();
        this.fileName = Utils.normalize(Utils.getFilenameWithExt(file_name_with_path));
        this.fileNameNoExt = Utils.normalize(Utils.getFilenameWithOutExt(file_name_with_path));
        this.fileCategory = null;
        this.fileSize = tf.size/FILE_CONSTANT.MEGA_BYTE_SIZE;
        this.extention = Utils.getFileExtention(file_name_with_path);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileNameNoExt() {
        return fileNameNoExt;
    }

    public void setFileNameNoExt(String fileNameNoExt) {
        this.fileNameNoExt = fileNameNoExt;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public FileCategory getFileCategory() {
        return fileCategory;
    }

    public void setFileCategory(FileCategory fileCategory) {
        this.fileCategory = fileCategory;
    }

    public String getExtention() {
        return extention;
    }

    public void setExtention(String extention) {
        this.extention = extention;
    }
}
