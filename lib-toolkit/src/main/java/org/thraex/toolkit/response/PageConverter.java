package org.thraex.toolkit.response;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.util.ClassUtils;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author 鬼王
 * @date 2022/07/23 14:32
 */
public enum PageConverter {

    SPRING_DATA("org.springframework.data.domain.Page", data -> {
        Page<?> page = (Page<?>) data;
        int pages = page.getTotalPages();
        long elements = page.getTotalElements();
        int number = page.getNumber();
        int size = page.getSize();
        List<?> content = page.getContent();

        return new PageWrapper(pages, elements, number, size, content);
    }),
    MYBATIS_PLUS("com.baomidou.mybatisplus.core.metadata.IPage", data -> {
        IPage<?> page = (IPage<?>) data;
        long pages = page.getPages();
        long elements = page.getTotal();
        long number = page.getCurrent();
        long size = page.getSize();
        List<?> content = page.getRecords();

        return new PageWrapper((int) pages, elements, (int) number, (int) size, content);
    }),
    PAGE_HELPER("com.github.pagehelper.PageInfo", data -> {
        PageInfo<?> page = (PageInfo<?>) data;
        int pages = page.getPages();
        long elements = page.getTotal();
        int number = page.getPageNum();
        int size = page.getPageSize();
        List<?> content = page.getList();

        return new PageWrapper<>(pages, elements, number, size, content);
    });

    private final String name;

    private final Function<Object, PageWrapper<?>> converter;

    PageConverter(String name, Function<Object, PageWrapper<?>> converter) {
        this.name = name;
        this.converter = converter;
    }

    public static PageWrapper<?> to(Object data) {
        // TODO: Optimization
        Predicate<String> present = c -> ClassUtils.isPresent(c, null);
        if (present.test(SPRING_DATA.name) && Page.class.isInstance(data)) {
            return SPRING_DATA.converter.apply(data);
        } else if (present.test(MYBATIS_PLUS.name) && IPage.class.isInstance(data)) {
            return MYBATIS_PLUS.converter.apply(data);
        } else if (present.test(PAGE_HELPER.name) && PageInfo.class.isInstance(data)) {
            return PAGE_HELPER.converter.apply(data);
        }

        return null;
    }

}
