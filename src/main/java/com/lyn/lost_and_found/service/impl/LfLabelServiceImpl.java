package com.lyn.lost_and_found.service.impl;

import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.lyn.lost_and_found.domain.LfLabel;
import com.lyn.lost_and_found.domain.LfLabelRepository;
import com.lyn.lost_and_found.domain.LfUser;
import com.lyn.lost_and_found.domain.LfUserRepository;
import com.lyn.lost_and_found.service.LfLabelService;
import com.lyn.lost_and_found.service.LfUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class LfLabelServiceImpl extends EntityCRUDServiceImpl<LfLabel, Long> implements LfLabelService {

    @Autowired
    private LfLabelRepository lfLabelRepository;

    @Override
    protected JpaRepository<LfLabel, Long> getRepository() {
        return super.getRepository();
    }
}

