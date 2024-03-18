package com.rfcreations.inventorymanager.repository.userpreferencerepository

interface UserPreferenceRepository {

    fun getIntPref(key: String, defValue: Int): Int
    fun getStringPref(key: String, defValue: String): String?
    fun getBooleanPref(key: String, defValue: Boolean): Boolean

    fun editIntPref(key: String, newValue: Int)
    fun editStringPref(key: String, newValue: String)
    fun editBooleanPref(key: String, newValue: Boolean)

}