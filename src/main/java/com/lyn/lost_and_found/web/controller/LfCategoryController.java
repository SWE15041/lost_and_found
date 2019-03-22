package com.lyn.lost_and_found.web.controller;

import com.lyn.lost_and_found.domain.LfCategory;
import com.lyn.lost_and_found.service.LfCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/categories")
public class LfCategoryController extends BaseLFGridController<LfCategory, Long> {

    @Autowired
    private LfCategoryService lfCategoryService;


}
