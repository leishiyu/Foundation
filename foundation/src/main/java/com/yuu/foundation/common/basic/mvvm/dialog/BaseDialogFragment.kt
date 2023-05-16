package com.yuu.foundation.common.basic.mvvm.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.yuu.foundation.common.ext.AnimatorListenerEx
import com.yuuoffice.view.dialog.extensions.UtilsExtension.Companion.getScreenHeight
import com.yuuoffice.view.dialog.extensions.UtilsExtension.Companion.getScreenHeightOverStatusBar
import com.yuuoffice.view.dialog.extensions.UtilsExtension.Companion.getScreenWidth
import com.yuuoffice.view.dialog.listener.OnKeyListener
import com.yuu.foundation.common.basic.mvvm.dialog.option.DialogGravity
import com.yuu.foundation.common.basic.mvvm.dialog.option.DialogOptions
import com.yuu.foundation.common.basic.mvvm.dialog.option.ViewHolder
import java.util.concurrent.atomic.AtomicBoolean


/**
 * @ClassName : BaseDialogFragment
 * @Description:
 * @Author: XiaRuPeng
 * @Date: 2021/7/23 10:36
 */

open class BaseDialogFragment : DialogFragment() {
    /**
     * 根布局
     */
    private lateinit var rootView: View

    /**
     * 绑定的activity
     */
    private lateinit var mActivity: AppCompatActivity

    /**
     * 执行顺序：2
     * 绑定activity，不建议使用fragment里面自带的getActivity()
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    fun getAppCompatActivity(): AppCompatActivity {
        return mActivity
    }

    /**
     * 是否已经dismiss，避免主动调用dismiss的时候与onStop中触发两次相同事件
     */
    private val dismissed = AtomicBoolean(false)

    /**
     * 保存UI状态的标签
     */
    private val options = "options"

    /**
     * dialog配置,所有配置都写在里面
     */
    var dialogOptions: DialogOptions = DialogOptions()

    /**
     * 继承该基类时可以通过覆写该属性，以提供一个修改DialogOptions的操作
     */
    protected open var compileOverrideOptions: (DialogOptions.() -> Unit)? = null

    /**
     * 使用时修改DialogOptions的操作
     */
    private var runtimeOverrideOptions: (DialogOptions.() -> Unit)? = null

    /**
     * 设置一个在使用时覆写DialogOptions的操作
     */
    fun setRuntimeOverrideOptions(runtimeOverrideOptions: (DialogOptions.() -> Unit)?) {
        this.runtimeOverrideOptions = runtimeOverrideOptions
    }

    /**
     * 懒加载，根据dialogOptions.duration来延迟加载实现懒加载（曲线救国）
     */
    protected fun onLazy() {
        convertView(ViewHolder(rootView), this)
    }

    /**
     * 执行顺序：3
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 根据compile和runtime提供者更新属性
        compileOverrideOptions?.run { this.invoke(dialogOptions) }
        runtimeOverrideOptions?.run { this.invoke(dialogOptions) }
        // 设置dialog样式
        setStyle(dialogOptions.dialogStyle, dialogOptions.dialogThemeFun.invoke(this))
        // 恢复保存的配置
        (savedInstanceState?.getParcelable(options) as? DialogOptions?)?.let { dialogOptions = it }
    }

    /**
     * 执行顺序：4
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //加载布局
        rootView = layoutView() ?: inflater.inflate(dialogOptions.layoutId, container, false)
        //unDisplayViewSize(view)
        if (!dialogOptions.isLazy) {
            convertView(ViewHolder(rootView), this)
        } else {
            //懒加载
            rootView.postDelayed({
                onLazy()
            }, dialogOptions.duration)
        }
        return rootView
    }

    /**
     * 数据绑定到视图/视图控件监听等
     */
    protected fun convertView(holder: ViewHolder, dialogFragment: BaseDialogFragment) {
        dialogOptions.convertListener?.convertView(holder, dialogFragment)
    }

    /**
     *  执行顺序：5
     */
    override fun onStart() {
        super.onStart()
        //初始化配置
        initParams()
    }

    /**
     * 屏幕旋转等导致DialogFragment销毁后重建时保存数据
     * （主要保存dialogOptions中的一些配置属性和监听，数据的保存还需自己手动来）
     *
     * @param outState
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(options, dialogOptions)
    }

    /**
     * 主动dismiss时会在onStop前调用（当采用特殊的view动画时，需要主动调用dismiss，才能生效退出动画）
     */
    override fun dismiss() {
        dialogOptions.exitAnimator.apply {
            //如果没自定义的view动画，那么直接执行
            if (this == null) {
                //如果没有执行过监听操作才执行，并且把监听设为已执行状态
                if (dismissed.compareAndSet(false, true)) {
                    executeDismissListener()
                    if (dialogOptions.allowingStateLoss) {
                        super.dismissAllowingStateLoss()
                    } else {
                        super.dismiss()
                    }

                }
            } else {//如果有动画
                //直接执行动画，该动画已经设置过监听，将会在结束动画时调用super.dismiss()方法
                this.start()
            }
        }

    }

    /**
     * 如果不是主动dismiss，而是点击屏幕或者返回键，
     * 就不会调用dismiss方法，直接走onStop
     * 所以需要在这里也调用dismiss的监听
     */
    override fun onStop() {
        super.onStop()
        //判断时候已经执行过dismiss的监听操作，如果已执行过，那么重新设为未执行监听状态
        if (dismissed.compareAndSet(true, false)) {
            return
        }
        executeDismissListener()
    }

    /**
     * 执行show时候的监听操作
     */
    private fun executeShowListener() {
        for (entry in dialogOptions.showDismissListenerMap.entries) {
            if (entry.value.enableExecuteShowListener) {
                entry.value.onDialogShow()
            }
        }
    }

    /**
     * 执行dismiss时候的监听操作
     */
    private fun executeDismissListener() {
        for (entry in dialogOptions.showDismissListenerMap.entries) {
            if (entry.value.enableExecuteDismissListener) {
                entry.value.onDialogDismiss()
            }
        }
    }

    /**
     * 进入动画的listener
     */
    private lateinit var animatorEnterListener: AnimatorListenerEx

    /**
     * 退出动画的listener
     */
    private lateinit var animatorExitListener: AnimatorListenerEx

    /**
     * 初始化进入动画的listener
     */
    private fun initAnimatorEnterListener(): AnimatorListenerEx {
        return AnimatorListenerEx()
            .onAnimatorStart {
                dialogOptions.canClick = false
            }
            .onAnimatorEnd {
                dialogOptions.canClick = true
            }
            .apply { animatorEnterListener = this }
    }

    /**
     * 初始化退出动画的listener
     */
    private fun initAnimatorExitListener(): AnimatorListenerEx {
        return AnimatorListenerEx()
            .onAnimatorStart {
                dialogOptions.canClick = false
            }
            .onAnimatorEnd {
                //退出动画结束时调用super.dismiss()
                if (dismissed.compareAndSet(false, true)) {
                    executeDismissListener()
                    if (dialogOptions.allowingStateLoss) {
                        super.dismissAllowingStateLoss()
                    } else {
                        super.dismiss()
                    }
                }
                dialogOptions.canClick = true
            }
            .apply { animatorExitListener = this }
    }

    /**
     * 初始化配置
     */
    private fun initParams() {
        //设置dialog的初始化数据
        dialog?.window?.let { window ->
            //设置dialog显示时，布局中view的自定义动画
            dialogOptions.enterAnimatorSupplier?.invoke(window.decorView.findViewById(android.R.id.content))?.let {
                it.addListener(initAnimatorEnterListener())
                dialogOptions.enterAnimator = it
            }
            //设置dialog隐藏时，布局中view的自定义动画
            dialogOptions.exitAnimatorSupplier?.invoke(window.decorView.findViewById(android.R.id.content))?.let {
                it.addListener(initAnimatorExitListener())
                dialogOptions.exitAnimator = it
            }
            //设置dialog的statusBarColor
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = dialogOptions.dialogStatusBarColor
            }
            //设置dialog的statusBar的显示模式
            dialogOptions.statusBarModeFun.invoke(this)
            //设置属性
            window.attributes?.run {
                //调节灰色背景透明度[0-1]，默认0.3f
                dimAmount = dialogOptions.dimAmount
                //设置dialog宽度
                width = if (dialogOptions.width == 0) {
                    WindowManager.LayoutParams.WRAP_CONTENT
                } else {
                    dialogOptions.width
                }
                //设置dialog高度
                height = if (dialogOptions.height == 0) {
                    WindowManager.LayoutParams.WRAP_CONTENT
                } else {
                    dialogOptions.height
                }
                //当左右占满时，设置左右两边的平均边距
                if (dialogOptions.isFullHorizontal) {
                    horizontalMargin = 0f
                    width = getScreenWidth(resources) - 2 * dialogOptions.fullHorizontalMargin
                } else {
                    //没有占满的时候，设置水平方向的相对边距
                    horizontalMargin = when {
                        dialogOptions.horizontalMargin < 0 -> 0f
                        dialogOptions.horizontalMargin in 0f..1f -> dialogOptions.horizontalMargin
                        else -> dialogOptions.horizontalMargin / getScreenWidth(resources)
                    }
                }
                //（不包含statusBar）当上下占满时，设置上下的平均边距
                if (dialogOptions.isFullVertical) {
                    verticalMargin = 0f
                    height = getScreenHeight(resources) - 2 * dialogOptions.fullVerticalMargin
                } else {
                    //没有占满的时候，设置水平方向的相对边距
                    verticalMargin = when {
                        dialogOptions.verticalMargin < 0 -> 0f
                        dialogOptions.verticalMargin in 0f..1f -> dialogOptions.verticalMargin
                        else -> dialogOptions.verticalMargin / getScreenHeight(resources)
                    }
                }
                //（包含StatusBar）真正的全屏
                if (dialogOptions.isFullVerticalOverStatusBar) {
                    verticalMargin = 0f
                    height = getScreenHeightOverStatusBar(mActivity) - 2 * dialogOptions.fullVerticalMargin
                }
                //设置位置(如果设置了asView,那么gravity则永远为LEFT_TOP)
                gravity = dialogOptions.gravity.index
                //如果设置了asView，那么设置dialog的x，y值，将dialog显示在view附近
                if (dialogOptions.isAsView()) {
                    x = dialogOptions.dialogViewX
                    y = dialogOptions.dialogViewY
                }
            }
            //设置dialog进入时内部view的动画,如果animator动画不为空，那么执行animator动画
            dialogOptions.enterAnimator?.start()
            //否则执行animation动画
                ?: apply {
                    dialogOptions.animStyle?.let {
                        window.setWindowAnimations(it)
                    }
                }
        }
        //设置是否点击外部不消失
        isCancelable = dialogOptions.outCancel
        //设置是否点击屏幕区域不消失（点击返回键可消失）
        dialog?.setCanceledOnTouchOutside(dialogOptions.touchCancel)
        //设置按键拦截事件，一般在全屏显示需要重写返回键时用到
        setOnKeyListener()
    }

    /**
     * 重写按钮监听
     */
    private fun setOnKeyListener() {
        //如果设置过特殊的动画，并且没有设置返回建的监听，那么默认设置一个返回键的监听
        if (dialogOptions.exitAnimator != null && dialogOptions.onKeyListener == null) {
            val onKey = object : OnKeyListener() {
                override fun onKey(dialog: DialogInterface, keyCode: Int, event: KeyEvent): Boolean {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        return if (dialogOptions.canClick) {
                            dismiss()
                            true
                        } else {
                            true
                        }
                    }
                    return false
                }
            }
            dialog?.setOnKeyListener(onKey)
        } else {//如果不是特殊动画，或者用户自定义了OnKeyListener，那么直接将onKeyListener设置
            dialogOptions.onKeyListener?.let { onKeyListener ->
                dialog?.setOnKeyListener(onKeyListener)
            }
        }
    }

    /**
     * 将dialog显示在屏幕位置
     * @param manager
     * @param gravity dialog相对于屏幕的位置(默认为上一次设置的位置)
     * @param newAnim 新的动画(默认为上一次设置的动画)
     * @param tag
     */
    fun showOnWindow(manager: FragmentManager, gravity: DialogGravity = dialogOptions.gravity,
                     @StyleRes newAnim: Int? = dialogOptions.animStyle, tag: String? = null,
                     allowingStateLoss: Boolean = true, commitNow: Boolean = false): BaseDialogFragment {
        executeShowListener()
        dialogOptions.apply {
            this.gravity = gravity
            this.animStyle = newAnim
            removeAsView()
            loadAnim()
        }
        show(manager, tag, allowingStateLoss, commitNow)
        return this
    }

    /**
     * 将dialog显示在view附近
     * @param manager
     * @param view 被依赖的view
     * @param gravityAsView 相对于该view的位置(默认为上一次设置的位置)
     * @param newAnim 新的动画(默认为上一次的动画效果)
     * @param offsetX x轴的偏移量，(默认为上一次设置过的偏移量)
     * 偏移量的定义请看{@link DialogOptions#dialogAsView(View,DialogGravity,Int,Int,Int) DialogOptions.dialogAsView}
     * @param offsetY y轴的偏移量，(默认为上一次设置过的偏移量)
     */
    fun showOnView(manager: FragmentManager, view: View,
                   gravityAsView: DialogGravity = dialogOptions.gravityAsView,
                   @StyleRes newAnim: Int? = dialogOptions.animStyle, tag: String? = null,
                   offsetX: Int = dialogOptions.offsetX, offsetY: Int = dialogOptions.offsetY,
                   allowingStateLoss: Boolean = true, commitNow: Boolean = false): BaseDialogFragment {
        executeShowListener()
        dialogOptions.dialogAsView(view, gravityAsView, newAnim, offsetX, offsetY)
        show(manager, tag, allowingStateLoss, commitNow)
        return this
    }

    /**
     * 显示dialog
     */
    private fun show(manager: FragmentManager, tag: String?, allowingStateLoss: Boolean = false, commitNow: Boolean = false) {
        val transaction = manager.beginTransaction()
        val fragmentTag = tag ?: javaClass.simpleName
        manager.findFragmentByTag(fragmentTag)?.run { transaction.remove(this) }
        transaction.add(this, fragmentTag)
        dialogOptions.allowingStateLoss = allowingStateLoss
        dialogOptions.commitNow = commitNow
        when (allowingStateLoss) {
            true -> when (commitNow) {
                true -> transaction.commitNowAllowingStateLoss()
                else -> transaction.commitAllowingStateLoss()
            }
            else -> when (commitNow) {
                true -> transaction.commitNow()
                else -> transaction.commit()
            }
        }
    }

    open fun layoutView():View? =null
}