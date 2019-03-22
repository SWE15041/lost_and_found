package com.lyn.lost_and_found.web.controller;

import com.jay.vito.uic.client.interceptor.IgnoreUserAuth;
import com.lyn.lost_and_found.domain.LfCorpus;
import com.lyn.lost_and_found.service.LfCorpusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/corpuses")
public class LfCorpusController extends BaseLFGridController<LfCorpus, Long> {

    @Autowired
    private LfCorpusService corpusService;

    @IgnoreUserAuth
    @RequestMapping(value = "/trainCorpus", method = RequestMethod.POST)
    public Boolean trainCorpus(@RequestBody Map<String, String> dirMap) {
        String corpusDir = dirMap.get("corpusDir");
        return corpusService.trainCorpus(corpusDir);
    }

}
