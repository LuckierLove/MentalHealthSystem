/**
 * 用户相关接口
 */
import request from '@/utils/request'

/**
 * 获取当前用户信息（角色、ID等）
 * @returns {Promise}
 */
export function getCurrentUserInfo() {
  return request({
    url: '/user/info',
    method: 'get'
  })
}

/**
 * 获取用户详细信息
 * @param {String} userId - 用户ID
 * @returns {Promise}
 */
export function getUserInfo(userId) {
  return request({
    url: `/user/${userId}`,
    method: 'get'
  })
}
