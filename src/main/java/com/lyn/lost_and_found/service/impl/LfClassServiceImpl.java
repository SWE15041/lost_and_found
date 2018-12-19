package com.lyn.lost_and_found.service.impl;

import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.lyn.lost_and_found.domain.LfClass;
import com.lyn.lost_and_found.domain.LfClassRepository;
import com.lyn.lost_and_found.domain.LfUser;
import com.lyn.lost_and_found.domain.LfUserRepository;
import com.lyn.lost_and_found.service.LfClassService;
import com.lyn.lost_and_found.service.LfUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class LfClassServiceImpl extends EntityCRUDServiceImpl<LfClass, Long> implements LfClassService {

    @Autowired
    private LfClassRepository lfClassRepository;

    @Override
    protected JpaRepository<LfClass, Long> getRepository() {
        return super.getRepository();
    }
}
