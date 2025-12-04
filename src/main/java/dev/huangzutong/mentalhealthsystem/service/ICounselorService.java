package dev.huangzutong.mentalhealthsystem.service;

import dev.huangzutong.mentalhealthsystem.entity.Counselor;
import com.baomidou.mybatisplus.extension.service.IService;
import dev.huangzutong.mentalhealthsystem.entity.req.AddCounselorReq;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetListVO;

import java.util.List;

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

    /**
     * 获取咨询师列表
     * @param page 页码
     * @param pageSize 页大小
     * @param keyword 姓名关键字
     * @return 咨询师列表
     */
    GetListVO<List<Counselor>> getCounselorList(Long page, Long pageSize, String keyword);
}
