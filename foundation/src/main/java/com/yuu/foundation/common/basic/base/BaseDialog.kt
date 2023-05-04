package com.yuu.foundation.common.basic.base

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.annotation.LayoutRes


/**
 * @ClassName : BaseDialog
 * @Description:
 *
 * feature: initLayoutId模板方法
 *
 * feature: initView模板方法
 *
 * feature: initData模板方法
 *
 * feature: initListener模板方法
 *
 * @Author: WuZhuoyu
 * @Date: 2021/3/5 11:15
 */

abstract class BaseDialog(context: Context, themeResId: Int) : Dialog(context,themeResId){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var mContext = this.context

        initContentView(savedInstanceState)

        initView(savedInstanceState)
        initData(savedInstanceState)
        initListener(savedInstanceState)

    }


    protected open fun initContentView(savedInstanceState: Bundle?) {
        initLayoutId()?.let {
            setContentView(it)
        }
    }


    /**============================================================
     *  抽象方法
     **===========================================================*/

    /**
     * 初始化LayoutId
     * 提供视图布局Id, 如果 [.initLayoutId] 返回 null, 框架则不会调用 [Activity.setContentView]
     */
    @LayoutRes
    protected abstract fun initLayoutId(): Int?

    /**
     * 初始化View
     * @param savedInstanceState
     */
    protected abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 初始化数据
     * @param savedInstanceState
     */
    protected abstract fun initData(savedInstanceState: Bundle?)

    /**
     * 初始化监听
     * @param savedInstanceState
     */
    protected abstract fun initListener(savedInstanceState: Bundle?)

    /**
     * 初始化主题
     * */
    protected abstract fun initStyleTheme()
}