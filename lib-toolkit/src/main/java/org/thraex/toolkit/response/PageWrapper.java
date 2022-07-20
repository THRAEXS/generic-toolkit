package org.thraex.toolkit.response;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * TODO: Opt
 *
 * @author 鬼王
 * @date 2022/03/16 15:44
 */
public class PageWrapper<T> {

    /**
     * {@link Page#getTotalPages()}
     */
    private int pages;

    /**
     * {@link Page#getTotalElements()}
     */
    private long elements;

    /**
     * {@link Page#getNumber()}
     */
    private int number;

    /**
     * {@link Page#getSize()}
     */
    private int size;

    /**
     * {@link Page#getContent()}
     */
    private List<T> content;

    public PageWrapper() {}

    public PageWrapper(int pages, long elements, int number, int size, List<T> content) {
        this.pages = pages;
        this.elements = elements;
        this.number = number;
        this.size = size;
        this.content = content;
    }

    public int getPages() {
        return pages;
    }

    public PageWrapper<T> setPages(int pages) {
        this.pages = pages;
        return this;
    }

    public long getElements() {
        return elements;
    }

    public PageWrapper<T> setElements(long elements) {
        this.elements = elements;
        return this;
    }

    public int getNumber() {
        return number;
    }

    public PageWrapper<T> setNumber(int number) {
        this.number = number;
        return this;
    }

    public int getSize() {
        return size;
    }

    public PageWrapper<T> setSize(int size) {
        this.size = size;
        return this;
    }

    public List<T> getContent() {
        return content;
    }

    public PageWrapper<T> setContent(List<T> content) {
        this.content = content;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

    public static PageWrapper<?> of(Object data) {
        if (IPage.class.isInstance(data)) {
            IPage<?> page = (IPage<?>) data;
            long pages = page.getPages();
            long elements = page.getTotal();
            long number = page.getCurrent();
            long size = page.getSize();
            List<?> content = page.getRecords();

            return new PageWrapper(Long.valueOf(pages).intValue(), elements, Long.valueOf(number).intValue(), Long.valueOf(size).intValue(), content);
        } else if (Page.class.isInstance(data)) {
            Page<?> page = (Page<?>) data;
            int pages = page.getTotalPages();
            long elements = page.getTotalElements();
            int number = page.getNumber();
            int size = page.getSize();
            List<?> content = page.getContent();

            return new PageWrapper(pages, elements, number, size, content);
        }

        return null;
    }

}
