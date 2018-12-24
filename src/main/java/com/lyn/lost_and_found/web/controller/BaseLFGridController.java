package com.lyn.lost_and_found.web.controller;

import com.jay.vito.common.util.string.StringUtil;
import com.jay.vito.storage.domain.BaseEntity;
import com.jay.vito.storage.model.Condition;
import com.jay.vito.storage.model.Page;
import com.jay.vito.uic.client.core.UserContextHolder;
import com.jay.vito.website.web.controller.BaseGridController;
import com.lyn.lost_and_found.web.vo.LocalPage;

import java.io.Serializable;
import java.util.*;

public abstract class  BaseLFGridController <T extends BaseEntity<ID>, ID extends Serializable> extends BaseGridController<T, ID> {
    public LocalPage localQuery() {
        Page<T> page = super.query();
        LocalPage localPage = new LocalPage();
        localPage.setPageNo(page.getPageNo());
        localPage.setPageSize(page.getPageSize());
        localPage.setTotal(page.getTotalCount());
        localPage.setRows(convertFieldCase(page.getItems()));
        return localPage;
    }

    protected List<Map<String, Object>> convertFieldCase(List items) {
        List<Map<String, Object>> copyItems = new ArrayList<>();
        if (items != null) {
            Iterator<Map<String, Object>> itemsIter = items.iterator();
            while (itemsIter.hasNext()) {
                Map<String, Object> item = itemsIter.next();
                Iterator<Map.Entry<String, Object>> entryIter = item.entrySet().iterator();
                Map<String, Object> m = new HashMap<>();
                while (entryIter.hasNext()) {
                    Map.Entry<String, Object> next = entryIter.next();
                    String s1 = StringUtil.convertUnderscore2Camelcase(next.getKey(), "_");
                    Object v1 = next.getValue();
                    m.put(s1, v1);
                }
                copyItems.add(m);
            }
        }
        return copyItems;
    }

    @Override
    public void before() {
        super.before();
        Long currentUserId = UserContextHolder.getCurrentUserId();
        this.addCondition(new Condition("currentUserId", currentUserId, currentUserId, Condition.SearchType.EQ));
    }
}
