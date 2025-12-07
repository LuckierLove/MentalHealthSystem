/**
 * 角色相关接口
 */
import request from '@/utils/request'

/**
 * 获取角色列表
 * @param {Object} params - 查询参数 {page, pageSize, keyword}
 * @returns {Promise}
 */
export function getRoleList(params) {
  return request({
    url: '/role/list',
    method: 'get',
    params
  })
}

/**
 * 获取角色详情
 * @param {String} roleId - 角色ID
 * @returns {Promise}
 */
export function getRoleInfo(roleId) {
  return request({
    url: `/role/${roleId}`,
    method: 'get'
  })
}

export function createRole(data) {
  return request({
    url: '/role',
    method: 'post',
    data
  })
}

export function updateRole(roleId, data) {
  return request({
    url: `/role/${roleId}`,
    method: 'put',
    data
  })
}

export function deleteRole(roleId) {
  return request({
    url: `/role/${roleId}`,
    method: 'delete'
  })
}
