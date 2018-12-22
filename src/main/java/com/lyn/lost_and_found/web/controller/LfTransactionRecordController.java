package com.lyn.lost_and_found.web.controller;

import com.lyn.lost_and_found.domain.LfTransactionRecord;
import com.lyn.lost_and_found.domain.LfUser;
import com.lyn.lost_and_found.service.LfTransactionRecordService;
import com.lyn.lost_and_found.service.LfUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/lost_and_found/lfTransactionRecords")
public class LfTransactionRecordController extends BaseLFGridController<LfTransactionRecord, Long> {

    @Autowired
    private LfTransactionRecordService transactionRecordService;



}
