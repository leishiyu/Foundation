package com.yuuoffice.common.application

import android.app.Application
import android.content.res.Configuration

/**
 * 将组件生命周期与app生命周期绑定
 * */
 abstract class BaseAppLifecycle {

    lateinit var mApplication: Application

    fun BaseAppLifecycle() {}

    fun setApplication(application: Application) {
        this.mApplication = application
    }

    abstract fun onCreate()

    abstract fun onTerminate()

    abstract fun onLowMemory()

    abstract fun configurationChanged(configuration: Configuration)

}