import request from '@/utils/request'

export function getTotalAppointments() {
  return request({
    url: '/count/appointment',
    method: 'get'
  })
}

export function getTodayAppointments() {
  return request({
    url: '/count/appointment/today',
    method: 'get'
  })
}

export function getCounselorCount() {
  return request({
    url: '/count/counselor',
    method: 'get'
  })
}

export function getUserCount() {
  return request({
    url: '/count/user',
    method: 'get'
  })
}
