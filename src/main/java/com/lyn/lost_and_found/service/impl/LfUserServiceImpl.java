package com.lyn.lost_and_found.service.impl;

import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.lyn.lost_and_found.domain.LfUser;
import com.lyn.lost_and_found.domain.LfUserRepository;
import com.lyn.lost_and_found.service.LfUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class LfUserServiceImpl extends EntityCRUDServiceImpl<LfUser, Long> implements LfUserService {

    @Autowired
    private LfUserRepository userRepository;

    @Override
    protected JpaRepository<LfUser, Long> getRepository() {
        return super.getRepository();
    }

    @Override
    public LfUser getByWechatOpenid(String openid) {
        LfUser user = userRepository.findByWechatOpenid(openid);
        return user;
    }

    @Override
    public LfUser getByMobile(String mobile) {
        LfUser lfUser = userRepository.findByMobile(mobile);
        return lfUser;
    }
}
