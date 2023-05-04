package com.yuu.foundation.common.basic.mvvm.activity

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.yuu.foundation.common.basic.base.BaseActivity


/**
 * @ClassName : BaseViewBindActivity
 * @Description: 基于ViewBind的Activity的基类
 * @Author: WuZhuoyu
 * @Date: 2021/3/5 11:37
 */
abstract class BaseViewBindActivity<VB : ViewBinding> : BaseActivity(){

    private lateinit var viewBinding:VB
    /**
     * 外部获取ViewBinding
     * */
    protected val viewBind get() = viewBinding

    override fun initContentView(savedInstanceState: Bundle?) {
        //绑定ViewBinding
        viewBinding = injectViewBind()
        super.initContentView(savedInstanceState)
    }

    /**
     * 注入ViewBinding
     * */
    protected abstract fun injectViewBind(): VB

    /**
     * 使用viewBinding不需要初始化layoutId
     * */
    override fun initLayoutId(): View? = viewBinding.root

}