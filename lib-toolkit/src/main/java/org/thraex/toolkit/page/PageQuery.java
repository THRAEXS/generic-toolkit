package org.thraex.toolkit.page;

import java.io.Serializable;

/**
 * @author 鬼王
 * @date 2022/03/14 18:37
 */
public class PageQuery implements Serializable {

    private int page;

    private int size;

    public PageQuery() {
        this.page = 0;
        this.size = 10;
    }

    public int getPage() {
        return page;
    }

    public PageQuery setPage(int page) {
        this.page = page;
        return this;
    }

    public int getSize() {
        return size;
    }

    public PageQuery setSize(int size) {
        this.size = size;
        return this;
    }

}
