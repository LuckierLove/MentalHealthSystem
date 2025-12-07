import request from '@/utils/request'

/**
 * 获取咨询师列表
 * @param {number} page - 页码
 * @param {number} pageSize - 每页数量
 * @param {string} keyword - 搜索关键词
 * @returns {Promise}
 */
export function getCounselorList(page = 1, pageSize = 10, keyword = '') {
  return request({
    url: '/counselor/list',
    method: 'get',
    params: { page, pageSize, keyword }
  })
}

/**
 * 获取咨询师详情
 * @param {string} id - 咨询师ID
 * @returns {Promise}
 */
export function getCounselor(id) {
  return request({
    url: `/counselor/${id}`,
    method: 'get'
  })
}

/**
 * 创建咨询师
 * @param {Object} data - 咨询师信息
 * @param {string} data.user_id - 对应用户ID
 * @param {string} data.name - 姓名
 * @param {string} data.specialty - 擅长领域
 * @param {string} data.introduction - 个人简介
 * @returns {Promise}
 */
export function createCounselor(data) {
  return request({
    url: '/counselor',
    method: 'post',
    data
  })
}

/**
 * 更新咨询师
 * @param {string} id - 咨询师ID
 * @param {Object} data - 咨询师信息
 * @returns {Promise}
 */
export function updateCounselor(id, data) {
  return request({
    url: `/counselor/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除咨询师
 * @param {string} id - 咨询师ID
 * @returns {Promise}
 */
export function deleteCounselor(id) {
  return request({
    url: `/counselor/${id}`,
    method: 'delete'
  })
}
