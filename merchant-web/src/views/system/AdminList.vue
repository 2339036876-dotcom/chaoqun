<template>
  <div class="admin-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>管理员管理</span>
          <el-button type="primary" @click="handleAdd">添加管理员</el-button>
        </div>
      </template>
      
      <el-table :data="adminList" style="width: 100%">
        <el-table-column prop="userId" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="nickname" label="昵称" />
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="state" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.state === 1 ? 'success' : 'danger'">
              {{ scope.row.state === 1 ? '可用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="loginTime" label="最后登录" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="warning" size="small" @click="handleResetPassword(scope.row.userId)">重置密码</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row.userId)" :disabled="scope.row.username === 'admin'">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
    
    <!-- 添加/编辑管理员对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
    >
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.state" placeholder="请选择状态">
            <el-option label="可用" value="1" />
            <el-option label="禁用" value="2" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import request from '../../utils/request'

export default {
  data() {
    return {
      adminList: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      dialogVisible: false,
      dialogTitle: '',
      form: {
        userId: '',
        username: '',
        nickname: '',
        phone: '',
        email: '',
        password: '',
        state: 1
      }
    }
  },
  mounted() {
    this.loadAdminList()
  },
  methods: {
    loadAdminList() {
      request.get('/api/admin/list', {
        params: {
          page: this.currentPage,
          size: this.pageSize
        }
      }).then(res => {
        if (res.code === 0) {
          this.adminList = res.data.records
          this.total = res.data.total
        }
      })
    },
    handleAdd() {
      this.dialogTitle = '添加管理员'
      this.form = {
        userId: '',
        username: '',
        nickname: '',
        phone: '',
        email: '',
        password: '',
        state: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑管理员'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleSubmit() {
      if (this.form.userId) {
        // 编辑
        request.put('/api/admin/edit', this.form).then(res => {
          if (res.code === 0) {
            this.$message.success('编辑成功')
            this.dialogVisible = false
            this.loadAdminList()
          } else {
            this.$message.error(res.msg)
          }
        })
      } else {
        // 添加
        request.post('/api/admin/add', this.form).then(res => {
          if (res.code === 0) {
            this.$message.success('添加成功')
            this.dialogVisible = false
            this.loadAdminList()
          } else {
            this.$message.error(res.msg)
          }
        })
      }
    },
    handleDelete(id) {
      this.$confirm('确定要删除该管理员吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request.delete(`/api/admin/delete/${id}`).then(res => {
          if (res.code === 0) {
            this.$message.success('删除成功')
            this.loadAdminList()
          } else {
            this.$message.error(res.msg)
          }
        })
      })
    },
    handleResetPassword(id) {
      this.$confirm('确定要重置该管理员的密码吗？重置后密码为123456', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request.put(`/api/admin/resetPassword/${id}`).then(res => {
          if (res.code === 0) {
            this.$message.success('密码重置成功')
          } else {
            this.$message.error(res.msg)
          }
        })
      })
    },
    handleSizeChange(size) {
      this.pageSize = size
      this.loadAdminList()
    },
    handleCurrentChange(current) {
      this.currentPage = current
      this.loadAdminList()
    }
  }
}
</script>

<style scoped>
.admin-list {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
