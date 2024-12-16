-- Subscription 테이블 데이터
INSERT IGNORE INTO subscription (name, price, explanation)
VALUES
    ('한달 무료 체험', 0, '한달 무료체험 가능 사실 3주로 기획했는데 어쩌다보니 4주됨.'),
    ('일반형', 2900, '일반형, 기본 기능이고 상태보기 기능 다양, 애정카드 기능 다양'),
    ('프리미엄형', 3900, '프리미엄 구독입니다. 모든 기능을 무제한으로 제공합니다.');

-- Status 테이블 데이터
INSERT IGNORE INTO status (text)
VALUES
    ('공부 중'),
    ('노는 중'),
    ('쉬는 중'),
    ('일하는 중');

-- Snapshot title 테이블 데이터
INSERT IGNORE INTO snapshot_title (title)
VALUES
    ('가장 잘나온 셀카'),
    ('오늘의 점심메뉴'),
    ('내 갤러리의 4번째 사진'),
    ('지금 내가 앉아있는 곳'),
    ('지금 보이는 파란색 물건'),
    ('오늘의 출근길/퇴근길'),
    ('내가 지내는 장소'),
    ('내가 좋아하는 간식'),
    ('좋아하는 음악'),
    ('나의 휴식 공간');

-- Lovecard 테이블 데이터
INSERT IGNORE INTO lovecard (image)
VALUES
    ('https://sursim-img.s3.ap-northeast-2.amazonaws.com/lovecard1.png'),
    ('https://sursim-img.s3.ap-northeast-2.amazonaws.com/lovecard2.png'),
    ('https://sursim-img.s3.ap-northeast-2.amazonaws.com/lovecard3.png'),
    ('https://sursim-img.s3.ap-northeast-2.amazonaws.com/lovecard4.png'),
    ('https://sursim-img.s3.ap-northeast-2.amazonaws.com/lovecard5.png'),
    ('https://sursim-img.s3.ap-northeast-2.amazonaws.com/lovecard6.png'),
    ('https://sursim-img.s3.ap-northeast-2.amazonaws.com/lovecard7.png'),
    ('https://sursim-img.s3.ap-northeast-2.amazonaws.com/lovecard8.png'),
    ('https://sursim-img.s3.ap-northeast-2.amazonaws.com/lovecard9.png'),
    ('https://sursim-img.s3.ap-northeast-2.amazonaws.com/lovecard10.png'),
    ('https://sursim-img.s3.ap-northeast-2.amazonaws.com/lovecard11.png'),
    ('https://sursim-img.s3.ap-northeast-2.amazonaws.com/lovecard12.png');