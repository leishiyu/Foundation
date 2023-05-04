package com.yuu.foundation.common.basic.mvvm.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.yuu.foundation.common.basic.mvvm.fragment.BaseViewBindFragment


/**
 * @ClassName : BaseViewModelFragment
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/3/15 18:06
 */

abstract class BaseViewModelFragment<VM : BaseViewModel, VB : ViewBinding>:
    BaseViewBindFragment<VB>(){

    private lateinit var _viewModel:VM

    protected val viewModel get() =_viewModel

    /**
     * 绑定ViewModel
     *
     * */
    protected abstract fun injectViewModel():VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setViewModel(injectViewModel())
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setViewModel(viewModel:VM) {
        _viewModel = viewModel
    }

    override fun showLoadingUI(isShow: Boolean) {
        TODO("Not yet implemented")
    }

    override fun showEmptyUI(isShow: Boolean) {
        TODO("Not yet implemented")
    }

    override fun showErrorNetworkUI(isShow: Boolean) {
        TODO("Not yet implemented")
    }

    override fun showToast(msg: String) {
        TODO("Not yet implemented")
    }

}