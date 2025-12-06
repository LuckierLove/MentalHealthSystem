/**
 * 用户状态管理
 * 管理用户登录状态、token、角色、权限等信息
 */
import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getCurrentUserInfo } from '@/api/user'
import { getCurrentPermissions } from '@/api/permission'

export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref(localStorage.getItem('token') || '')
  const userId = ref('')
  const roles = ref([])
  const permissions = ref([])
  
  /**
   * 设置 token
   * @param {String} newToken - 新的 token
   */
  function setToken(newToken) {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }
  
  /**
   * 设置用户信息
   * @param {Object} userInfo - 用户信息
   */
  function setUserInfo(userInfo) {
    userId.value = userInfo.id || ''
    roles.value = userInfo.role || []
  }
  
  /**
   * 设置用户权限
   * @param {Array} perms - 权限列表
   */
  function setPermissions(perms) {
    permissions.value = perms
  }
  
  /**
   * 获取用户信息
   * 从后端获取当前用户的角色和权限
   */
  async function fetchUserInfo() {
    try {
      // 获取用户基本信息（角色、ID）
      const userRes = await getCurrentUserInfo()
      if (userRes.data) {
        setUserInfo(userRes.data)
      }
      
      // 获取用户权限
      const permRes = await getCurrentPermissions()
      if (permRes.data && permRes.data.permission) {
        setPermissions(permRes.data.permission)
      }
    } catch (error) {
      console.error('获取用户信息失败:', error)
      throw error
    }
  }
  
  /**
   * 登出
   * 清除本地存储的用户信息
   */
  function logout() {
    token.value = ''
    userId.value = ''
    roles.value = []
    permissions.value = []
    localStorage.removeItem('token')
  }
  
  /**
   * 检查是否有某个角色
   * @param {String} role - 角色名称
   * @returns {Boolean}
   */
  function hasRole(role) {
    return roles.value.includes(role)
  }
  
  /**
   * 检查是否有某个权限
   * @param {String} permission - 权限标识
   * @returns {Boolean}
   */
  function hasPermission(permission) {
    return permissions.value.includes(permission)
  }
  
  return {
    token,
    userId,
    roles,
    permissions,
    setToken,
    setUserInfo,
    setPermissions,
    fetchUserInfo,
    logout,
    hasRole,
    hasPermission
  }
})
