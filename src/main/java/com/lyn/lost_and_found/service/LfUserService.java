package com.lyn.lost_and_found.service;

import com.jay.vito.storage.service.EntityCRUDService;
import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.lyn.lost_and_found.domain.LfUser;

public interface LfUserService extends EntityCRUDService<LfUser, Long> {

//    LfUser  getByName(String name);

    /**
     * 通过用户微信openid获取用户记录
     * @param openid
     * @return
     */
    LfUser getByWechatOpenid(String openid);
}
