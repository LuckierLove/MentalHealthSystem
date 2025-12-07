import request from '@/utils/request'

// 获取对话记录列表（按咨询师ID）
export function getDialogList(page = 1, pageSize = 10, counselorId) {
  return request({
    url: '/dialog/list',
    method: 'get',
    params: {
      page,
      pageSize,
      counselor_id: counselorId
    }
  })
}

// 获取对话记录详情
export function getDialog(id) {
  return request({
    url: `/dialog/${id}`,
    method: 'get'
  })
}

// 创建对话记录
export function createDialog(data) {
  return request({
    url: '/dialog',
    method: 'post',
    data
  })
}

// 更新对话记录
export function updateDialog(id, data) {
  return request({
    url: `/dialog/${id}`,
    method: 'put',
    data
  })
}

// 删除对话记录
export function deleteDialog(id) {
  return request({
    url: `/dialog/${id}`,
    method: 'delete'
  })
}
