package com.lyn.lost_and_found.service.impl;

import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.lyn.lost_and_found.domain.LfKeywordTfidf;
import com.lyn.lost_and_found.domain.LfKeywordTfidfRepository;
import com.lyn.lost_and_found.service.LfKeywordTfidfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class LfKeywordTfidfServiceImpl extends EntityCRUDServiceImpl<LfKeywordTfidf, Long> implements LfKeywordTfidfService {

    @Autowired
    private LfKeywordTfidfRepository keywordTfidfRepository;

    @Override
    protected JpaRepository<LfKeywordTfidf, Long> getRepository() {
        return super.getRepository();
    }
}
