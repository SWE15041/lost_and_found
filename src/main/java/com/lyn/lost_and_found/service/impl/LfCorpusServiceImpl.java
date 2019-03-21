package com.lyn.lost_and_found.service.impl;

import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.lyn.lost_and_found.domain.LfCorpus;
import com.lyn.lost_and_found.domain.LfCorpusRepository;
import com.lyn.lost_and_found.service.LfCorpusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
public class LfCorpusServiceImpl extends EntityCRUDServiceImpl<LfCorpus, Long> implements LfCorpusService {

    @Autowired
    private LfCorpusRepository corpusRepository;
    @Autowired
    private EntityManager entityManager;

    @Override
    protected JpaRepository<LfCorpus, Long> getRepository() {
        return super.getRepository();
    }


    @Override
    public Long getWordSum() {
//        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
//        Root<LfCorpus> root = criteriaQuery.from(LfCorpus.class);
////         criteriaQuery.multiselect(criteriaBuilder.sum(root.get("quantities")));
//        Expression<Long> quantities = criteriaBuilder.sum(root.get("quantities")).as(Long.class);
//        List<Tuple> resultList = entityManager.createQuery(criteriaQuery).getResultList();
//todo jpa 求和sum的用法
//        List<LfCorpus> all = getAll();
//        Long quantitiesSum = 0L;
//        for (LfCorpus corpus : all) {
//            Long quantities = corpus.getQuantities();
//            quantitiesSum += quantities;
//        }
//        return quantitiesSum;
        return corpusRepository.findByQuantities();
    }

    @Override
    public Long getWordQuantities(String name) {
        LfCorpus corpus = corpusRepository.findByName(name);
        return corpus.getQuantities();
    }
}
