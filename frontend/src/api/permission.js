/**
 * 权限相关接口
 */
import request from '@/utils/request'

/**
 * 获取当前用户权限列表
 * @returns {Promise}
 */
export function getCurrentPermissions() {
  return request({
    url: '/ps/current',
    method: 'get'
  })
}

/**
 * 获取指定角色的权限列表
 * @param {String} roleId - 角色ID
 * @returns {Promise}
 */
export function getRolePermissions(roleId) {
  return request({
    url: `/ps/${roleId}`,
    method: 'get'
  })
}
