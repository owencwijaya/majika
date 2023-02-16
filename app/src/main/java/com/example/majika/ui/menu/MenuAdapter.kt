package com.example.majika.ui.menu

import android.app.Activity
import android.content.Context
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.MajikaApplication
import com.example.majika.R
import com.example.majika.db.entity.CartItemEntity
import com.example.majika.model.Menu
import com.example.majika.ui.cart.CartViewModel
import com.example.majika.ui.cart.CartViewModelFactory
import com.example.majika.utils.observeOnce

class MenuAdapter(val context: Context, val cartViewModel: CartViewModel) : RecyclerView.Adapter<MenuAdapter.Holder>() {
    private var menuData: List<Menu>? = null
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var addButton: ImageButton = itemView.findViewById(R.id.add_button)
        var reduceButton: ImageButton = itemView.findViewById(R.id.reduce_button)
        var quantity: TextView = itemView.findViewById(R.id.quantity)

        fun bind(menu: Menu?) {
            menu?.let {
                itemView.findViewById<TextView>(R.id.name).text = it.name
                itemView.findViewById<TextView>(R.id.price).text = it.currency + " " + it.price
                itemView.findViewById<TextView>(R.id.sold).text = it.sold.toString() + " sold"
                itemView.findViewById<TextView>(R.id.description).text = it.description

                cartViewModel.cartItems.observe(context as AppCompatActivity) { its ->
                    val cartItem = its.find { ite -> ite.name == it.name }
                    if (cartItem != null) {
                        reduceButton.visibility = View.VISIBLE
                        quantity.text = cartItem.quantity.toString()
                        quantity.visibility = View.VISIBLE
                    } else {
                        quantity.text = "0"
                    }
                }
            }
        }
    }

    fun setMenuData(menuData: List<Menu>) {
        this.menuData = menuData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = menuData?.size ?: 0

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val menu = menuData?.get(position)
        holder.bind(menu)

        holder.addButton.setOnClickListener() {
            val cartItem = CartItemEntity(
                name = menu?.name!!,
                price = menu.price!!,
                currency = menu.currency!!,
                quantity = 1
            )

            val entity: LiveData<List<CartItemEntity>> = cartViewModel.getEntity(cartItem)

            entity.observeOnce(context as AppCompatActivity) {
                if (it == null || it.isEmpty()) {
                    cartViewModel.insert(cartItem)
                    holder.quantity.visibility = View.VISIBLE
                    holder.quantity.text = "1"
                    holder.reduceButton.visibility = View.VISIBLE
                } else {
                    val newQuantity = it[0].quantity!! + 1
                    cartViewModel.updateQuantity(cartItem, newQuantity)
                    holder.quantity.text = newQuantity.toString()
                }
            }
        }

        holder.reduceButton.setOnClickListener() {
            val cartItem = CartItemEntity(
                name = menu?.name!!,
                price = menu.price!!,
                currency = menu.currency!!,
                quantity = 1
            )

            val entity: LiveData<List<CartItemEntity>> = cartViewModel.getEntity(cartItem)

            entity.observeOnce(context as AppCompatActivity) {
                if (it[0].quantity == 1) {
                    cartViewModel.delete(it[0])
                    holder.quantity.text = "0"
                    holder.quantity.visibility = View.GONE
                    holder.reduceButton.visibility = View.GONE
                } else {
                    val newQuantity = it[0].quantity!! - 1
                    cartViewModel.updateQuantity(cartItem, newQuantity)
                    holder.quantity.text = newQuantity.toString()
                }
            }
        }
    }
}
