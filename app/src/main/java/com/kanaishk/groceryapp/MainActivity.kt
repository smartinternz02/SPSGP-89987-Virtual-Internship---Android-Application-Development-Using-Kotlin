package com.kanaishk.groceryapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity(), GroceryRVAdapter.GroceryItemClickInterface {
    lateinit var itemsRV: RecyclerView
    lateinit var addFAB: FloatingActionButton
    lateinit var list: List<GroceryItems>
    lateinit var groceryRVAdapter: GroceryRVAdapter
    lateinit var groceryViewModal: GroceryViewModal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        itemsRV = findViewById(R.id.idRVItems)
        addFAB = findViewById(R.id.idFABAdd)
        list = ArrayList<GroceryItems>()
        groceryRVAdapter = GroceryRVAdapter(list,this)
        itemsRV.layoutManager = LinearLayoutManager(this)
        itemsRV.adapter = groceryRVAdapter
        val groceryRepository = GroceryRepository(GroceryDatabase(this))
        val factory = GroceryViewModalFactory(groceryRepository)
        groceryViewModal = ViewModelProvider(this,factory).get(GroceryViewModal::class.java)
        groceryViewModal.getAllGroceryItems().observe( this, Observer{
            groceryRVAdapter.list = it
            groceryRVAdapter.notifyDataSetChanged()
        })

        addFAB.setOnClickListener {
            openDialog()
        }

    }

    private fun openDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.grocery_add_dialog)
        val cancelBtn = dialog.findViewById<MaterialButton>(R.id.idBtnCancel)
        val addBtn = dialog.findViewById<MaterialButton>(R.id.idBtnAdd)
        val itemNameEdt = dialog.findViewById<TextInputEditText>(R.id.idEdtItemName)
        val itemPriceEdt = dialog.findViewById<TextInputEditText>(R.id.idEdtItemCost)
        val itemQuantityEdt = dialog.findViewById<TextInputEditText>(R.id.idEdtItemQuantity)

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        addBtn.setOnClickListener {
            val itemName: String = itemNameEdt.text.toString()
            val itemPrice: String = itemPriceEdt.text.toString()
            val itemQuantity: String = itemQuantityEdt.text.toString()
            val qty : Int = itemQuantity.toInt()
            val price : Float = itemPrice.toFloat()
            if(itemName.isNotEmpty() && itemPrice.isNotEmpty() && itemQuantity.isNotEmpty() && qty>0 && price>0) {
                val items = GroceryItems(itemName, qty, price)
                groceryViewModal.insert(items)
                Toast.makeText(
                    applicationContext,
                    "Item Inserted...",
                    Toast.LENGTH_SHORT
                ).show()
                groceryRVAdapter.notifyDataSetChanged()
                dialog.dismiss()
            } else if(qty<=0) {
                Toast.makeText(
                    applicationContext,
                    "Enter Valid Quantity...",
                    Toast.LENGTH_SHORT
                ).show()
            } else if(price<=0) {
                Toast.makeText(
                    applicationContext,
                    "Enter Valid Price...",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Invalid Input...",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        dialog.show()
    }

    override fun onItemClick(groceryItems: GroceryItems) {
        groceryViewModal.delete(groceryItems)
        groceryRVAdapter.notifyDataSetChanged()
        Toast.makeText(applicationContext,"Item Deleted...",Toast.LENGTH_SHORT).show()
    }
}