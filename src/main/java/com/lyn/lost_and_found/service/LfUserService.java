package com.lyn.lost_and_found.service;

import com.jay.vito.storage.service.EntityCRUDService;
import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.lyn.lost_and_found.domain.LfUser;

public interface LfUserService extends EntityCRUDService<LfUser, Long> {

    /**
     * 通过用户微信openid获取用户记录
     * @param openid
     * @return
     */
    LfUser getByWechatOpenid(String openid);

    /**
     * 通过手机号获取用户
     * @param mobile
     * @return
     */
    LfUser getByMobile(String mobile);

}
