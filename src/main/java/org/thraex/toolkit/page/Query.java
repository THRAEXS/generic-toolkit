package org.thraex.toolkit.page;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

/**
 * @author 鬼王
 * @date 2020/08/25 11:37
 */
public class Query implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String DEFAULT_KEY = "keywords";

    private long page;
    private long size;
    private Map<String, Object> params;

    public Query() {
        this.page = 1;
        this.size = 10;
    }

    public Query(long page, long size) {
        this.page = page < 1 ? 1 : page;
        this.size = size < 1 ? 10 : size;
    }

    public long getPage() {
        return page;
    }

    public Query setPage(long page) {
        this.page = page < 1 ? 1 : page;
        return this;
    }

    public long getSize() {
        return size;
    }

    public Query setSize(long size) {
        this.size = size < 1 ? 10 : size;
        return this;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public Query setParams(Map<String, Object> params) {
        this.params = params;
        return this;
    }

    /*public <T> T parse(Class<T> clazz) {
        return Optional.ofNullable(JSON.parseObject(JSON.toJSONString(this.params), clazz)).orElseGet(() -> {
            T t = null;
            try {
                t = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return t;
        });
    }*/

    public Object getValue() {
        return getValue(DEFAULT_KEY);
    }

    public Object getValue(String key) {
        return Optional.ofNullable(params).map(it -> it.get(key)).orElse(null);
    }

}
