package com.yuu.foundation.common.basic.mvvm.impl

/**
 * @InterfaceName : IViewBehaviour
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/3/11 10:59
 */

interface IViewBehaviour {

    /**
     * 是否显示Loading视图
     */
    fun showLoadingUI(isShow: Boolean)

    /**
     * 是否显示空白视图
     */
    fun showEmptyUI(isShow: Boolean)

    /**
     * 是否显示网络错误视图
     */
    fun showErrorNetworkUI(isShow: Boolean)

    /**
     * 弹出Toast提示
     */
    fun showToast(msg: String)

}