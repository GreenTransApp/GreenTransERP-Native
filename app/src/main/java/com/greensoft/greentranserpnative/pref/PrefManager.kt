package com.greensoft.greentranserpnative.pref

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.ui.login.models.LoginDataModel
import com.greensoft.greentranserpnative.ui.login.models.UserDataModel

class PrefManager {
    fun clearAllPreferences() {
        prefsEditor = myPrefs!!.edit()
        prefsEditor.clear()
        prefsEditor.commit()
    }

    fun setParentLogin(loginModel: LoginDataModel?, tag: String?) {
        val gson = Gson()
        val hashMapString = gson.toJson(loginModel)
        prefsEditor!!.putString(tag, hashMapString)
        prefsEditor!!.apply()
    }

    fun getParentLogin(tag: String?): LoginDataModel? {
        val obj = myPrefs!!.getString(tag, "defValue")
        return if (obj == "defValue") {
            null
        } else {
            val gson = Gson()
            val storedloginHashMapString =
                myPrefs!!.getString(tag, "")
            val logintype = object : TypeToken<LoginDataModel?>() {}.type
            gson.fromJson(storedloginHashMapString, logintype)
        }
    }

    fun setLoginUser(userModel: UserDataModel?, tag: String?) {
        val gson = Gson()
        val hashMapString = gson.toJson(userModel)
        prefsEditor!!.putString(tag, hashMapString)
        prefsEditor!!.apply()
    }

    fun getUser(tag: String?): UserDataModel? {
        val obj = myPrefs!!.getString(tag, "defValue")
        return if (obj == "defValue") {
            null
        } else {
            val gson = Gson()
            val storedloginHashMapString =
                myPrefs!!.getString(tag, "")
            val logintype = object : TypeToken<UserDataModel?>() {}.type
            gson.fromJson(storedloginHashMapString, logintype)
        }
    }

    fun setType(type: String?, tag: String?) {
        prefsEditor!!.putString(tag, type)
        prefsEditor!!.apply()
    }

    fun getType(tag: String?): String? {
        return myPrefs!!.getString(tag, "")
    }

    //    public void setUser(BranchList branchList, String tag) {
    //
    //        Gson gson = new Gson();
    //        String hashMapString = gson.toJson(branchList);
    //
    //        prefsEditor.putString(tag, hashMapString);
    //        prefsEditor.apply();
    //    }
    //
    //
    //    public BranchList getUser(String tag) {
    //        String obj = myPrefs.getString(tag, "defValue");
    //        if (obj.equals("defValue")) {
    //            return new BranchList();
    //        } else {
    //            Gson gson = new Gson();
    //            String storedloginHashMapString = myPrefs.getString(tag, "");
    //            Type logintype = new TypeToken<BranchList>() {
    //            }.getType();
    //            BranchList LoginHashMap = gson.fromJson(storedloginHashMapString, logintype);
    //            return LoginHashMap;
    //        }
    //    }
    fun getLoadType(Tag: String?): String? {
        return myPrefs!!.getString(Tag, "")
    }

    fun setLoadType(Tag: String?, LoadType: String?) {
        prefsEditor!!.putString(Tag, LoadType)
        prefsEditor!!.commit()
    }

    fun isLogin(Tag: String?): Boolean {
        return myPrefs!!.getBoolean(Tag, false)
    }

    fun setLogin(Tag: String?, token: Boolean) {
        prefsEditor!!.putBoolean(Tag, token)
        prefsEditor!!.commit()
    }

    companion object {
        lateinit var myPrefs: SharedPreferences
        lateinit var prefsEditor: SharedPreferences.Editor
        private var myObj: PrefManager? = null
        fun getInstance(ctx: Context?): PrefManager? {
            if (myObj == null) {
                myObj = PrefManager()
                myPrefs = PreferenceManager.getDefaultSharedPreferences(ctx)
                prefsEditor = myPrefs.edit()
            }
            return myObj
        }
    }
}