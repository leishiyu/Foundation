package com.yuu.foundation.common.basic.base

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.yuu.foundation.common.basic.mvvm.impl.IViewBehaviour


/**
 * @ClassName : BaseFragment
 * @Description:
 * feature: initLayoutId模板方法
 *
 * feature: initView模板方法
 *
 * feature: initData模板方法
 *
 * feature: initListener模板方法
 * @Author: WuZhuoyu
 * @Date: 2021/3/15 14:16
 */

abstract class BaseFragment :Fragment(), IViewBehaviour {

    protected val onBackPressDispatcher by lazy { requireActivity().onBackPressedDispatcher }
    protected var onBackPressCallback:OnBackPressedCallback?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initLayoutId()?.let {
            return inflater.inflate(it, container, false)
        }?:let{
            return super.onCreateView(inflater, container, savedInstanceState)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(savedInstanceState)
        initListener(savedInstanceState)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData(savedInstanceState)
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


}