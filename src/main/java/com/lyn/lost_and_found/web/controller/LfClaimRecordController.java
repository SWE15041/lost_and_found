package com.lyn.lost_and_found.web.controller;

import com.jay.vito.website.web.controller.BaseGridController;
import com.lyn.lost_and_found.domain.LfClaimRecord;
import com.lyn.lost_and_found.domain.LfUser;
import com.lyn.lost_and_found.service.LfClaimRecordService;
import com.lyn.lost_and_found.service.LfUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/lost_and_found/lfusers")
public class LfClaimRecordController extends BaseLFGridController<LfClaimRecord, Long> {

    @Autowired
    private LfClaimRecordService lfClaimRecordService;

//    @RequestMapping(value = "/login",method = RequestMethod.POST)
//    public Map<String,Object> login(@RequestBody LfUser lfUser){
//
//    }


}
