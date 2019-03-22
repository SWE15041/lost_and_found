package com.lyn.lost_and_found.service.impl;

import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.lyn.lost_and_found.corpus.CorpusTraining;
import com.lyn.lost_and_found.domain.LfCorpus;
import com.lyn.lost_and_found.domain.LfCorpusRepository;
import com.lyn.lost_and_found.service.LfCorpusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Iterator;
import java.util.Map;

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
        if (Validator.isNull(corpus)) {
            return null;
        }
        return corpus.getQuantities();
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public Boolean trainCorpus(String corpusDir) {
        if (Validator.isNull(corpusDir)) {
            return false;
        }
        Map<String, Long> corpusMap = CorpusTraining.trainCorpus(corpusDir);
        Iterator<Map.Entry<String, Long>> iterator = corpusMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Long> next = iterator.next();
            String key = next.getKey();
            Long value = next.getValue();
            LfCorpus corpus = new LfCorpus();
            corpus.setName(key);
            corpus.setQuantities(value);
            super.save(corpus);
        }
        return true;
    }
}
