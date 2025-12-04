package dev.huangzutong.mentalhealthsystem.service;

import dev.huangzutong.mentalhealthsystem.entity.Counselor;
import com.baomidou.mybatisplus.extension.service.IService;
import dev.huangzutong.mentalhealthsystem.entity.req.AddCounselorReq;

/**
 * 咨询师表 服务类
 */
public interface ICounselorService extends IService<Counselor> {

    /**
     * 添加咨询师
     * @param req 添加咨询师请求参数
     */
    void addCounselor(AddCounselorReq req);

    /**
     * 删除咨询师
     * @param id 咨询师id
     */
    void removeCounselor(String id);

    /**
     * 更新咨询师信息
     * @param id 咨询师id
     * @param req 更新咨询师请求参数
     */
    void updateCounselor(String id, AddCounselorReq req);
}
