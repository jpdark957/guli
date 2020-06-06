<template>
  <div class="app-container">
    <h2 style="text-align: center;">发布新课程</h2>

    <el-steps :active="1" process-status="wait" align-center style="margin-bottom: 40px;">
      <el-step title="填写课程基本信息" />
      <el-step title="创建课程大纲" />
      <el-step title="提交审核" />
    </el-steps>

    <el-form label-width="120px">
      <el-form-item label="课程标题">
        <el-input v-model="courseInfo.title" placeholder=" 示例：机器学习项目课：从基础到搭建项目视频课程。专业名称注意大小写" />
      </el-form-item>

      <!-- 所属分类 TODO -->
      <!-- 课程分类 -->
      <el-form-item label="课程分类">
        <el-select
          v-model="courseInfo.subjectParentId"
          placeholder="一级分类"
          @change="subjectLevelOneChanged"
        >
          <el-option
            v-for="subject in subjectNestedList"
            :key="subject.id"
            :label="subject.title"
            :value="subject.id"
          />
        </el-select>

        <el-select v-model="courseInfo.subjectId" placeholder="二级分类">
          <el-option
            v-for="subject in subSubjectList"
            :key="subject.id"
            :label="subject.title"
            :value="subject.id"
          />
        </el-select>
      </el-form-item>

      <!-- 课程讲师 TODO -->
      <!-- 课程讲师 -->
      <el-form-item label="课程讲师">
        <el-select v-model="courseInfo.teacherId" placeholder="请选择">
          <el-option
            v-for="teacher in teacherList"
            :key="teacher.id"
            :label="teacher.name"
            :value="teacher.id"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="总课时">
        <el-input-number
          :min="0"
          v-model="courseInfo.lessonNum"
          controls-position="right"
          placeholder="请填写课程的总课时数"
        />
      </el-form-item>

      <!-- 课程简介-->
      <el-form-item label="课程简介">
        <tinymce :height="300" v-model="courseInfo.description" />
      </el-form-item>

      <!-- 课程封面 TODO -->
      <el-form-item label="课程封面">
        <el-upload
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
          :action="BASE_API+'/eduoss/fileoss'"
          class="avatar-uploader"
        >
          <img :src="courseInfo.cover" />
        </el-upload>
      </el-form-item>

      <el-form-item label="课程价格">
        <el-input-number
          :min="0"
          v-model="courseInfo.price"
          controls-position="right"
          placeholder="免费课程请设置为0元"
        />元
      </el-form-item>

      <el-form-item>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate">保存并下一步</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import course from "@/api/edu/course";
import teacher from "@/api/edu/teacher";
import subject from "@/api/edu/subject";
import Tinymce from "@/components/Tinymce";
export default {
  components: { Tinymce },
  data() {
    return {
      saveBtnDisabled: false, // 保存按钮是否禁用
      courseInfo: {
        title: "",
        subjectId: "", //二级分类ID
        subjectParentId: "", //一级分类ID
        teacherId: "",
        lessonNum: 0,
        description: "",
        cover:
          "https://jpdark.oss-cn-beijing.aliyuncs.com/guli/teacher/avatar/ec2377972cd74e57a27f7026400d9e43file.png",
        price: 0
      },
      BASE_API: process.env.BASE_API, // 接口API地址
      teacherList: [],
      subjectNestedList: [], //一级分类列表
      subSubjectList: [], //二级分类列表
      courseId: ""
    };
  },
  created() {
    //获取路由id值
    if (this.$route.params && this.$route.params.id) {
      this.courseId = this.$route.params.id;
      this.getInfo();
    } else {
      this.getListTeacher();
      this.getOneSubject();
    }
  },
  methods: {
    //根据id回显数据
    getInfo() {
      course.getCourseInfo(this.courseId).then(res => {
        this.courseInfo = res.data.courseInfoVo;
        subject.getSubjectList().then(res => {
          this.subjectNestedList = res.data.list;
          for (let i = 0; i < this.subjectNestedList.length; i++) {
            if (
              this.subjectNestedList[i].id == this.courseInfo.subjectParentId
            ) {
              this.subSubjectList = this.subjectNestedList[i].children;
            }
          }
        });
        this.getListTeacher();
      });
    },
    //课程封面上传成功后调用的方法
    handleAvatarSuccess(res, file) {
      this.courseInfo.cover = res.data.url;
    },
    //课程封面上传之前调用的方法
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg";
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error("上传头像图片只能是 JPG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isJPG && isLt2M;
    },
    //点击一级分类 遍历查询二级分类
    subjectLevelOneChanged(value) {
      for (let i = 0; i < this.subjectNestedList.length; i++) {
        if (value == this.subjectNestedList[i].id) {
          this.subSubjectList = this.subjectNestedList[i].children;
          this.courseInfo.subjectId = "";
        }
      }
    },
    //查询所有的一级分类
    getOneSubject() {
      subject.getSubjectList().then(res => {
        this.subjectNestedList = res.data.list;
      });
    },
    //查询所有讲师
    getListTeacher() {
      teacher.teacherList().then(res => {
        this.teacherList = res.data.teacherList;
      });
    },
    addCourse() {
      //添加课程
      course.addCourse(this.courseInfo).then(res => {
        this.$message({
          type: "success",
          message: "添加课程信息成功!"
        });
        this.$router.push({ path: "/course/chapter/" + res.data.courseId });
      });
    },
    updateCourse() {
      //修改课程
      course.updateCourseInfo(this.courseInfo).then(res => {
        this.$message({
          type: "success",
          message: "修改课程信息成功!"
        });
        this.$router.push({ path: "/course/chapter/" + this.$route.params.id });
      });
    },
    saveOrUpdate() {
      if (this.$route.params && this.$route.params.id) {
        this.updateCourse();
      } else {
        this.addCourse();
      }
    }
  }
};
</script>
<style scoped>
.tinymce-container {
  line-height: 29px;
}
</style>