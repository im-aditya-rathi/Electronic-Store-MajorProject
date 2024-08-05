package com.asr.project.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageableResponse<T> {

    private boolean lastPage;
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private long totalElements;

}
