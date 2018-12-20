package com.lyn.lost_and_found.service.impl;

import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.lyn.lost_and_found.domain.LfTransactionRecord;
import com.lyn.lost_and_found.domain.LfTransactionRecordRepository;
import com.lyn.lost_and_found.domain.LfUser;
import com.lyn.lost_and_found.domain.LfUserRepository;
import com.lyn.lost_and_found.service.LfTransactionRecordService;
import com.lyn.lost_and_found.service.LfUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class LfTransactionRecordServiceImpl extends EntityCRUDServiceImpl<LfTransactionRecord, Long> implements LfTransactionRecordService {

    @Autowired
    private LfTransactionRecordRepository transactionRecordRepository;

    @Override
    protected JpaRepository<LfTransactionRecord, Long> getRepository() {
        return super.getRepository();
    }

}
