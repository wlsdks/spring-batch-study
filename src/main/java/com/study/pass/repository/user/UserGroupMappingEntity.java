package com.study.pass.repository.user;

import com.study.pass.repository.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Table(name = "user_group_mapping")
@IdClass(UserGroupMappingId.class)
@Entity
public class UserGroupMappingEntity extends BaseEntity {

    // pk에 복합키를 적용시켰다.
    @Id
    private String userGroupId;
    @Id
    private String userId;

    private String userGroupName;
    private String description;


}
