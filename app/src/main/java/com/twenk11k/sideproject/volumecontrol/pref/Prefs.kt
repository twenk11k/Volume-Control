package com.twenk11k.sideproject.volumecontrol.pref

import android.content.Context
import android.content.SharedPreferences
import com.twenk11k.sideproject.volumecontrol.App
import kotlin.reflect.KProperty

abstract class Prefs(private val name: String? = null) {

    private val prefs: SharedPreferences by lazy {
        App.getContext().getSharedPreferences(name ?: javaClass.simpleName, Context.MODE_PRIVATE)
    }

    abstract class PrefDelegate<T>(val prefKey: String?) {
        abstract operator fun getValue(thisRef: Any?, property: KProperty<*>): T
        abstract operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)
    }

    enum class Type {
        Int
    }

    fun intPref(prefKey: String? = null, defaultValue: Int = 0) = GenericPrefDelegate(prefKey, defaultValue, Type.Int)

    @Suppress("UNCHECKED_CAST")
    inner class GenericPrefDelegate<T>(prefKey: String? = null, private val defaultValue: T, val type: Type) :
            PrefDelegate<T>(prefKey) {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T =
                prefs.getInt(prefKey ?: property.name, defaultValue as Int) as T

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            prefs.edit().putInt(prefKey ?: property.name, value as Int).apply()
        }
    }

}