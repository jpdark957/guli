import request from '@/utils/request'
export default {
  /**
   * 分页查询讲师
   * @param {*} page 当前页
   * @param {*} size 每页记录数
   */
  getTeacherList(page, size) {
    return request({
      url: `/eduservice/teacherfront/getTeacherFrontList/${page}/${size}`,
      method: "POST",
    })
  },
  /**
   * 讲师详情的方法
   */
  getTeacherInfo(id) {
    return request({
      url: `/eduservice/teacherfront/getTeacherFrontInfo/${id}`,
      method: 'GET'
    })
  }
}