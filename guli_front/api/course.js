import request from '@/utils/request'
export default {
  /**
   * 条件分页课程查询
   * @param {*} page 当前页
   * @param {*} size 每页记录数
   * @param {*} searchObj 搜索条件
   */
  getCourseList(page, size, searchObj) {
    return request({
      url: `/eduservice/coursefront/getFrontCourseList/${page}/${size}`,
      method: 'POST',
      data: searchObj
    })
  },
  /**
   * 查询所有分类
   */
  getAllSubject() {
    return request({
      url: `/eduservice/subject/getAllSubject`,
      method: 'GET'
    })
  },
  /**
   * 课程详情
   * @param {*} id 课程id
   */
  getCourseInfo(courseId) {
    return request({
      url: `/eduservice/coursefront/getFrontCourseInfo/${courseId}`,
      method: 'GET'
    })
  }
}