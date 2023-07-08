package com.study.pass.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserGroupMappingRepository extends JpaRepository<UserGroupMappingEntity, UserGroupMappingId> {
    // userGroupId에 의해 find 된다는 의미다.
    List<UserGroupMappingEntity> findByUserGroupId(String userGroupId);
}