<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.title" prefix-icon="Search" style="width: 200px; margin-right: 10px" placeholder="按书名查询"></el-input>
      <el-input v-model="data.author" prefix-icon="Search" style="width: 200px; margin-right: 10px" placeholder="按作者查询"></el-input>
      <el-select v-model="data.categoryId" clearable style="width: 180px; margin-right: 10px" placeholder="按分类查询">
        <el-option v-for="item in data.categories" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      <el-button type="info" plain @click="load">查询</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary" plain @click="handleAdd">新增</el-button>
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column label="封面" width="70">
          <template v-slot="scope">
            <el-image style="width: 40px; height: 50px; display: block; border-radius: 3px;" v-if="scope.row.coverImage" :src="scope.row.coverImage" :preview-src-list="[scope.row.coverImage]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="书名" show-overflow-tooltip />
        <el-table-column prop="author" label="作者" />
        <el-table-column prop="publisher" label="出版社" show-overflow-tooltip />
        <el-table-column prop="categoryName" label="分类" />
        <el-table-column prop="stock" label="库存" width="70" />
        <el-table-column prop="isbn" label="ISBN" width="130" />
        <el-table-column label="操作" width="100" fixed="right">
          <template v-slot="scope">
            <el-button type="primary" circle :icon="Edit" @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>

    <el-dialog title="图书信息" v-model="data.formVisible" width="50%" destroy-on-close>
      <el-form ref="form" :model="data.form" label-width="90px" style="padding: 20px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item prop="title" label="书名">
              <el-input v-model="data.form.title" placeholder="请输入书名"></el-input>
            </el-form-item>
            <el-form-item prop="author" label="作者">
              <el-input v-model="data.form.author" placeholder="请输入作者"></el-input>
            </el-form-item>
            <el-form-item prop="publisher" label="出版社">
              <el-input v-model="data.form.publisher" placeholder="请输入出版社"></el-input>
            </el-form-item>
            <el-form-item prop="categoryId" label="分类">
              <el-select v-model="data.form.categoryId" placeholder="请选择分类" style="width: 100%">
                <el-option v-for="item in data.categories" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
            <el-form-item prop="isbn" label="ISBN">
              <el-input v-model="data.form.isbn" placeholder="请输入ISBN"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="publishDate" label="出版日期">
              <el-date-picker v-model="data.form.publishDate" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
            </el-form-item>
            <el-form-item prop="stock" label="库存数量">
              <el-input-number v-model="data.form.stock" :min="0" style="width: 100%" />
            </el-form-item>
            <el-form-item prop="coverImage" label="封面图片">
              <el-upload :action="baseUrl + '/files/upload'" :on-success="handleFileUpload" list-type="picture">
                <el-button type="primary" size="small">点击上传</el-button>
              </el-upload>
            </el-form-item>
            <el-form-item prop="description" label="简介">
              <el-input type="textarea" :rows="3" v-model="data.form.description" placeholder="请输入简介"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";

const baseUrl = import.meta.env.VITE_BASE_URL

const data = reactive({
  formVisible: false,
  form: {},
  tableData: [],
  categories: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  title: null,
  author: null,
  categoryId: null,
  ids: []
})

const loadCategories = () => {
  request.get('/bookCategory/selectAll').then(res => {
    if (res.code === '200') data.categories = res.data || []
  })
}

const load = () => {
  request.get('/book/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title,
      author: data.author,
      categoryId: data.categoryId
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || []
      data.total = res.data?.total
    }
  })
}

const handleAdd = () => {
  data.form = { stock: 1 }
  data.formVisible = true
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const add = () => {
  request.post('/book/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () => {
  request.put('/book/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    }
  })
}

const save = () => {
  data.form.id ? update() : add()
}

const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/book/delete/' + id).then(res => {
      if (res.code === '200') { ElMessage.success("删除成功"); load() }
      else { ElMessage.error(res.msg) }
    })
  }).catch(err => {})
}

const delBatch = () => {
  if (!data.ids.length) { ElMessage.warning("请选择数据"); return }
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete("/book/delete/batch", {data: data.ids}).then(res => {
      if (res.code === '200') { ElMessage.success('操作成功'); load() }
    })
  }).catch(err => {})
}

const handleSelectionChange = (rows) => {
  data.ids = rows.map(v => v.id)
}

const handleFileUpload = (res) => {
  data.form.coverImage = res.data
}

const reset = () => {
  data.title = null
  data.author = null
  data.categoryId = null
  load()
}

loadCategories()
load()
</script>
