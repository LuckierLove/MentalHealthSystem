/**
 * 管理端用户相关接口（用户管理）
 */
import request from '@/utils/request'

/**
 * 获取用户列表（分页/搜索）
 * @param {number} page - 页码
 * @param {number} pageSize - 每页数量
 * @param {string} keyword - 搜索关键词
 */
export function getUserList(page = 1, pageSize = 10, keyword = '') {
  return request({
    url: '/user/list',
    method: 'get',
    params: { page, pageSize, keyword }
  })
}

export function getUser(id) {
  return request({
    url: `/user/${id}`,
    method: 'get'
  })
}

export function createUser(data) {
  return request({
    url: '/user',
    method: 'post',
    data
  })
}

export function updateUser(id, data) {
  return request({
    url: `/user/${id}`,
    method: 'put',
    data
  })
}

export function deleteUser(id) {
  return request({
    url: `/user/${id}`,
    method: 'delete'
  })
}
