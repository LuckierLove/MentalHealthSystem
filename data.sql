USE mental_health_system;

-- 插入用户数据 (密码均为 12345678)
-- 密码哈希: $2a$10$QJyBCAsR/NFAt1Z/C2GO2.wurarTstVMcQed8PGE6BqeD9ZlSGyh6
INSERT INTO user (id, username, password, nickname, real_name, email, avatar) VALUES
('1', 'admin', '$2a$10$QJyBCAsR/NFAt1Z/C2GO2.wurarTstVMcQed8PGE6BqeD9ZlSGyh6', '管理员', '张三', 'admin@example.com', 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'),
('2', 'counselor1', '$2a$10$QJyBCAsR/NFAt1Z/C2GO2.wurarTstVMcQed8PGE6BqeD9ZlSGyh6', '李医生', '李四', 'counselor1@example.com', 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'),
('3', 'student1', '$2a$10$QJyBCAsR/NFAt1Z/C2GO2.wurarTstVMcQed8PGE6BqeD9ZlSGyh6', '小王', '王五', 'student1@example.com', 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'),
('4', 'counselor2', '$2a$10$QJyBCAsR/NFAt1Z/C2GO2.wurarTstVMcQed8PGE6BqeD9ZlSGyh6', '王医生', '王六', 'counselor2@example.com', 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'),
('5', 'student2', '$2a$10$QJyBCAsR/NFAt1Z/C2GO2.wurarTstVMcQed8PGE6BqeD9ZlSGyh6', '小李', '李七', 'student2@example.com', 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png');

-- 插入用户角色关联
-- 1528734928371712000: 学生
-- 1528734928371712001: 咨询师
-- 1528734928371712002: 管理员
INSERT INTO user_role (user_id, role_id) VALUES
('1', '1528734928371712002'),
('2', '1528734928371712001'),
('3', '1528734928371712000'),
('4', '1528734928371712001'),
('5', '1528734928371712000');

-- 插入咨询师详情
INSERT INTO counselor (id, user_id, name, specialty, introduction, rating) VALUES
('201', '2', '李四', '抑郁症, 焦虑症', '资深心理咨询师，从业10年，擅长认知行为疗法。', 4.8),
('202', '4', '王六', '人际关系, 职业规划', '国家二级心理咨询师，专注于青少年心理健康和职业发展规划。', 4.9);

-- 插入预约数据
INSERT INTO appointment (counselor_id, student_id, appointment_time, type, status, rating) VALUES
('2', '3', DATE_ADD(NOW(), INTERVAL 1 DAY), 1, 1, 0),
('2', '5', DATE_ADD(NOW(), INTERVAL 2 DAY), 2, 1, 0),
('4', '3', DATE_SUB(NOW(), INTERVAL 1 DAY), 1, 3, 5);

-- 插入树洞帖子
INSERT INTO treehole_post (id, title, content, user_id, post_time, pass) VALUES
('301', '最近压力好大', '期末考试要到了，感觉复习不完，每天都很焦虑，睡不着觉。', '3', NOW(), 1),
('302', '今天天气真好', '去公园散步，心情变好了很多。大家也要多出去走走哦！', '5', DATE_SUB(NOW(), INTERVAL 1 HOUR), 1),
('303', '求助：如何处理室友关系', '和室友作息时间不一致，产生了一些矛盾，不知道该怎么沟通。', '3', DATE_SUB(NOW(), INTERVAL 2 HOUR), 1);

-- 插入树洞回复
INSERT INTO treehole_reply (post_id, user_id, content, reply_time) VALUES
('301', '2', '同学你好，不要太焦虑，制定好计划一步步来。如果失眠严重，建议去校医院看看。', DATE_ADD(NOW(), INTERVAL 10 MINUTE)),
('301', '5', '抱抱楼主，我也一样，一起加油！', DATE_ADD(NOW(), INTERVAL 20 MINUTE)),
('303', '4', '建议找个时间心平气和地坐下来聊聊，互相理解和包容。', DATE_ADD(NOW(), INTERVAL 30 MINUTE));

-- 插入心理测试问卷
INSERT INTO mental_test (id, user_id, title, description, content, create_time, threshold) VALUES
('401', '2', '焦虑自评量表(SAS)', '用于评定焦虑症状的轻重程度及其在治疗中的变化。', '[
    {"id": 1, "question": "我觉得比平时容易紧张和着急", "options": [{"label": "没有或很少时间", "score": 1}, {"label": "小部分时间", "score": 2}, {"label": "相当多时间", "score": 3}, {"label": "绝大部分或全部时间", "score": 4}]},
    {"id": 2, "question": "我无缘无故地感到害怕", "options": [{"label": "没有或很少时间", "score": 1}, {"label": "小部分时间", "score": 2}, {"label": "相当多时间", "score": 3}, {"label": "绝大部分或全部时间", "score": 4}]},
    {"id": 3, "question": "我容易心里烦乱或觉得惊恐", "options": [{"label": "没有或很少时间", "score": 1}, {"label": "小部分时间", "score": 2}, {"label": "相当多时间", "score": 3}, {"label": "绝大部分或全部时间", "score": 4}]}
]', NOW(), 50),
('402', '4', '抑郁自评量表(SDS)', '用于评定抑郁症状的轻重程度。', '[
    {"id": 1, "question": "我觉得闷闷不乐，情绪低沉", "options": [{"label": "没有或很少时间", "score": 1}, {"label": "小部分时间", "score": 2}, {"label": "相当多时间", "score": 3}, {"label": "绝大部分或全部时间", "score": 4}]},
    {"id": 2, "question": "我觉得一天之中早晨最好", "options": [{"label": "没有或很少时间", "score": 4}, {"label": "小部分时间", "score": 3}, {"label": "相当多时间", "score": 2}, {"label": "绝大部分或全部时间", "score": 1}]}
]', NOW(), 53);

-- 插入心理测试作答
INSERT INTO mental_test_answer (user_id, test_id, content, answer_time, score) VALUES
('3', '401', '[{"questionId": 1, "optionIndex": 2}, {"questionId": 2, "optionIndex": 1}, {"questionId": 3, "optionIndex": 2}]', NOW(), 5),
('5', '402', '[{"questionId": 1, "optionIndex": 3}, {"questionId": 2, "optionIndex": 2}]', NOW(), 60);

-- 插入收藏
-- 注意：这里使用的是咨询师ID (counselor.id)，而不是用户ID (user.id)
INSERT INTO favourite (student_id, counselor_id) VALUES
('3', '201'),
('5', '202');
