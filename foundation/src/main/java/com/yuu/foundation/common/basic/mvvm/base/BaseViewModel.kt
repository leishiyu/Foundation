package com.yuu.foundation.common.basic.mvvm.base

import android.util.Log
import androidx.lifecycle.*
import com.yuu.foundation.common.basic.mvvm.impl.IViewBehaviour
import com.yuu.foundation.common.basic.base.impl.IViewModelLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


/**
 * @ClassName : BaseViewModel
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/3/9 12:17
 */

abstract class BaseViewModel: ViewModel(), IViewModelLifecycle, IViewBehaviour {

    lateinit var lifecycleOwner: LifecycleOwner

    /**
     * loading视图显示Event
     * */
    var loadingEvent = MutableLiveData<Boolean>()
        private set

    /**
     *  无数据视图显示Event
     * */
    var emptyPageEvent = MutableLiveData<Boolean>()
        private set

    /**
     *  网络错误视图显示Event
     * */
    var errorNetworkPageEvent = MutableLiveData<Boolean>()
        private set

    /**
     * toast提示Event
     * */
    var toastEvent = MutableLiveData<String>()
        private set

    /**
     * 在主线程中执行一个协程
     */
    protected fun launchOnUI(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(Dispatchers.Main) { block() }
    }

    /**
     * 在IO线程中执行一个协程
     */
    protected fun launchOnIO(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            try {
                block()
            } catch (e: Exception) {
                Log.e("xia", e.toString())
            }
        }
    }

    override fun onAny(owner: LifecycleOwner, event: Lifecycle.Event) {
        this.lifecycleOwner = owner
    }

    override fun onCreate() {
        TODO("Not yet implemented")
    }

    override fun onStart() {
        TODO("Not yet implemented")
    }

    override fun onResume() {
        TODO("Not yet implemented")
    }

    override fun onPause() {
        TODO("Not yet implemented")
    }

    override fun onStop() {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        TODO("Not yet implemented")
    }

    /**
     * ViewModel调用方法通过LiveData通知
     * 具体的观察者在BaseViewModelActivity or BaseViewModelFragment 实现
     * */

    /**
     * 通知需要展示加载弹窗
     * @param isShow
     * */
    override fun showLoadingUI(isShow: Boolean) {
        loadingEvent.postValue(isShow)
    }

    /**
     * 通知是否需要展示数据空布局
     * @param isShow */
    override fun showEmptyUI(isShow: Boolean) {
        emptyPageEvent.postValue(isShow)
    }

    /**
     * 通知是否需要展示网络错误布局
     * @param isShow
     * */
    override fun showErrorNetworkUI(isShow: Boolean) {
        errorNetworkPageEvent.postValue(isShow)
    }

    /**
     * 通知是否展示Toast
     * @param msg
     * */
    override fun showToast(msg: String) {
        toastEvent.postValue(msg)
    }

    fun <T> Flow<T>.loading(): Flow<T> {
        return this.onStart {
            showLoadingUI(true)
        }.onCompletion {
            showLoadingUI(false)
        }
    }

    fun <T> Flow<T>.catch(): Flow<T> {
        return this.catch {
            showToast(it.message ?: it.toString())
            Log.e("flow catch","BaseViewModel ${it.message ?: it.toString()}")
        }
    }

}