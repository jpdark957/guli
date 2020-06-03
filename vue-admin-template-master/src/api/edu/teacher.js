import request from '@/utils/request'

export default {
  /**
   * 获取全部讲师
   */
  teacherList() {
    return request({
      url: '/eduservice/teacher/findAll',
      method: 'get'
    })
  },
  /**
   * 根据条件分页查询讲师
   * @param {*} current 当前页 
   * @param {*} size 每页记录数
   * @param {*} teacherQuery 条件对象
   */
  getTeacherListPage(current, size, teacherQuery) {
    return request({
      url: `/eduservice/teacher/pageTeacherCondition/${current}/${size}`,
      method: 'post',
      /**
       * teacherQuery条件对象，后端使用RequestBody获取数据
       * data表示把对象转换json进行传递到接口里面
       */
      data: teacherQuery
    })
  },
  addTeacher(teacher) {
    return request({
      url: '/eduservice/teacher/addTeacher',
      method: 'post',
      data: teacher
    })
  },
  /**
   * 删除讲师
   * @param {*} id 讲师ID值
   */
  deleteTeacherId(id) {
    return request({
      url: `/eduservice/teacher/${id}`,
      method: 'delete'
    })
  },
  /**
   * 根据ID查询讲师
   * @param {*} id 讲师ID
   */
  getTeacherInfo(id) {
    return request({
      url: `/eduservice/teacher/getTeacher/${id}`,
      method: 'get'
    })
  },
  /**
   * 修改讲师信息
   * @param {*} teacher 修改信息
   */
  updateTeacher(teacher) {
    return request({
      url: `/eduservice/teacher/updateTeacher`,
      method: 'post',
      data: teacher
    })
  }
}