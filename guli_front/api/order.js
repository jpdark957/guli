import request from '@/utils/request'
export default {
  /**
   * 生成订单
   * @param {*} courseId 课程id
   */
  createOrders(courseId) {
    return request({
      url: `/eduorder/order/createOrder/${courseId}`,
      method: 'POST'
    })
  },
  /**
   * 根据订单id查询订单信息
   * @param {*} orderId 订单id
   */
  getOrdersInfo(orderId) {
    return request({
      url: `/eduorder/order/getOrderInfo/${orderId}`,
      method: 'GET'
    })
  },
  /**
   * 生成二维码
   * @param {*} orderNo 订单号
   */
  createNative(orderNo) {
    return request({
      url: `/eduorder/paylog/createNative/${orderNo}`,
      method: 'GET'
    })
  },
  /**
   * 查询订单支付状态
   * @param {*} orderNo 订单号
   */
  queryPayStatus(orderNo) {
    return request({
      url: `/eduorder/paylog/queryPayStatus/${orderNo}`,
      method: 'GET'
    })
  }
}