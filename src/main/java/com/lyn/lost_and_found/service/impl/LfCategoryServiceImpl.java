package com.lyn.lost_and_found.service.impl;

import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.lyn.lost_and_found.domain.LfCategory;
import com.lyn.lost_and_found.domain.LfCategoryRepository;
import com.lyn.lost_and_found.service.LfCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class LfCategoryServiceImpl extends EntityCRUDServiceImpl<LfCategory, Long> implements LfCategoryService {

    @Autowired
    private LfCategoryRepository lfCategoryRepository;

    @Override
    protected JpaRepository<LfCategory, Long> getRepository() {
        return super.getRepository();
    }
}
