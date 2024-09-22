-- 실패한 마이그레이션 로그 삭제 (dev, local)
DELETE FROM flyway_schema_history WHERE success = false;
