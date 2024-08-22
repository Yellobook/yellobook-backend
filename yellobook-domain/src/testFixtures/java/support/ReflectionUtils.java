package support;

import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class ReflectionUtils {

    /**
     * 엔티티의 createdAt과 updatedAt 필드를 설정
     * @param entity 엔티티 인스턴스
     * @param timestamp 설정할 시간
     */
    public static void setBaseTimeEntityFields(Object entity, LocalDateTime timestamp) {
        try {
            setField(entity, "createdAt", timestamp);
            setField(entity, "updatedAt", timestamp);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Assertions.fail("리플렉션 필드 설정 중 예외 발생: " + e.getMessage());
        }
    }

    // 리플렉션으로 필드값 설정
    private static void setField(Object entity, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = entity.getClass().getSuperclass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(entity, value);
    }
}
