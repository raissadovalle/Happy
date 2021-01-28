package com.example.happy.repository

import android.app.Application
import com.example.happy.database.AppDatabase
import com.example.happy.model.Meeting
import com.example.happy.model.Shopping

class ShoppingRepository(application: Application) {

    private val shoppingDao = AppDatabase.getDatabase(application).shoppingDao()

    fun loadShoppingById(id: String) : Shopping = shoppingDao.loadShoppingById(id)

    fun loadShoppingsByRepId(repId: String) = shoppingDao.loadShoppingsByRepId(repId)

    fun insert(shopping: Shopping) = shoppingDao.insert(shopping)

    fun update(shopping: Shopping) = shoppingDao.update(shopping)

    fun delete(shopping: Shopping) = shoppingDao.delete(shopping)
}