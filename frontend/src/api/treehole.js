import request from '@/utils/request'

export function getPostList(page = 1, pageSize = 10, keyword, pass) {
  return request({
    url: '/treehole/post/list',
    method: 'get',
    params: { page, pageSize, keyword, pass }
  })
}

export function getPost(id) {
  return request({
    url: `/treehole/post/${id}`,
    method: 'get'
  })
}

export function createPost(data) {
  return request({
    url: '/treehole/post',
    method: 'post',
    data
  })
}

export function updatePost(id, data) {
  return request({
    url: `/treehole/post/${id}`,
    method: 'put',
    data
  })
}

export function deletePost(id) {
  return request({
    url: `/treehole/post/${id}`,
    method: 'delete'
  })
}

export function listReplies(postId) {
  return request({
    url: `/treehole/reply/list/${postId}`,
    method: 'get'
  })
}

export function createReply(data) {
  return request({
    url: '/treehole/reply',
    method: 'post',
    data
  })
}

export function updateReply(id, data) {
  return request({
    url: `/treehole/reply/${id}`,
    method: 'put',
    data
  })
}

export function deleteReply(id) {
  return request({
    url: `/treehole/reply/${id}`,
    method: 'delete'
  })
}
