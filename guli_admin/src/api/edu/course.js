import request from '@/utils/request'
export default {
  //添加课程信息
  addCourse(courseInfo) {
    return request({
      url: '/eduservice/course/addCourseInfo',
      method: 'POST',
      data: courseInfo
    })
  },
  //根据课程id获取信息
  getCourseInfo(id) {
    return request({
      url: `/eduservice/course/getCourseInfo/${id}`,
      method: 'GET'
    })
  },
  //修改课程信息
  updateCourseInfo(courseInfo) {
    return request({
      url: '/eduservice/course/updateCourseInfo',
      method: 'POST',
      data: courseInfo
    })
  },
  //根据课程id查询课程确认信息
  getPublishCourseInfo(courseId) {
    return request({
      url: `/eduservice/course/getPublishCourseInfo/${courseId}`,
      method: "GET"
    })
  },
  //课程最终发布
  publishCourse(courseId) {
    return request({
      url: `/eduservice/course/publishCourse/${courseId}`,
      method: 'POST'
    })
  },

  //删除课程
  deleteCourse(courseId) {
    return request({
      url: `/eduservice/course/deleteCourse/${courseId}`,
      method: 'DELETE'
    })
  },
  //课程列表
  getCourseList() {
    return request({
      url: `/eduservice/course/courseList`,
      method: 'GET'
    })
  },

  //多条件分页查询课程
  pageCourseCondition(current, size, course) {
    return request({
      url: `/eduservice/course/pageCourseCondition/${current}/${size}`,
      method: 'POST',
      data: course
    })
  }
}