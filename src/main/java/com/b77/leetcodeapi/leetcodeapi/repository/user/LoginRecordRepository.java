package com.b77.leetcodeapi.leetcodeapi.repository.user;

import com.b77.leetcodeapi.leetcodeapi.model.entity.user.LoginRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LoginRecordRepository extends JpaRepository<LoginRecord, Long> {

}
