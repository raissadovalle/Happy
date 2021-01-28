package com.example.happy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happy.adapter.ShoppingAdapter
import com.example.happy.repository.ShoppingRepository
import com.example.happy.viewmodel.MemberViewModel
import com.example.happy.viewmodel.ShoppingViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ShoppingActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var textTitle: TextView
    lateinit var floatingButton: FloatingActionButton
    lateinit var recyclerShoppingItens: RecyclerView
    val shoppingViewModel by viewModels<ShoppingViewModel>()
    val memberViewModel by viewModels<MemberViewModel>()

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
            intent.putExtra("IS_EDIT_SHOPPING", false)
            startActivity(intent)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true;
    }

    override fun onResume() {
        super.onResume()


        recyclerShoppingItens = findViewById(R.id.rv_shopping)

        val adapterShoppingItens = ShoppingAdapter(this)

        memberViewModel.isLogged().observe(this, Observer {
            it?.let {
                shoppingViewModel.getShoppingByRepId(it.repId!!).observe(this, Observer{ it2 ->
                    adapterShoppingItens.list = it2
                    adapterShoppingItens.notifyDataSetChanged()
                })
            }
        })

        recyclerShoppingItens.adapter = adapterShoppingItens
        recyclerShoppingItens.layoutManager = LinearLayoutManager(this,  LinearLayoutManager.VERTICAL, false)

        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                val billToRemove = adapterShoppingItens.list.removeAt(position)
                shoppingViewModel.delete(billToRemove)
                adapterShoppingItens.notifyItemRemoved(position)
                adapterShoppingItens.notifyDataSetChanged()
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerShoppingItens)
    }
}