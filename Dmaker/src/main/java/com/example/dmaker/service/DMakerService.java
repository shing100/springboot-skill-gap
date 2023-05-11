package com.example.dmaker.service;

import com.example.dmaker.entity.Developer;
import com.example.dmaker.repository.DeveloperRepository;
import com.example.dmaker.type.DeveloperLevel;
import com.example.dmaker.type.DeveloperSkillType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

@Slf4j
@Service
@RequiredArgsConstructor
public class DMakerService {
    private final DeveloperRepository developerRepository;
    private final EntityManager em;

    // ACID
    // Atomicity: 원자성
    // Consistency: 일관성
    // Isolation: 고립성
    // Durability: 지속성
    @Transactional
    public void createDeveloper() {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            Developer developer = Developer.builder()
                    .developerLevel(DeveloperLevel.JUNIOR)
                    .developerSkillType(DeveloperSkillType.FRONT_END)
                    .experienceYears(2)
                    .name("Olaf")
                    .age(5)
                    .build();

            developerRepository.save(developer);
        } catch (Exception e) {
            log.error("error: {}", e);
            transaction.rollback();
        } finally {
            transaction.commit();
        }
    }


}
