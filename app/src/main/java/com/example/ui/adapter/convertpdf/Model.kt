package com.example.ui.adapter.convertpdf

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Model : ViewModel() {
var list= MutableLiveData<ArrayList<Uri>>()
}