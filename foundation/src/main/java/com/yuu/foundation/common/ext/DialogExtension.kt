package com.yuu.foundation.common.ext

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.yuu.foundation.common.basic.mvvm.dialog.BaseDialogFragment
import com.yuu.foundation.common.basic.mvvm.dialog.option.DialogOptions


/**
 * @ClassName : DialogExtension
 * @Description:
 * @Author: XiaRuPeng
 * @Date: 2021/7/23 10:58
 */

/**
 * AppCompatActivity中，在window上显示对话框
 */
fun AppCompatActivity.showDialogFragmentOnWindow(baseDialogFragment: BaseDialogFragment, tag: String? = null,
                                                 allowingStateLoss: Boolean = true, commitNow: Boolean = false) {
    baseDialogFragment.showOnWindow(supportFragmentManager, tag = tag, allowingStateLoss = allowingStateLoss, commitNow = commitNow)
}

/**
 * AppCompatActivity中，在view处显示对话框
 */
fun AppCompatActivity.showDialogFragmentOnView(view: View, baseDialogFragment: BaseDialogFragment, tag: String? = null,
                                               allowingStateLoss: Boolean = true, commitNow: Boolean = false) {
    baseDialogFragment.showOnView(supportFragmentManager, view, tag = tag, allowingStateLoss = allowingStateLoss, commitNow = commitNow)
}

/**
 * SupportFragment中，在window上显示对话框
 */
fun Fragment.showDialogFragmentOnWindow(baseDialogFragment: BaseDialogFragment, tag: String? = null,
                                        allowingStateLoss: Boolean = true, commitNow: Boolean = false) {
    baseDialogFragment.showOnWindow(childFragmentManager, tag = tag, allowingStateLoss = allowingStateLoss, commitNow = commitNow)
}

/**
 * SupportFragment中，在view处显示对话框
 */
fun Fragment.showDialogFragmentOnView(baseDialogFragment: BaseDialogFragment, view: View, tag: String? = null,
                                      allowingStateLoss: Boolean = true, commitNow: Boolean = false) {
    baseDialogFragment.showOnView(childFragmentManager, view, tag = tag, allowingStateLoss = allowingStateLoss, commitNow = commitNow)
}

/**
 * 创建一个dialog
 * 你也可以继承BaseDialog，实现更多功能，并通过扩展函数来简化创建过程
 */
fun baseDialogFragment(runtimeOverrideOptions: (DialogOptions.() -> Unit)? = null): BaseDialogFragment {
    return BaseDialogFragment().apply { setRuntimeOverrideOptions(runtimeOverrideOptions) }
}

