package com.lyn.lost_and_found.web.vo;

import java.util.List;

public class LocalPage<T> {
    /**
     * 当前所在页码
     */
    private int pageNo = 1;
    /**
     * 当前单页显示条目
     */
    private int pageSize = 10;

    /**
     * 当前请求结果集
     */
    private List<T> rows = null;

    private Long total;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
