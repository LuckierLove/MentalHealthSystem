/**
 * 认证相关接口
 */
import request from '@/utils/request'

/**
 * 用户登录
 * @param {Object} data - 登录数据 {username, password}
 * @returns {Promise}
 */
export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

/**
 * 用户注册
 * @param {Object} data - 注册数据 {username, password, nickname, real_name, email, avatar}
 * @returns {Promise}
 */
export function register(data) {
  return request({
    url: '/auth/register',
    method: 'post',
    data
  })
}
