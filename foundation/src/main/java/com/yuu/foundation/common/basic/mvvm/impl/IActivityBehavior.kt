package com.yuu.foundation.common.basic.base.impl

/**
 * @InterfaceName : IActivityBehavior
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/3/11 14:29
 */

interface IActivityBehavior {

    /**
     * 不带参数的页面跳转
     */
    fun navigateTo(page: Any)

    /**
     * 返回键点击
     */
    fun backPress(arg: Any?)

    /**
     * 关闭页面
     */
    fun finishPage(arg: Any?)

}