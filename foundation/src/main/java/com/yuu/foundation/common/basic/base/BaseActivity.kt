package com.yuu.foundation.common.basic.base

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.yuu.foundation.common.basic.base.impl.IActivityBehavior
import com.yuu.foundation.common.basic.mvvm.impl.IViewBehaviour


/**
 * @ClassName : BaseActivity
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

abstract class BaseActivity : AppCompatActivity(), IActivityBehavior, IViewBehaviour {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    protected abstract fun initLayoutId():View?

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

}