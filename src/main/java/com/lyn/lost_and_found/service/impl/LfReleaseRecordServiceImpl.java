package com.lyn.lost_and_found.service.impl;

import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.lyn.lost_and_found.domain.LfReleaseRecord;
import com.lyn.lost_and_found.domain.LfReleaseRecordRepository;
import com.lyn.lost_and_found.domain.LfUser;
import com.lyn.lost_and_found.domain.LfUserRepository;
import com.lyn.lost_and_found.service.LfReleaseRecordService;
import com.lyn.lost_and_found.service.LfUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class LfReleaseRecordServiceImpl extends EntityCRUDServiceImpl<LfReleaseRecord, Long> implements LfReleaseRecordService {

    @Autowired
    private LfReleaseRecordRepository lfReleaseRecordRepository;

    @Override
    protected JpaRepository<LfReleaseRecord, Long> getRepository() {
        return super.getRepository();
    }
}
