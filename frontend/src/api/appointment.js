import request from '../utils/request'

/**
 * 获取预约列表
 */
export function getAppointmentList(page, pageSize, studentId, counselorId) {
  return request({
    url: '/appointment/list',
    method: 'get',
    params: {
      page,
      pageSize,
      studentId,
      counselorId
    }
  })
}

/**
 * 创建预约
 */
export function createAppointment(data) {
  return request({
    url: '/appointment',
    method: 'post',
    data
  })
}

/**
 * 取消预约
 */
export function cancelAppointment(id) {
  return request({
    url: `/appointment/${id}`,
    method: 'delete'
  })
}

/**
 * 获取预约详情
 */
export function getAppointment(id) {
  return request({
    url: `/appointment/${id}`,
    method: 'get'
  })
}

/**
 * 更新预约（评分）
 */
export function updateAppointment(id, data) {
  return request({
    url: `/appointment/${id}`,
    method: 'put',
    data
  })
}
