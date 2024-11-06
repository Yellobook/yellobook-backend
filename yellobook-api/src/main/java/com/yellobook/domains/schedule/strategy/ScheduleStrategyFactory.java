package com.yellobook.domains.schedule.strategy;

import com.yellobook.common.enums.TeamMemberRole;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleStrategyFactory {
    // 빈이름, ScheduleStrategy 타입 빈으로 Autowired
    private final Map<String, ScheduleStrategy> scheduleStrategies;

    public ScheduleStrategy getStrategy(TeamMemberRole role) {
        // 컴포넌트 스캔으로 등록된 빈 이름으로 선택
        String beanName = role.name()
                .toLowerCase() + "ScheduleStrategy";
        return scheduleStrategies.get(beanName);
    }
}
