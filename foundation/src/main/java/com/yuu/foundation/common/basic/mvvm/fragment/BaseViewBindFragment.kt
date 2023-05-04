package com.yuu.foundation.common.basic.mvvm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.yuu.foundation.common.basic.base.BaseFragment


/**
 * @ClassName : BaseViewBindFragment
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/3/15 17:09
 */

abstract class BaseViewBindFragment<VB : ViewBinding> : BaseFragment() {


    private lateinit var binding:VB
    /**
     * 外部获取ViewBinding
     * */
    protected val viewBind get() = binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = injectViewBind(inflater, container)
        return binding.root
    }

    /**
     * 注入ViewBinding
     * */
    protected abstract fun injectViewBind(inflater: LayoutInflater, viewGroup: ViewGroup?): VB

    override fun initLayoutId(): Int? {
        return null
    }
}