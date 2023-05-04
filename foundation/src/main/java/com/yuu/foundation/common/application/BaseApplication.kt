package com.yuu.foundation.common.application

import android.app.Application
import android.content.res.Configuration
import com.yuuoffice.common.application.BaseAppLifecycle

abstract class BaseApplication : Application() {
    private val classLifecycleList: ArrayList<Class<out BaseAppLifecycle>> = ArrayList()
    private val appLifecycleList: ArrayList<BaseAppLifecycle> = ArrayList()

    override fun onCreate() {
        super.onCreate()
        initResource()
        initAppResource()
        initAppLifecycle()
        initService()
        createAppLifecycle()
    }


    /**初始化资源*/
    abstract fun initResource()

    /**
     * 统一注册应用运行所需资源
     * 若是module所需，尽量放在ModuleAppLifeCycle.onCreate中
     * */
    abstract fun initAppResource()

    /**
     * 统一绑定子 module 与宿主app的生命周期
     * */
    abstract fun initAppLifecycle()

    /**
     * 统一注册 communicate 交互的服务接口
     * */
    abstract fun initService()


    /**
     * 注册生命周期，将需要绑定的module 添加到classLifecycleList进行管理
     * */
    fun registerAppLifecycle(classLifecycle: Class<out BaseAppLifecycle>) {
        classLifecycleList.add(classLifecycle);
    }

    /**
     * 创建子 module 的Lifecycle，添加到appLifecycleList进行管理
     * */
    private fun createAppLifecycle() {
        for (classLifecycle in classLifecycleList) {
            try {
                val appLifecycle = classLifecycle.newInstance()
                appLifecycleList.add(appLifecycle)
                appLifecycle.onCreate()
                appLifecycle.setApplication(this)

            } catch (e: InstantiationException) {
                e.printStackTrace()
            }
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        for (appInit in appLifecycleList) {
            appInit.onTerminate()
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        for (appInit in appLifecycleList) {
            appInit.onLowMemory()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        for (appInit in appLifecycleList) {
            appInit.configurationChanged(newConfig)
        }
    }

}