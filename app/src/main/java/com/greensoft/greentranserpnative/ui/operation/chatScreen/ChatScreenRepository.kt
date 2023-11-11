package com.greensoft.greentranserpnative.ui.operation.chatScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greensoft.greentranserpnative.base.BaseRepository
import com.greensoft.greentranserpnative.common.CommonResult
import com.greensoft.greentranserpnative.ui.operation.chatScreen.models.ChatScreenModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ChatScreenRepository @Inject constructor() : BaseRepository(){
    private val getChatListMuteData = MutableLiveData<ArrayList<ChatScreenModel>>()
    private val addChatListMuteData = MutableLiveData<ArrayList<ChatScreenModel>>()
    private val deleteChatListMuteData = MutableLiveData<ArrayList<ChatScreenModel>>()

    val getChatListLiveData: LiveData<ArrayList<ChatScreenModel>>
        get() = getChatListMuteData
    val addChatListLiveData: LiveData<ArrayList<ChatScreenModel>>
        get() = addChatListMuteData
    val deleteChatListLiveData: LiveData<ArrayList<ChatScreenModel>>
        get() = deleteChatListMuteData

    val viewDialogLiveData: LiveData<Boolean>
        get() = viewDialogMutData


    fun getChats(companyId:String,spname: String,param:List<String>, values:ArrayList<String>){
        viewDialogMutData.postValue(true)
        api.commonApiHome(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult1(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<ChatScreenModel>>() {}.type
                            val resultList: ArrayList<ChatScreenModel> = gson.fromJson(jsonArray.toString(), listType);
                            getChatListMuteData.postValue(resultList);

                        }
                    } else {
                        isError.postValue(result.CommandMessage.toString());
                    }
                } else {
                    isError.postValue(SERVER_ERROR);
                }
                viewDialogMutData.postValue(false)

            }

            override fun onFailure(call: Call<CommonResult?>, t: Throwable) {
                viewDialogMutData.postValue(false)
                isError.postValue(t.message)
            }

        })
    }
    fun addChats(companyId:String, spname: String, param:List<String>, values:ArrayList<String>){
        viewDialogMutData.postValue(true)
        api.commonApiHome(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<ChatScreenModel>>() {}.type
                            val resultList: ArrayList<ChatScreenModel> = gson.fromJson(jsonArray.toString(), listType);
                            addChatListMuteData.postValue(resultList);

                        }
                    } else {
                        isError.postValue(result.CommandMessage.toString());
                    }
                } else {
                    isError.postValue(SERVER_ERROR);
                }
                viewDialogMutData.postValue(false)

            }

            override fun onFailure(call: Call<CommonResult?>, t: Throwable) {
                viewDialogMutData.postValue(false)
                isError.postValue(t.message)
            }

        })
    }

    fun deleteChats(companyId:String, spname: String, param:List<String>, values:ArrayList<String>){
        viewDialogMutData.postValue(true)
        api.commonApiHome(companyId,spname, param,values).enqueue(object: Callback<CommonResult> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult>) {
                if(response.body() != null){
                    val result = response.body()!!
                    val gson = Gson()
                    if(result.CommandStatus == 1){
                        val jsonArray = getResult(result);
                        if(jsonArray != null) {
                            val listType = object: TypeToken<List<ChatScreenModel>>() {}.type
                            val resultList: ArrayList<ChatScreenModel> = gson.fromJson(jsonArray.toString(), listType);
                            addChatListMuteData.postValue(resultList);

                        }
                    } else {
                        isError.postValue(result.CommandMessage.toString());
                    }
                } else {
                    isError.postValue(SERVER_ERROR);
                }
                viewDialogMutData.postValue(false)

            }

            override fun onFailure(call: Call<CommonResult?>, t: Throwable) {
                viewDialogMutData.postValue(false)
                isError.postValue(t.message)
            }

        })
    }


}