package com.lyn.lost_and_found.domain;

import com.jay.vito.storage.core.MyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LfCorpusRepository extends MyJpaRepository<LfCorpus, Long> {

    LfCorpus findByName(String name);
}
