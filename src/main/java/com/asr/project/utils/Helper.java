package com.asr.project.utils;

import com.asr.project.payloads.PageableResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;

public class Helper {

    private Helper(){}

    public static<U,V> PageableResponse<V> pageableResponse(Page<U> page, Class<V> type) {

        ModelMapper mapper = new ModelMapper();

        List<U> users = page.toList();
        List<V> list = users.stream().map(obj -> mapper.map(obj, type)).toList();

        PageableResponse<V> response = new PageableResponse<>();
        response.setContent(list);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());

        return response;
    }

}
