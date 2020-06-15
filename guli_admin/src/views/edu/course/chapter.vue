<template>
  <div class="app-container">
    <h2 style="text-align: center;">发布新课程</h2>

    <el-steps :active="2" process-status="wait" align-center style="margin-bottom: 40px;">
      <el-step title="填写课程基本信息" />
      <el-step title="创建课程大纲" />
      <el-step title="提交审核" />
    </el-steps>
    <el-button type="text" @click="openChapterDialog">添加章节</el-button>
    <!-- 章节 -->
    <ul class="chanpterList">
      <li v-for="chapter in chatperVideoList" :key="chapter.id">
        <p>
          {{ chapter.title }}
          <span class="acts">
            <el-button type="text" @click="openVideo(chapter.id)">添加小节</el-button>
            <el-button style type="text" @click="openEditChapter(chapter.id)">编辑</el-button>
            <el-button type="text" @click="removeChapter(chapter)">删除</el-button>
          </span>
        </p>

        <!-- 视频 -->
        <ul class="chanpterList videoList">
          <li v-for="video in chapter.children" :key="video.id">
            <p>
              {{ video.title }}
              <span class="acts">
                <el-button type="text" @click="openEditVideo(video)">编辑</el-button>
                <el-button type="text" @click="removeVideo(video)">删除</el-button>
              </span>
            </p>
          </li>
        </ul>
      </li>
    </ul>

    <div>
      <el-button @click="previous">上一步</el-button>
      <el-button :disabled="saveBtnDisabled" type="primary" @click="next">下一步</el-button>
    </div>

    <!-- 添加和修改章节表单 -->
    <el-dialog :visible.sync="dialogChapterFormVisible" :title="from_chapter">
      <el-form :model="chapter" label-width="120px">
        <el-form-item label="章节标题">
          <el-input v-model="chapter.title" />
        </el-form-item>
        <el-form-item label="章节排序">
          <el-input-number v-model="chapter.sort" :min="0" controls-position="right" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogChapterFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdate">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 添加和修改课时表单 -->
    <el-dialog :visible.sync="dialogVideoFormVisible" :title="from_video">
      <el-form :model="video" label-width="120px">
        <el-form-item label="课时标题">
          <el-input v-model="video.title" />
        </el-form-item>
        <el-form-item label="课时排序">
          <el-input-number v-model="video.sort" :min="0" controls-position="right" />
        </el-form-item>
        <el-form-item label="是否免费">
          <el-radio-group v-model="video.isFree">
            <el-radio :label="true">免费</el-radio>
            <el-radio :label="false">默认</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="上传视频">
          <el-upload
            :on-success="handleVodUploadSuccess"
            :on-remove="handleVodRemove"
            :before-remove="beforeVodRemove"
            :on-exceed="handleUploadExceed"
            :file-list="fileList"
            :action="BASE_API+'/eduvod/video/uploadAlyVideo'"
            :limit="1"
            class="upload-demo"
          >
            <el-button size="small" type="primary">上传视频</el-button>
            <el-tooltip placement="right-end">
              <div slot="content">
                最大支持1G，
                <br />支持3GP、ASF、AVI、DAT、DV、FLV、F4V、
                <br />GIF、M2T、M4V、MJ2、MJPEG、MKV、MOV、MP4、
                <br />MPE、MPG、MPEG、MTS、OGG、QT、RM、RMVB、
                <br />SWF、TS、VOB、WMV、WEBM 等视频格式上传
              </div>
              <i class="el-icon-question" />
            </el-tooltip>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVideoFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdateVideo">确 定</el-button>
        <!-- :disabled="saveVideoBtnDisabled" -->
      </div>
    </el-dialog>
  </div>
</template>

<script>
import chapter from "@/api/edu/chapter";
import video from "@/api/edu/video";
export default {
  data() {
    return {
      saveBtnDisabled: false, // 保存按钮是否禁用
      chatperVideoList: [],
      courseId: "",
      from_chapter: "",
      from_video: "",
      chapter: {
        title: "",
        sort: 0
      },
      video: {
        title: "",
        sort: 0,
        free: 0,
        videoSourceId: null,
        videoOriginalName: null
      },
      dialogChapterFormVisible: false, //章节弹框
      dialogVideoFormVisible: false, //小节弹框
      fileList: [], //上传文件列表
      BASE_API: process.env.BASE_API // 接口API地址
    };
  },
  created() {
    //获取路由的id值
    if (this.$route.params && this.$route.params.id) {
      this.courseId = this.$route.params.id;
      this.getChapterVideo();
    }
  },
  methods: {
    //上传视频成功调用的方法
    handleVodUploadSuccess(res, file, fileList) {
      this.video.videoSourceId = res.data.videoId;
      this.video.videoOriginalName = file.name;
      this.fileList = fileList;
      console.log(res)
      console.log(fileList)
    },
    handleUploadExceed() {
      this.$message.warning("想要重新上传视频，请先删除已上传的视频");
    },
    //确定删除视频调用的方法
    handleVodRemove() {
      video.deleteAlyvod(this.video.videoSourceId).then(res => {
        this.$message({
          type: "success",
          message: "删除视频成功!"
        });
        this.fileList = [];
        this.video.videoSourceId = "";
        this.video.videoOriginalName = "";
      });
      video.deleteVidAndName(this.video.id)
    },
    //点击x调用这个方法
    beforeVodRemove(file, fileList) {
      return this.$confirm(`确定移除${file.name}?`);
    },
    //========================小节管理============================//
    //修改弹框数据回显
    openEditVideo(videoInfo) {
      this.from_video = "修改小节";
      this.dialogVideoFormVisible = true;
      video.getVideoInfo(videoInfo.id).then(res => {
        this.video = res.data.video;
        if (res.data.video.videoOriginalName != null
          && res.data.video.videoOriginalName.length > 0
        ) {
          this.fileList = [
            {
              name: res.data.video.videoOriginalName
            }
          ];
        }
      });
    },

    //修改小节
    updateVideo() {
      video.updateVideo(this.video).then(res => {
        this.dialogVideoFormVisible = false;
        this.$message({
          type: "success",
          message: "修改小节成功!"
        });
        this.getChapterVideo();
      });
    },
    //删除小节
    removeVideo(value) {
      this.$confirm("此操作将删除小节, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true
      })
        .then(() => {
          video.deleteVideo(value.id).then(res => {
            this.$message({
              type: "success",
              message: "删除成功!"
            });
            this.getChapterVideo();
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },

    //添加小节弹框的方法
    openVideo(chapterId) {
      this.video = {
        title: "",
        sort: 0
      };
      this.fileList = [];
      this.video.courseId = this.courseId;
      this.video.chapterId = chapterId;
      this.from_video = "添加小节";
      this.dialogVideoFormVisible = true;
    },
    //添加小节
    addVideo() {
      video.addVideo(this.video).then(res => {
        this.dialogVideoFormVisible = false;
        this.$message({
          type: "success",
          message: "添加小节成功!"
        });
        this.getChapterVideo();
        this.video = {
          title: "",
          sort: 0,
          free: 0,
          videoSourceId: ""
        };
      });
    },
    saveOrUpdateVideo() {
      if (!this.video.id) {
        this.addVideo();
      } else {
        this.updateVideo();
      }
    },

    //========================章节管理============================//
    //删除章节
    removeChapter(value) {
      this.$confirm("此操作将删除章节, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true
      })
        .then(() => {
          if (value.children.length > 0) {
            this.$message({
              type: "info",
              message: "该章节含有小节,无法删除!"
            });
          } else {
            chapter.deleteChapter(value.id).then(res => {
              this.$message({
                type: "success",
                message: "删除成功!"
              });
              this.getChapterVideo();
            });
          }
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    //弹出添加章节页面
    openChapterDialog() {
      this.from_title = "添加章节";
      this.dialogChapterFormVisible = true;
      this.chapter = {
        title: "",
        sort: 0
      };
    },
    //修改章节弹框数据回显
    openEditChapter(id) {
      this.from_title = "修改章节";
      this.dialogChapterFormVisible = true;
      chapter.getChapter(id).then(res => {
        this.chapter = res.data.chapter;
      });
    },
    //添加章节
    addChapter() {
      this.chapter.courseId = this.courseId;
      chapter.addChapter(this.chapter).then(res => {
        this.dialogChapterFormVisible = false;
        this.$message({
          type: "success",
          message: "添加章节成功!"
        });
        this.getChapterVideo();
      });
    },
    //修改章节功能
    updateChapter() {
      chapter.updateChapter(this.chapter).then(res => {
        this.dialogChapterFormVisible = false;
        this.$message({
          type: "success",
          message: "修改章节成功!"
        });
        this.getChapterVideo();
      });
    },
    saveOrUpdate() {
      if (!this.chapter.id) {
        this.addChapter();
      } else {
        this.updateChapter();
      }
    },
    //根据课程id查询章节和小节
    getChapterVideo() {
      chapter.getAllChapterVideo(this.courseId).then(res => {
        this.chatperVideoList = res.data.allChapterVideo;
      });
    },
    previous() {
      this.$router.push({ path: `/course/info/${this.courseId}` });
    },
    next() {
      this.$router.push({ path: `/course/publish/${this.courseId}` });
    }
  }
};
</script>
<style scoped>
.chanpterList {
  position: relative;
  list-style: none;
  margin: 0;
  padding: 0;
}
.chanpterList li {
  position: relative;
}
.chanpterList p {
  float: left;
  font-size: 20px;
  margin: 10px 0;
  padding: 10px;
  height: 70px;
  line-height: 50px;
  width: 100%;
  border: 1px solid #ddd;
}
.chanpterList .acts {
  float: right;
  font-size: 14px;
}

.videoList {
  padding-left: 50px;
}
.videoList p {
  float: left;
  font-size: 14px;
  margin: 10px 0;
  padding: 10px;
  height: 50px;
  line-height: 30px;
  width: 100%;
  border: 1px dotted #ddd;
}
</style>