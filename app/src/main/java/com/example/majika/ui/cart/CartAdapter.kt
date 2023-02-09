package com.example.majika.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.R
import com.example.majika.db.entity.CartItemEntity

class CartAdapter : ListAdapter<CartItemEntity, CartAdapter.CartViewHolder>(CartComparator()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int){
        val current = getItem(position)
        holder.bind(current)
    }
    class CartViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(cartItemEntity: CartItemEntity?) {
            System.err.println("SUDAH SAMPE BINDING")
            cartItemEntity?.let{
                itemView.findViewById<TextView>(R.id.name_cart).text = it.name
                itemView.findViewById<TextView>(R.id.price_cart).text = it.currency + " " + it.price
                itemView.findViewById<TextView>(R.id.quantity).text = it.quantity.toString()
            }
        }

        companion object {
            fun create(parent: ViewGroup): CartViewHolder {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
                return CartViewHolder(view)
            }
        }
    }

    class CartComparator: DiffUtil.ItemCallback<CartItemEntity>() {
        override fun areItemsTheSame(oldItem: CartItemEntity, newItem: CartItemEntity): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: CartItemEntity, newItem: CartItemEntity): Boolean {
            val nameComparison: Boolean = oldItem.name == newItem.name
            val currencyComparison: Boolean = oldItem.currency == newItem.currency
            val priceComparison: Boolean = oldItem.price == newItem.price
            val quantityComparison: Boolean = oldItem.quantity == newItem.quantity
            return nameComparison && currencyComparison && priceComparison && quantityComparison
        }
    }
}