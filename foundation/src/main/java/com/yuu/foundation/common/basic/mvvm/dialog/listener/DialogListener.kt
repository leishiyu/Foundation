package com.yuuoffice.view.dialog.listener

import android.os.Parcel
import android.os.Parcelable


/**
 * @ClassName : DialogListener
 * @Description:
 * @Author: XiaRuPeng
 * @Date: 2021/7/23 10:50
 */

class DialogListener : Parcelable {

    /*****************对外暴露元素*******************/

    /**
     * 是否在dialog显示的时候执行onDialogShow()方法
     */
    var enableExecuteShowListener = true

    /**
     * 是否在dialog关闭的时候执行onDialogDismiss()方法
     */
    var enableExecuteDismissListener = true

    /**
     * 设置在dialog显示的时候执行的回调方法
     */
    fun onDialogShow(listener: () -> Unit) {
        dialogShowFun = listener
    }

    /**
     * 设置在dialog关闭的时候执行的回调方法
     */
    fun onDialogDismiss(listener: () -> Unit) {
        dialogDismissFun = listener
    }

    /*****************模块内部使用*******************/

    /**
     * dialog显示回调
     */
    private var dialogShowFun: (() -> Unit)? = null

    /**
     * dialog关闭回调
     */
    private var dialogDismissFun: (() -> Unit)? = null

    /**
     * 模块内dialog显示回调函数执行调用
     */
    internal fun onDialogShow() {
        dialogShowFun?.invoke()
    }

    /**
     * 模块内dialog关闭回调函数执行调用
     */
    internal fun onDialogDismiss() {
        dialogDismissFun?.invoke()
    }

    constructor()

    /*****************Parcelable相关*******************/

    constructor(source: Parcel) : this(
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {}

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<DialogListener> = object : Parcelable.Creator<DialogListener> {
            override fun createFromParcel(source: Parcel): DialogListener = DialogListener(source)
            override fun newArray(size: Int): Array<DialogListener?> = arrayOfNulls(size)
        }
    }
}