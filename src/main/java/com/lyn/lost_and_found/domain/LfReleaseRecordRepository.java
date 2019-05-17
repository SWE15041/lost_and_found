package com.lyn.lost_and_found.domain;

import com.jay.vito.storage.core.MyJpaRepository;
import com.lyn.lost_and_found.config.constant.ReleaseType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LfReleaseRecordRepository extends MyJpaRepository<LfReleaseRecord, Long> {

    LfReleaseRecord findByGoodsId(Long goodsId);

    LfReleaseRecord findByGoodsIdAndReleaseUserId(Long goodsId, Long releaseUserId);

    List<LfReleaseRecord> findByReleaseType(ReleaseType releaseType);

    @Query(value = " select count(1)  from lf_goods g JOIN lf_category c ON g.category_id = c.id JOIN lf_release_record rr ON g.id = rr.goods_id JOIN lf_user u ON rr.release_user_id=u.id WHERE g.release_status !=1 and (g.name LIKE CONCAT('%',?1,'%')   or g.description like CONCAT('%',?1,'%') ) ", nativeQuery = true)
    Long cntRecords(String word);
//    @Query(value = "select * from house where if(?1 !='',address like %?1%,1=1)",nativeQuery = true)
}
