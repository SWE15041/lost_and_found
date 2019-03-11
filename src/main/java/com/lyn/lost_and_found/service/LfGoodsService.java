package com.lyn.lost_and_found.service;

import com.jay.vito.storage.service.EntityCRUDService;
import com.lyn.lost_and_found.config.constant.ReleaseStatus;
import com.lyn.lost_and_found.config.constant.ReleaseType;
import com.lyn.lost_and_found.domain.LfGoods;
import com.lyn.lost_and_found.domain.LfUser;

import java.util.List;

public interface LfGoodsService extends EntityCRUDService<LfGoods, Long> {


    //    LfUser  getByName(String name);

    /**
     * 根据发布类型和发布记录状态筛选物品
     * @param releaseType
     * @param releaseStatus
     * @return
     */
    List<LfGoods> findGoods(ReleaseType releaseType, ReleaseStatus releaseStatus);
}
