-- ------------------------------------------------------------
--  V5  |  Purge all data from real-estate–related tables
-- ------------------------------------------------------------
-- 주의: 이 스크립트는 모든 데이터를 영구히 삭제합니다.
--      운영 DB가 아닌 사본/스테이징 DB에서 먼저 검증하세요.
-- ------------------------------------------------------------

-- Flyway는 기본적으로 트랜잭션 안에서 실행하지만,
-- 데이터베이스 설정에 따라 다를 수 있으니 명시적으로 감싸 줍니다.
BEGIN;

DELETE FROM "review_comment_like";
DELETE FROM "review_like";
DELETE FROM "review_comment";
DELETE FROM "review";
DELETE FROM "image";
DELETE FROM "property";
DELETE FROM "complex";
DELETE FROM "realty";

COMMIT;
