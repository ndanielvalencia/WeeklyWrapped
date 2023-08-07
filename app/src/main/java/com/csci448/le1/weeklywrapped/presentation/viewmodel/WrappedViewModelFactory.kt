package com.csci448.le1.weeklywrapped.presentation.viewmodel

import android.content.Context
import android.provider.ContactsContract.Contacts.Photo
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.csci448.le1.weeklywrapped.data.PhotographRepository
import com.csci448.le1.weeklywrapped.data.WrappedRepo

//import com.csci448.le1.weeklywrapped.data.WrappedRepo

class WrappedViewModelFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        private const val LOG_TAG = "448.WrappedViewModelFactory"
    }

    fun getViewModelClass() = WrappedViewModel::class.java

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Log.d(LOG_TAG, "create() called")
        if(modelClass.isAssignableFrom(getViewModelClass())) {
            Log.d(LOG_TAG, "creating ViewModel: ${getViewModelClass()}")
            return modelClass
                .getConstructor(
                    WrappedRepo::class.java,
                    PhotographRepository::class.java,
                )
                .newInstance(
                    WrappedRepo.getInstance(context),
                    PhotographRepository.getInstance(context),
                )
        }
        Log.e(LOG_TAG, "Unknown ViewModel: $modelClass")
        throw IllegalArgumentException("Unknown ViewModel")
    }

}