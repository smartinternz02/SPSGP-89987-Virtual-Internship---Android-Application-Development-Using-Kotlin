package com.kanaishk.groceryapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GroceryViewModalFactory (private val repository: GroceryRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GroceryViewModal::class.java)){
            return GroceryViewModal(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}