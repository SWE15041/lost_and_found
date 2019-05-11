package com.lyn.lost_and_found.domain;

import com.jay.vito.storage.core.MyJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LfCorpusRepository extends MyJpaRepository<LfCorpus, Long> {

//    @Query(value = "select top 1 quantities from lf_corpus c where c.name = ?1", nativeQuery = true)
    List<LfCorpus>  findByName(String name);

    @Query(value = "select sum(quantities) from LfCorpus ")
    Long findSumByQuantities();
}
