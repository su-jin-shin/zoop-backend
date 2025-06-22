

--  review 테이블 제약 조건 수정
ALTER TABLE review DROP CONSTRAINT IF EXISTS review_has_children_check;

ALTER TABLE review ADD CONSTRAINT review_has_children_check
  CHECK (has_children IN ('HAS_CHILDREN', 'NON_CHILDREN'));

--  review_comment 테이블 제약 조건 수정
ALTER TABLE review_comment DROP CONSTRAINT IF EXISTS review_comment_has_children_check;

ALTER TABLE review_comment ADD CONSTRAINT review_comment_has_children_check
  CHECK (has_children IN ('HAS_CHILDREN', 'NON_CHILDREN'));
