package com.lyn.lost_and_found.service.impl;

import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.lyn.lost_and_found.domain.LfClaimRecord;
import com.lyn.lost_and_found.domain.LfUser;
import com.lyn.lost_and_found.domain.LfUserRepository;
import com.lyn.lost_and_found.service.LfClaimRecordService;
import com.lyn.lost_and_found.service.LfUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class LfClaimRecordServiceImpl extends EntityCRUDServiceImpl<LfClaimRecord, Long> implements LfClaimRecordService {

    @Autowired
    private LfUserRepository lfUserRepository;

    @Override
    protected JpaRepository<LfClaimRecord, Long> getRepository() {
        return super.getRepository();
    }
}
