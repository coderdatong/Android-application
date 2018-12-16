package top.thanks_code.bookcircle.bean;

import java.io.Serializable;

/**
 * 多图选择的图片对象
 */
public class ImageItem implements Serializable {
    private String sourcePath;

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }
}
