//package support;
//
//import java.lang.reflect.Field;
//import java.time.LocalDateTime;
//import org.junit.jupiter.api.Assertions;
//
//public class ReflectionUtil {
//    /**
//     * 엔티티의 createdAt과 updatedAt 필드를 설정
//     *
//     * @param entity    엔티티 인스턴스
//     * @param timestamp 설정할 시간
//     */
//    public static void setBaseTimeEntityFields(Object entity, LocalDateTime timestamp) {
//        setField(entity, "createdAt", timestamp);
//        setField(entity, "updatedAt", timestamp);
//    }
//
//    /**
//     * 주어진 객체의 필드나 상위 클래스 필드를 리플렉션으로 설정하는 메서드
//     *
//     * @param targetObject 필드를 설정할 객체
//     * @param fieldName    설정할 필드명
//     * @param value        설정할 값
//     */
//    public static void setField(Object targetObject, String fieldName, Object value) {
//        try {
//            Field field = findFieldRecursive(targetObject.getClass(), fieldName);
//
//            if (field != null) {
//                field.setAccessible(true);
//                field.set(targetObject, value);
//            } else {
//                Assertions.fail("설정할 필드를 찾을 수 없음: " + fieldName);
//            }
//        } catch (IllegalAccessException e) {
//            Assertions.fail("리플렉션 필드 설정 중 예외 발생: " + e.getMessage());
//        }
//    }
//
//    /**
//     * 현재 클래스와 상위 클래스에서 필드를 재귀적으로 검색하는 메서드
//     *
//     * @param clazz     검색할 클래스
//     * @param fieldName 검색할 필드명
//     * @return 필드 객체, 없으면 null 반환
//     */
//    private static Field findFieldRecursive(Class<?> clazz, String fieldName) {
//        if (clazz == null) {
//            return null;
//        }
//
//        try {
//            return clazz.getDeclaredField(fieldName);
//        } catch (NoSuchFieldException e) {
//            // 상위 클래스에서 다시 검색
//            return findFieldRecursive(clazz.getSuperclass(), fieldName);
//        }
//    }
//}
