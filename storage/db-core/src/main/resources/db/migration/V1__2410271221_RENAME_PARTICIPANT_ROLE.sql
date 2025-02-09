-- Reason: participants 테이블의 팀별 사용자 권한을 나타내는 role 과 애플리케이션 사용자 권한을 나타내는 role 이 이름이 중복되어 수정
-- Author: Ywoosang
-- Date: 2024-10-27

ALTER TABLE participants
    DROP CHECK participants_chk_1;

ALTER TABLE participants
    CHANGE COLUMN role team_member_role VARCHAR(255) NOT NULL;

ALTER TABLE participants
    ADD CONSTRAINT participants_chk_1 CHECK (team_member_role IN ('ADMIN', 'ORDERER', 'VIEWER'));
