package com.yellobook.domains.schedule.service.strategy;

import com.yellobook.common.enums.MemberTeamRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ScheduleStrategyFactory {
    // 빈이름, ScheduleStrategy 타입 빈으로 Autowired
    private final Map<String, ScheduleStrategy> scheduleStrategies;

    public ScheduleStrategy getStrategy(MemberTeamRole role) {
        // 컴포넌트 스캔으로 등록된 빈 이름으로 선택
        String beanName = role.name().toLowerCase() + "ScheduleStrategy";
        return scheduleStrategies.get(beanName);
    }
}
