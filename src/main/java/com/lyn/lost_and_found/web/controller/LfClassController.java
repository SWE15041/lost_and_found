package com.lyn.lost_and_found.web.controller;

import com.jay.vito.website.web.controller.BaseGridController;
import com.lyn.lost_and_found.domain.LfClass;
import com.lyn.lost_and_found.domain.LfUser;
import com.lyn.lost_and_found.service.LfClassService;
import com.lyn.lost_and_found.service.LfUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/lost_and_found/lfusers")
public class LfClassController extends BaseGridController<LfClass, Long> {

    @Autowired
    private LfClassService lfClassService;

//    @RequestMapping(value = "/login",method = RequestMethod.POST)
//    public Map<String,Object> login(@RequestBody LfUser lfUser){
//
//    }


}
