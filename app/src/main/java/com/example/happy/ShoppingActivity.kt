package com.example.happy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happy.adapter.BillItemAdapter
import com.example.happy.adapter.ShoppingAdapter
import com.example.happy.model.BillItem
import com.example.happy.model.BillType
import com.example.happy.model.Shopping
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ShoppingActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var textTitle: TextView
    lateinit var floatingButton: FloatingActionButton
    lateinit var recyclerShoppingItens: RecyclerView

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

        val arrayShoppingItens = arrayListOf<Shopping>(
            Shopping(1, "Leite", "12/01/2021",5.99),
            Shopping(2, "Arroz", "12/01/2021",12.99),
            Shopping(3, "Amaciante", "12/01/2021",7.90),
            Shopping(4, "Panela", "12/01/2021",49.90)
        )

        val adapterShoppingItens = ShoppingAdapter(arrayShoppingItens, this)

        recyclerShoppingItens.adapter = adapterShoppingItens
        recyclerShoppingItens.layoutManager = LinearLayoutManager(this,  LinearLayoutManager.VERTICAL, false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true;
    }
}