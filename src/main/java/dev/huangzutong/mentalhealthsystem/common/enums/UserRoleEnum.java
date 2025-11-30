package dev.huangzutong.mentalhealthsystem.common.enums;

/**
 * 内置用户角色枚举
 */
public enum UserRoleEnum {

    STUDENT("1528734928371712000", "学生"),
    COUNSELOR("1528734928371712001", "咨询师"),
    ADMIN("1528734928371712002", "管理员");

    private final String id;
    private final String name;

    UserRoleEnum(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
