package dev.huangzutong.mentalhealthsystem.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import dev.huangzutong.mentalhealthsystem.entity.MentalTest;
import dev.huangzutong.mentalhealthsystem.entity.MentalTestAnswer;
import dev.huangzutong.mentalhealthsystem.mapper.MentalTestAnswerMapper;
import dev.huangzutong.mentalhealthsystem.mapper.MentalTestMapper;
import dev.huangzutong.mentalhealthsystem.service.IMentalTestAnswerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 心理测试问卷作答表 服务实现类
 */
@Service
public class MentalTestAnswerServiceImpl extends ServiceImpl<MentalTestAnswerMapper, MentalTestAnswer> implements IMentalTestAnswerService {
    @Resource
    private MentalTestAnswerMapper mentalTestAnswerMapper;
    @Resource
    private MentalTestMapper mentalTestMapper;
    /**
     * 创建心理测试问卷作答
     * @param req 创建心理测试问卷作答参数
     */
    @Override
    public void createAnswer(MentalTestAnswer req) {
        req.setUserId(StpUtil.getLoginId().toString());
        req.setAnswerTime(LocalDateTime.now());
        req.setScore(getScore(req.getTestId(), req.getContent()));
        save(req);
    }

    /**
     * 获取心理测试问卷作答分数
     * @param testId 心理测试问卷id
     * @param answer 作答内容
     * @return 作答分数
     */
    public Integer getScore(String testId, String answer) {
        MentalTest mentalTest = mentalTestMapper.selectById(testId);
        JSONArray questions = JSONArray.parse(mentalTest.getContent());
        JSONArray answers = JSONArray.parse(answer);

        Integer score = 0;
        for (int i = 0; i < answers.size(); i++) {
            JSONObject as = answers.getJSONObject(i);
            Integer questionIndex = as.getInteger("question");
            Integer choose = as.getInteger("choose");
            JSONObject questionContent = questions.getJSONObject(questionIndex - 1);
            Integer value = questionContent.getJSONArray("choose").getJSONObject(choose - 1).getInteger("value");
            score += value;
        }
        return score;
    }
}
