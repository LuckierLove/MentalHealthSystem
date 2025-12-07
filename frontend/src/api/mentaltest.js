import request from '@/utils/request'

// 获取问卷列表
export function getTestList(page = 1, pageSize = 10, keyword, counselorId) {
  return request({
    url: '/mt/list',
    method: 'get',
    params: { page, pageSize, keyword, counselor_id: counselorId }
  })
}

// 创建问卷
export function createTest(data) {
  return request({
    url: '/mt',
    method: 'post',
    data
  })
}

// 修改问卷
export function updateTest(id, data) {
  return request({
    url: `/mt/${id}`,
    method: 'put',
    data
  })
}

// 删除问卷
export function deleteTest(id) {
  return request({
    url: `/mt/${id}`,
    method: 'delete'
  })
}

// 创建作答
export function createAnswer(data) {
  return request({
    url: '/mta',
    method: 'post',
    data
  })
}

// 获取作答列表
export function getAnswerList(page = 1, pageSize = 10, testId, userId) {
  return request({
    url: '/mta/list',
    method: 'get',
    params: { page, pageSize, test_id: testId, user_id: userId }
  })
}

// 获取作答详情
export function getAnswer(id) {
  return request({
    url: `/mta/${id}`,
    method: 'get'
  })
}

// 删除作答
export function deleteAnswer(id) {
  return request({
    url: `/mta/${id}`,
    method: 'delete'
  })
}
