package com.example.ui.adapter.convertpdf.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class viewmodel():ViewModel() {
     var list: MutableLiveData<MutableList<Uri>> = MutableLiveData()

   fun getImages(): MutableList<Uri>? {
       return list.value
   }
    fun setPictureList(picture: MutableList<Uri>) {
        val list1 = ArrayList<Uri>()
        list1.clear()
        list1.addAll(picture)
        this.list.value = list1
    }
}