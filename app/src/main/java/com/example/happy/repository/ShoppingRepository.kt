package com.example.happy.repository

import android.app.Application
import com.example.happy.database.AppDatabase
import com.example.happy.model.Shopping

class ShoppingRepository(application: Application) {

    private val shoppingDao = AppDatabase.getDatabase(application).shoppingDao()

    fun loadShoppingsByRepId(repId: String) = shoppingDao.loadShoppingsByRepId(repId)
}