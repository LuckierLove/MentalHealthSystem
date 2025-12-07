import request from '@/utils/request'

/**
 * 创建收藏
 * @param {string} counselorId - 咨询师ID
 * @returns {Promise}
 */
export function createFavourite(counselorId) {
  return request({
    url: '/favourite',
    method: 'post',
    data: { counselor_id: counselorId }
  })
}

/**
 * 获取收藏列表
 * @param {number} page - 页码
 * @param {number} pageSize - 页大小
 * @param {string} studentId - 学生用户ID
 * @returns {Promise}
 */
export function getFavouriteList(page = 1, pageSize = 10, studentId) {
  return request({
    url: '/favourite/list',
    method: 'get',
    params: {
      page,
      pageSize,
      student_id: studentId
    }
  })
}

/**
 * 删除收藏
 * @param {number} favouriteId - 收藏ID
 * @returns {Promise}
 */
export function deleteFavourite(favouriteId) {
  return request({
    url: `/favourite/${favouriteId}`,
    method: 'delete'
  })
}
