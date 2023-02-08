package com.example.majika.ui.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.R
import com.example.majika.model.CartItem
import com.example.majika.ui.menu.MenuAdapter

class CartAdapter(val data: List<CartItem>, val context: Context): RecyclerView.Adapter<CartAdapter.Holder>() {
    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(cartItem: CartItem?) {
            cartItem?.let{
                itemView.findViewById<TextView>(R.id.name).text = it.name
                itemView.findViewById<TextView>(R.id.price).text = it.currency + " " + it.price
                itemView.findViewById<TextView>(R.id.quantity).text = it.quantity.toString()
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: CartAdapter.Holder, position: Int) {
        val menu = data[position]
        holder.bind(menu)
    }
}