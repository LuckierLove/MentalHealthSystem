<template>
  <div>
    <el-card>
      <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:12px">
        <el-input v-model="query.keyword" placeholder="按角色名搜索" style="width:300px" clearable @clear="fetchRoles" @keyup.enter.native="fetchRoles" />
        <div>
          <el-button type="primary" @click="openCreate">新建角色</el-button>
        </div>
      </div>

      <el-table :data="roles" stripe style="width:100%">
        <el-table-column prop="id" label="ID" width="180" />
        <el-table-column prop="name" label="角色名" />
        <el-table-column label="操作" width="240">
          <template #default="{ row }">
            <el-button size="small" @click="editRole(row)">编辑</el-button>
            <el-button size="small" @click="assignPerms(row)">权限</el-button>
            <el-button size="small" type="danger" @click="removeRole(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog title="角色" v-model="dialogVisible" width="400px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="角色名" prop="name">
          <el-input v-model="form.name" style="width: 260px" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveRole">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog title="分配权限" v-model="permDialogVisible" width="600px">
      <div style="margin-bottom:8px">
        <el-input v-model="newPerm" placeholder="输入权限标识后按回车添加" @keyup.enter.native="addPerm" clearable />
      </div>
      <div style="min-height:120px">
        <el-tag v-for="p in selectedPerms" :key="p" closable @close="removePerm(p)" style="margin:6px">{{ p }}</el-tag>
        <div v-if="selectedPerms.length===0" class="muted">该角色暂无权限，可通过输入添加</div>
      </div>
      <template #footer>
        <el-button @click="permDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="savePerms">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getRoleList, createRole, updateRole, deleteRole, getRoleInfo } from '@/api/role'
import { getRolePermissions, updateRolePermissions } from '@/api/permission'

const roles = ref([])
const query = reactive({ page: 1, pageSize: 20, keyword: '' })
const dialogVisible = ref(false)
const formRef = ref(null)
const permDialogVisible = ref(false)
const form = reactive({ id: null, name: '' })
const rules = {
  name: [{ required: true, message: '请输入角色名', trigger: 'blur' }]
}
const allPerms = ref([])
const selectedPerms = ref([])
const newPerm = ref('')
let currentRoleId = null

function fetchRoles() {
  getRoleList({ page: query.page, pageSize: query.pageSize, keyword: query.keyword }).then(res => {
    roles.value = res.data?.records || res.data?.list || []
  }).catch(()=>{})
}

function openCreate() { form.id = null; form.name=''; dialogVisible.value = true }
function editRole(row) { form.id = row.id; form.name = row.name; dialogVisible.value = true }
async function saveRole() {
  try {
    await formRef.value.validate()
    if (form.id) await updateRole(form.id, form)
    else await createRole(form)
    dialogVisible.value=false; fetchRoles()
  } catch (e) {}
}
async function removeRole(row) {
  try {
    const { ElMessageBox, ElMessage } = await import('element-plus')
    await ElMessageBox.confirm(`确认删除角色 ${row.name} ?`, '删除确认', { type: 'warning' })
    await deleteRole(row.id)
    ElMessage.success('删除成功')
    fetchRoles()
  } catch (e) {}
}

function assignPerms(row) {
  currentRoleId = row.id
  // 仅获取该角色已有权限，后端并没有提供所有权限的接口
  getRolePermissions(row.id).then(roleRes => {
    selectedPerms.value = roleRes.data?.permission || roleRes.data || []
    permDialogVisible.value = true
  }).catch(()=>{ selectedPerms.value = []; permDialogVisible.value = true })
}

function addPerm() {
  const v = newPerm.value && newPerm.value.trim()
  if (v && !selectedPerms.value.includes(v)) {
    selectedPerms.value.push(v)
  }
  newPerm.value = ''
}

function removePerm(p) {
  selectedPerms.value = selectedPerms.value.filter(it => it !== p)
}

function savePerms(){
  // 后端期望字段名为 permission: []
  updateRolePermissions(currentRoleId, { permission: selectedPerms.value }).then(()=>{ permDialogVisible.value=false })
}

onMounted(fetchRoles)
</script>

<style scoped></style>
