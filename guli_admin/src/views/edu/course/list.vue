<template>
  <div class="app-container">
    <!--查询表单-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="courseQuery.title" placeholder="课程名称" />
      </el-form-item>

      <el-form-item>
        <el-select v-model="courseQuery.status" clearable placeholder="课程状态">
          <el-option value="Normal" label="已发布" />
          <el-option value="Draft" label="未发布" />
        </el-select>
      </el-form-item>

      <el-button type="primary" icon="el-icon-search" @click="getCourseList()">查询</el-button>
      <el-button type="default" @click="resetData()">清空</el-button>
    </el-form>

    <el-table
      v-loading="listLoading"
      :data="courseList"
      element-loading-text="数据加载中"
      border
      fit
      highlight-current-row
    >
      <el-table-column label="序号" width="70" align="center">
        <template slot-scope="scope">{{ (page - 1) * size + scope.$index + 1 }}</template>
      </el-table-column>

      <el-table-column prop="title" label="课程名称" />

      <el-table-column label="课程状态" width="80">
        <template slot-scope="scope">{{ scope.row.status==='Normal'?'已发布':'未发布' }}</template>
      </el-table-column>

      <el-table-column prop="lessonNum" label="课时数" width="80" />

      <el-table-column prop="gmtCreate" label="添加时间" width="160" />

      <el-table-column prop="viewCount" label="浏览量" width="60" />

      <el-table-column label="操作" width="450" align="center">
        <template slot-scope="scope">
          <router-link :to="'/course/info/'+scope.row.id">
            <el-button type="primary" size="mini" icon="el-icon-edit">编辑课程基本信息</el-button>
          </router-link>
          <router-link :to="'/course/chapter/'+scope.row.id">
            <el-button type="primary" size="mini" icon="el-icon-edit">编辑课程大纲</el-button>
          </router-link>
          <el-button
            type="danger"
            size="mini"
            icon="el-icon-delete"
            @click="removeDataById(scope.row.id)"
          >删除课程信息</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      :current-page="page"
      :page-size="size"
      :total="total"
      style="padding: 30px 0; text-align: center;"
      layout="total, prev, pager, next, jumper"
      @current-change="getCourseList"
    />
  </div>
</template>

<script>
import course from "@/api/edu/course";
export default {
  data() {
    return {
      courseList: null,
      page: 1,
      size: 8,
      total: 0,
      courseQuery: {},
      listLoading: true
    };
  },
  created() {
    this.getCourseList();
  },
  methods: {
    removeDataById(value) {
      this.$confirm(
        "此操作将永久删除该课程，以及该课程下的章节和视频，是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(() => {
          return course.deleteCourse(value);
        })
        .then(() => {
          this.getCourseList();
          this.$message({
            type: "success",
            message: "删除成功!"
          });
        })
        .catch(res => {
          // 失败
          if (res === "cancel") {
            this.$message({
              type: "info",
              message: "已取消删除"
            });
          }
        });
    },
    subjectLevelOneChanged() {},
    getList() {},
    resetData() {
      this.courseQuery = {};
      this.getCourseList();
    },
    getCourseList(page = 1) {
      this.page = page;
      course
        .pageCourseCondition(this.page, this.size, this.courseQuery)
        .then(res => {
          this.total = res.data.total;
          this.courseList = res.data.rows;
          this.listLoading = false;
        });
    }
  }
};
</script>

