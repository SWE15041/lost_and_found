package com.lyn.lost_and_found.web.controller;

import com.jay.vito.website.web.controller.BaseGridController;
import com.lyn.lost_and_found.domain.LfReleaseRecord;
import com.lyn.lost_and_found.domain.LfUser;
import com.lyn.lost_and_found.service.LfReleaseRecordService;
import com.lyn.lost_and_found.service.LfUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/lost_and_found/lfusers")
public class LfReleaseRecordController extends BaseGridController<LfReleaseRecord, Long> {

    @Autowired
    private LfReleaseRecordService lfReleaseRecordService;

//    @RequestMapping(value = "/login",method = RequestMethod.POST)
//    public Map<String,Object> login(@RequestBody LfUser lfUser){
//
//    }


}
