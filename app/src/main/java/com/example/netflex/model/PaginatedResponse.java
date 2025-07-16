package com.example.netflex.model;

import java.util.List;

public class PaginatedResponse<T> {
    private int pageIndex;
    private int pageSize;
    private int total;
    private List<T> data;

    public int getPageIndex() { return pageIndex; }
    public int getPageSize() { return pageSize; }
    public int getTotal() { return total; }
    public List<T> getData() { return data; }
}

