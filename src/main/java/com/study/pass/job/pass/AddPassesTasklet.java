package com.study.pass.job.pass;

import com.study.pass.repository.pass.*;
import com.study.pass.repository.user.UserGroupMappingEntity;
import com.study.pass.repository.user.UserGroupMappingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class AddPassesTasklet implements Tasklet { // Tasklet을 구현해야 한다. execute()메서드 구현이 필요함

    private final PassRepository passRepository;
    private final BulkPassRepository bulkPassRepository;
    private final UserGroupMappingRepository userGroupMappingRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        // 이용권 시작 일시 1일전 user group내 각 사용자에게 이용권을 추가해 준다.
        final LocalDateTime startedAt = LocalDateTime.now().minusDays(1);
        // bulkPass가 READY 상태로 아직 벌크상태 처리가 안되었고 하루 전일때의 데이터를 조회해서 가져온다.
        final List<BulkPassEntity> bulkPassEntities = bulkPassRepository.findByStatusAndStartedAtGreaterThan(BulkPassStatus.READY, startedAt);

        int count = 0;
        // 대량 이용권의 정보를 돌면서 user group에 속한 userId를 조회하고 해당 userId로 이용권을 추가한다.
        for (BulkPassEntity bulkPassEntity : bulkPassEntities) {
            final List<String> userIds = userGroupMappingRepository.findByUserGroupId(bulkPassEntity.getUserGroupId())
                    .stream().map(UserGroupMappingEntity::getUserId).toList();

            count += addPasses(bulkPassEntity, userIds);

            // 모든 작업이 끝나면 status를 completed로 바꿔준다.
            bulkPassEntity.setStatus(BulkPassStatus.COMPLETED);
        }

        log.info("AddPassesTasklet - execute: 이용권 {}건 추가 완료, startedAt = {}", count, startedAt);
        return RepeatStatus.FINISHED; //finished를 해주면 끝난다.(다른걸로 하면 반복도있음)
    }

    // bulkPass의 정보로 pass 데이터를 생성한다.
    private int addPasses(BulkPassEntity bulkPassEntity, List<String> userIds) {
        List<PassEntity> passEntities = new ArrayList<>();
        for (String userId : userIds) {
            // PassModelMapper를 통해 PassEntity를 생성하였다.
            PassEntity passEntity = PassModelMapper.INSTANCE.toPassEntity(bulkPassEntity, userId);
            passEntities.add(passEntity);
        }
        // 저장한 건수를 받아온다.
        return passRepository.saveAll(passEntities).size();
    }


}

