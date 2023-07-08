package com.study.pass.repository.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface BookingRepository extends JpaRepository<BookingEntity, Integer> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE BookingEntity b" +
                    " SET b.usedPass = :usedPass," +
                        " b.modifiedAt = CURRENT_TIMESTAMP " +
                    " where  b.passSeq = :passSeq")
    int updateUsedPass(Integer passSeq, boolean usedPass);
}