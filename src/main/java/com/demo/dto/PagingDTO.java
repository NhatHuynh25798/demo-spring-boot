package com.demo.dto;

import java.util.List;

public class PagingDTO<T> {
    private List<T> data;
    private long total;
    private int page;
    private int limit;
    private int skip;

    public PagingDTO(List<T> data, long total) {
        this.data = data;
        this.total = total;
    }

    public PagingDTO(List<T> data, long total, int page, int limit) {
        this.data = data;
        this.total = total;
        this.page = page;
        this.limit = limit;
        this.skip = (page - 1) * limit;
    }

    public PagingDTO(List<T> data) {
        this.data = data;
        this.total = data.size();
    }

    public PagingDTO() {
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }
}
