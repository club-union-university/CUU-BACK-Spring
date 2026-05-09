-- =============================================
-- Schools
-- =============================================
INSERT INTO schools (name, email_domain, region, campus_type, lat, lng, is_whitelisted, created_at)
VALUES
    ('인하대학교', 'inha.ac.kr', 'GYEONGIN', 'MAIN', 37.4496, 126.6544, true, NOW()),
    ('인천대학교', 'inu.ac.kr', 'GYEONGIN', 'MAIN', 37.3745, 126.6327, true, NOW()),
    ('경인교육대학교', 'ginue.ac.kr', 'GYEONGIN', 'MAIN', 37.4781, 126.6455, true, NOW()),
    ('한국항공대학교', 'kau.ac.kr', 'GYEONGIN', 'MAIN', 37.6000, 126.8641, true, NOW()),
    ('서울대학교', 'snu.ac.kr', 'SEOUL', 'MAIN', 37.4597, 126.9523, false, NOW()),
    ('아주대학교', 'ajou.ac.kr', 'GYEONGIN', 'MAIN', 37.2832, 127.0454, true, NOW());

-- =============================================
-- School Facilities
-- =============================================
INSERT INTO school_facilities (school_id, name, facility_type, capacity, lat, lng, created_at)
VALUES
    (1, '60주년기념관 대강당', 'AUDITORIUM', 500, 37.4501, 126.6551, NOW()),
    (1, '5호관 501호', 'LECTURE_ROOM', 60, 37.4498, 126.6548, NOW()),
    (1, '학생회관 스터디룸 A', 'STUDY_ROOM', 20, 37.4490, 126.6540, NOW()),
    (2, '송도캠퍼스 대강당', 'AUDITORIUM', 400, 37.3748, 126.6330, NOW()),
    (2, '7호관 703호', 'LECTURE_ROOM', 50, 37.3750, 126.6335, NOW());

-- =============================================
-- Users
-- =============================================
INSERT INTO users (firebase_uid, auth_provider, email, nickname, profile_image, school_id, bio, role, created_at, updated_at)
VALUES
    ('firebase-uid-001', 'GOOGLE', 'admin@inha.ac.kr', '슈퍼관리자', 'https://via.placeholder.com/150', 1, '시스템 관리자입니다.', 'SUPER_ADMIN', NOW(), NOW()),
    ('firebase-uid-002', 'GOOGLE', 'alice@inha.ac.kr', '김앨리스', 'https://via.placeholder.com/150', 1, '프론트엔드 개발자입니다.', 'PRESIDENT', NOW(), NOW()),
    ('firebase-uid-003', 'GOOGLE', 'bob@inha.ac.kr', '이밥', 'https://via.placeholder.com/150', 1, '백엔드 개발자입니다.', 'PRESIDENT', NOW(), NOW()),
    ('firebase-uid-004', 'GOOGLE', 'carol@inu.ac.kr', '박캐롤', 'https://via.placeholder.com/150', 2, 'UI/UX 디자이너입니다.', 'PRESIDENT', NOW(), NOW()),
    ('firebase-uid-005', 'GOOGLE', 'dave@inu.ac.kr', '최데이브', 'https://via.placeholder.com/150', 2, 'PM입니다.', 'PRESIDENT', NOW(), NOW()),
    ('firebase-uid-006', 'GOOGLE', 'eve@kau.ac.kr', '정이브', 'https://via.placeholder.com/150', 4, 'iOS 개발자입니다.', 'PRESIDENT', NOW(), NOW()),
    ('firebase-uid-007', 'GOOGLE', 'frank@ajou.ac.kr', '한프랭크', 'https://via.placeholder.com/150', 6, '아주대 풀스택 개발자입니다.', 'PRESIDENT', NOW(), NOW());

-- =============================================
-- Clubs
-- =============================================
INSERT INTO clubs (school_id, president_user_id, name, category, description, invite_code, logo_image, evidence_url, status, approved_by_user_id, approved_at, created_at)
VALUES
    (1, 2, 'INHA DEV CLUB', 'DEV', '인하대 개발 동아리입니다. 웹/앱 개발을 함께 공부해요!', 'INHADEV1', 'https://via.placeholder.com/100', 'https://instagram.com/inhadev', 'APPROVED', 1, NOW(), NOW()),
    (1, 3, '인하 디자인 스튜디오', 'DESIGN', '인하대 디자인 동아리입니다. UI/UX, 그래픽 디자인을 공부해요!', 'INHADSGN', 'https://via.placeholder.com/100', 'https://notion.so/inhadesign', 'APPROVED', 1, NOW(), NOW()),
    (2, 4, 'INU MAKERS', 'DEV', '인천대 메이커스 동아리입니다. 창업과 개발을 함께해요!', 'INUMAKER', 'https://via.placeholder.com/100', 'https://instagram.com/inumakers', 'APPROVED', 1, NOW(), NOW()),
    (2, 5, '인천대 스타트업 클럽', 'STARTUP', '스타트업 아이디어를 함께 발전시켜 나가요!', 'INUSTART', 'https://via.placeholder.com/100', 'https://notion.so/inustartup', 'PENDING', null, null, NOW()),
    (4, 6, 'KAU 항공 해커톤팀', 'DEV', '항공대 해커톤 준비 동아리입니다.', 'KAUHACK1', 'https://via.placeholder.com/100', 'https://instagram.com/kauhack', 'APPROVED', 1, NOW(), NOW()),
    (6, 7, '아주 풀스택 동아리', 'DEV', '아주대 풀스택 개발 동아리입니다. 웹/앱/AI 모두 다뤄요!', 'AJOUDEV1', 'https://via.placeholder.com/100', 'https://instagram.com/ajoudev', 'APPROVED', 1, NOW(), NOW());

-- =============================================
-- Club Members
-- =============================================
INSERT INTO club_members (club_id, user_id, member_role, status, joined_at)
VALUES
    (1, 2, 'PRESIDENT', 'APPROVED', NOW()),
    (1, 3, 'MEMBER', 'APPROVED', NOW()),
    (2, 3, 'PRESIDENT', 'APPROVED', NOW()),
    (2, 2, 'MEMBER', 'APPROVED', NOW()),
    (3, 4, 'PRESIDENT', 'APPROVED', NOW()),
    (3, 5, 'MEMBER', 'APPROVED', NOW()),
    (4, 5, 'PRESIDENT', 'APPROVED', NOW()),
    (5, 6, 'PRESIDENT', 'APPROVED', NOW()),
    (6, 7, 'PRESIDENT', 'APPROVED', NOW());

-- =============================================
-- Events
-- =============================================
INSERT INTO events (type, host_club_id, partner_club_id, title, category, description, format,
                    location_name, location_address, location_lat, location_lng,
                    start_at, end_at, recruit_deadline, max_participants,
                    status, host_approved, partner_approved, proposal_message,
                    created_by_user_id, created_at, updated_at)
VALUES
    ('INTRA_CLUB', 1, null, '2025 INHA 해커톤', 'HACKATHON',
     '인하대 개발 동아리 해커톤입니다. 48시간 동안 아이디어를 구현해요!', '오프라인',
     '60주년기념관 대강당', '인천광역시 미추홀구 인하로 100', 37.4501, 126.6551,
     '2025-06-15 09:00:00', '2025-06-17 09:00:00', '2025-06-01 23:59:59', 50,
     'RECRUITING', true, false, null, 2, NOW(), NOW()),

    ('INTER_CLUB', 1, 3, '인하-인천대 연합 밋업', 'MEETUP',
     '인하대와 인천대 개발 동아리 연합 네트워킹 행사입니다.', '오프라인',
     '인천대학교 송도캠퍼스 대강당', '인천광역시 연수구 아카데미로 119', 37.3748, 126.6330,
     '2025-07-05 14:00:00', '2025-07-05 18:00:00', '2025-06-25 23:59:59', 80,
     'APPROVED', true, true, '함께 네트워킹 행사를 진행하고 싶습니다!', 2, NOW(), NOW()),

    ('INTRA_CLUB', 2, null, '인하 디자인 스터디', 'STUDY',
     'Figma와 UI/UX 디자인 스터디입니다.', '온라인',
     null, null, null, null,
     '2025-06-20 19:00:00', '2025-06-20 21:00:00', '2025-06-15 23:59:59', 20,
     'RECRUITING', true, false, null, 3, NOW(), NOW()),

    ('INTRA_CLUB', 5, null, 'KAU 항공 해커톤 2025', 'HACKATHON',
     '항공 관련 아이디어로 해커톤을 진행합니다.', '오프라인',
     null, null, null, null,
     '2025-08-01 09:00:00', '2025-08-03 09:00:00', '2025-07-20 23:59:59', 30,
     'DRAFT', false, false, null, 6, NOW(), NOW());

-- =============================================
-- Event Participants
-- =============================================
INSERT INTO event_participants (event_id, user_id, status, applied_at, responded_at)
VALUES
    (1, 3, 'APPROVED', NOW(), NOW()),
    (1, 4, 'APPROVED', NOW(), NOW()),
    (1, 5, 'PENDING', NOW(), null),
    (2, 4, 'APPROVED', NOW(), NOW()),
    (2, 5, 'APPROVED', NOW(), NOW()),
    (3, 2, 'APPROVED', NOW(), NOW());

-- =============================================
-- Posts
-- =============================================
INSERT INTO posts (author_id, board_type, target_id, category, is_official_notice, title, content, created_at, updated_at)
VALUES
    (2, 'EVENT', 1, 'NOTICE', true, '[공지] 2025 INHA 해커톤 참가 안내',
     '안녕하세요! 2025 INHA 해커톤 참가자 여러분께 안내드립니다.\n\n- 일시: 2025년 6월 15일 ~ 17일\n- 장소: 60주년기념관 대강당\n- 팀 구성: 3~5인\n\n많은 참여 부탁드립니다!',
     NOW(), NOW()),

    (2, 'EVENT', 1, 'TEAM_BUILDING', false, '팀원 구합니다! 백엔드 개발자 1명',
     '안녕하세요! 해커톤 팀원을 구합니다.\n\n저희 팀은 현재 프론트 2명, 기획 1명으로 구성되어 있고 백엔드 개발자 1명을 찾고 있습니다.\n관심 있으신 분은 댓글 달아주세요!',
     NOW(), NOW()),

    (3, 'EVENT', 1, 'QNA', false, '개발 환경 관련 질문드립니다',
     '해커톤 때 노트북 지참해야 하나요? 아니면 현장에 PC가 있나요?',
     NOW(), NOW()),

    (2, 'EVENT', 2, 'NOTICE', true, '[공지] 인하-인천대 연합 밋업 안내',
     '연합 밋업 행사 안내입니다.\n\n- 일시: 2025년 7월 5일 14:00 ~ 18:00\n- 장소: 인천대학교 송도캠퍼스 대강당\n\n네트워킹을 통해 좋은 인연 만들어가세요!',
     NOW(), NOW()),

    (4, 'EVENT', 2, 'SCHEDULE', false, '밋업 세부 일정 공유',
     '밋업 세부 일정입니다.\n\n14:00 - 입장 및 등록\n14:30 - 각 동아리 소개\n15:30 - 자유 네트워킹\n17:00 - 미니 해커톤\n18:00 - 마무리',
     NOW(), NOW()),

    (3, 'EVENT', 3, 'NOTICE', true, '[공지] 디자인 스터디 안내',
     'Figma 기초부터 실전까지 함께 공부해요!\n\n매주 금요일 저녁 7시 온라인으로 진행됩니다.',
     NOW(), NOW());

-- =============================================
-- Comments
-- =============================================
INSERT INTO comments (post_id, author_id, content, created_at)
VALUES
    (1, 3, '참가 신청 완료했습니다! 기대됩니다', NOW()),
    (1, 4, '저도 신청했어요! 잘 부탁드립니다~', NOW()),
    (2, 4, '저 관심 있어요! 사용 기술 스택이 어떻게 되나요?', NOW()),
    (2, 2, 'Spring Boot + JPA 사용할 예정이에요!', NOW()),
    (3, 2, '노트북 지참해주셔야 합니다! 전원 멀티탭은 준비되어 있어요', NOW()),
    (4, 5, '기대됩니다! 잘 부탁드려요~', NOW()),
    (5, 2, '좋은 일정이네요! 미니 해커톤 주제가 뭔가요?', NOW()),
    (5, 4, '아직 미정이에요! 당일 발표 예정입니다', NOW());
