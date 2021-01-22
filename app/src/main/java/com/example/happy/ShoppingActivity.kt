package com.example.happy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happy.adapter.ShoppingAdapter
import com.example.happy.repository.ShoppingRepository
import com.example.happy.viewmodel.ShoppingViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ShoppingActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var textTitle: TextView
    lateinit var floatingButton: FloatingActionButton
    lateinit var recyclerShoppingItens: RecyclerView
    val shoppingViewModel by viewModels<ShoppingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        textTitle = findViewById(R.id.toolbar_title)
        textTitle.text = getString(R.string.shopping_toolbar_title)

        floatingButton = findViewById(R.id.fb_shopping)
        floatingButton.setOnClickListener {
            val intent = Intent(this, AddShoppingItemActivity::class.java)
            startActivity(intent)
        }

        recyclerShoppingItens = findViewById(R.id.rv_shopping)

        val adapterShoppingItens = ShoppingAdapter(this)

        shoppingViewModel.getShoppingByRepId("1").observe(this, Observer{
            adapterShoppingItens.list = it
            adapterShoppingItens.notifyDataSetChanged()
        }) //TODO repid

        recyclerShoppingItens.adapter = adapterShoppingItens
        recyclerShoppingItens.layoutManager = LinearLayoutManager(this,  LinearLayoutManager.VERTICAL, false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true;
    }
}