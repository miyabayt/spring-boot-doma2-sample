package com.sample.web.base.controller.api.resource;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sample.domain.dto.common.Dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageableResourceImpl extends ResourceImpl implements PageableResource {

    int page = 1;

    int totalPages;

    public PageableResourceImpl() {
    }

    public PageableResourceImpl(List<? extends Dto> data, int page, int totalPages) {
        this.data = data;
        this.page = page;
        this.totalPages = totalPages;
    }
}
