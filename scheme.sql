CREATE DATABASE IF NOT EXISTS mental_health_system;

USE mental_health_system;

# 基础用户表创建
DROP TABLE IF EXISTS user;
CREATE TABLE user(
    id VARCHAR(255) PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(255) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    nickname VARCHAR(255) NOT NULL COMMENT '昵称',
    real_name VARCHAR(5) NOT NULL COMMENT '真实姓名',
    email VARCHAR(255) NOT NULL UNIQUE COMMENT '邮箱',
    avatar VARCHAR(255) NOT NULL COMMENT '头像URL'
) COMMENT '基础用户表';

# 角色表创建
DROP TABLE IF EXISTS role;
CREATE TABLE role(
    id VARCHAR(255) PRIMARY KEY COMMENT '角色ID',
    name VARCHAR(255) NOT NULL UNIQUE COMMENT '角色名'
) COMMENT '角色表';

# 用户角色关系表
DROP TABLE IF EXISTS user_role;
CREATE TABLE user_role(
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id VARCHAR(255) NOT NULL COMMENT '用户ID',
    role_id VARCHAR(255) NOT NULL COMMENT '角色ID',
    UNIQUE (user_id)
) COMMENT '用户角色关系表';

# 角色权限关联表
DROP TABLE IF EXISTS role_permission;
CREATE TABLE role_permission(
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    role_id VARCHAR(255) NOT NULL COMMENT '角色ID',
    permission VARCHAR(255) NOT NULL COMMENT '权限',
    UNIQUE (role_id, permission)
) COMMENT '角色权限关系表';

# 咨询师表
DROP TABLE IF EXISTS counselor;
CREATE TABLE counselor(
    id VARCHAR(255) PRIMARY KEY COMMENT '咨询师ID',
    user_id VARCHAR(255) NOT NULL COMMENT '对应用户ID',
    name VARCHAR(5) NOT NULL COMMENT '姓名',
    specialty VARCHAR(255) NOT NULL COMMENT '擅长领域',
    introduction TEXT NOT NULL COMMENT '个人简介',
    rating decimal(2, 1) NOT NULL COMMENT '评分（保留一位小数）'
) COMMENT '咨询师表';

# 预约表
DROP TABLE IF EXISTS appointment;
CREATE TABLE appointment(
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    counselor_id VARCHAR(255) NOT NULL COMMENT '咨询师对应用户ID',
    student_id VARCHAR(255) NOT NULL COMMENT '学生对应用户ID',
    appointment_time DATETIME NOT NULL COMMENT '预约时间',
    type TINYINT NOT NULL COMMENT '预约类型（1：线上；2：线下）',
    status TINYINT UNSIGNED NOT NULL COMMENT '状态（1：待确认；2：未完成；3：已完成；4：爽约）',
    rating TINYINT UNSIGNED NOT NULL COMMENT '评分（1-5）'
) COMMENT '预约表';

# 树洞帖子表
DROP TABLE IF EXISTS treehole_post;
CREATE TABLE treehole_post(
    id VARCHAR(255) PRIMARY KEY COMMENT '帖子ID',
    title VARCHAR(255) NOT NULL COMMENT '帖子标题',
    content LONGTEXT NOT NULL COMMENT '内容',
    user_id VARCHAR(255) NOT NULL COMMENT '发帖人用户ID',
    post_time TIMESTAMP NOT NULL COMMENT '发帖时间',
    pass TINYINT NOT NULL COMMENT '审核是否通过（0：不通过； 1：通过）'
) COMMENT '树洞帖子表';

# 树洞帖子回复表
DROP TABLE IF EXISTS treehole_reply;
CREATE TABLE treehole_reply(
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    post_id VARCHAR(255) NOT NULL COMMENT '帖子ID',
    user_id VARCHAR(255) NOT NULL COMMENT '发送回复的用户ID',
    content LONGTEXT NOT NULL COMMENT '回复内容',
    reply_time TIMESTAMP NOT NULL COMMENT '回复时间'
) COMMENT '树洞帖子回复表';

# 心理健康测试问卷表
DROP TABLE IF EXISTS mental_test;
CREATE TABLE mental_test(
    id VARCHAR(255) PRIMARY KEY COMMENT '主键ID',
    user_id VARCHAR(255) NOT NULL COMMENT '发布问卷咨询师对应用户ID',
    title VARCHAR(255) NOT NULL COMMENT '问卷标题',
    description TEXT NOT NULL COMMENT '问卷描述',
    content JSON NOT NULL COMMENT '问卷内容',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    threshold TINYINT UNSIGNED NOT NULL COMMENT '警告阈值'
) COMMENT '心理健康测试问卷表';

# 心理测试问卷作答表
DROP TABLE IF EXISTS mental_test_answer;
CREATE TABLE mental_test_answer(
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id VARCHAR(255) NOT NULL COMMENT '作答学生对应用户ID',
    test_id VARCHAR(255) NOT NULL COMMENT '问卷ID',
    content JSON NOT NULL COMMENT '作答',
    answer_time DATETIME NOT NULL COMMENT '作答时间',
    score TINYINT UNSIGNED NOT NULL COMMENT '得分I（0-100）'
) COMMENT '心理测试问卷作答表';

# 收藏表
DROP TABLE IF EXISTS favourite;
CREATE TABLE favourite(
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    student_id VARCHAR(255) NOT NULL COMMENT '学生对应用户ID',
    counselor_id VARCHAR(255) NOT NULL COMMENT '咨询师对应用户ID'
) COMMENT '收藏表';

# 对话记录表
DROP TABLE IF EXISTS dialog;
CREATE TABLE dialog(
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    counselor_id VARCHAR(255) NOT NULL COMMENT '咨询师ID',
    content LONGTEXT NOT NULL COMMENT  '对话记录',
    dialog_time TIMESTAMP NOT NULL COMMENT '对话时间'
);

# 内置数据
INSERT INTO role(id, name) VALUES
                               ('1528734928371712000', '学生'),
                               ('1528734928371712001', '咨询师'),
                               ('1528734928371712002', '管理员');