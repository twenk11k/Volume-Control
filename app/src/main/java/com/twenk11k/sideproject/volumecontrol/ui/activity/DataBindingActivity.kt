package com.twenk11k.sideproject.volumecontrol.ui.activity

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

open class DataBindingActivity : AppCompatActivity() {

    protected inline fun <reified T : ViewDataBinding> binding(@LayoutRes resId: Int): Lazy<T> =
            lazy { DataBindingUtil.setContentView(this, resId) }

}