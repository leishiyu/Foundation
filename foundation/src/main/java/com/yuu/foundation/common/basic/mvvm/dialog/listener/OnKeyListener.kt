package com.yuuoffice.view.dialog.listener

import android.content.DialogInterface
import android.os.Parcel
import android.os.Parcelable
import android.view.KeyEvent


/**
 * @ClassName : OnKeyListener
 * @Description:
 * @Author: XiaRuPeng
 * @Date: 2021/7/23 10:53
 */

abstract class OnKeyListener : DialogInterface.OnKeyListener, Parcelable {


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {}

    constructor() {}

    protected constructor(source: Parcel) {}

    companion object {

        val CREATOR: Parcelable.Creator<OnKeyListener> = object : Parcelable.Creator<OnKeyListener> {
            override fun createFromParcel(source: Parcel): OnKeyListener {
                return object : OnKeyListener(source) {
                    override fun onKey(dialog: DialogInterface, keyCode: Int, event: KeyEvent): Boolean {
                        return false
                    }
                }
            }

            override fun newArray(size: Int): Array<OnKeyListener?> {
                return arrayOfNulls(size)
            }
        }
    }
}