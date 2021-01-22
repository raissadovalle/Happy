package com.example.happy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.happy.repository.ShoppingRepository

class ShoppingViewModel (application: Application) : AndroidViewModel(application) {

    private val shoppingRepository = ShoppingRepository(getApplication())

    fun getShoppingByRepId(repId: String) = shoppingRepository.loadShoppingsByRepId(repId)
}