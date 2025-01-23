package com.example.server.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class PageView<T> {
    private List<T> content;
    private int totalItems;
    private int totalPages;

    public PageView() {
        this.content = new ArrayList<T>();
        this.totalItems = 0;
        this.totalPages = 0;
    }
}
