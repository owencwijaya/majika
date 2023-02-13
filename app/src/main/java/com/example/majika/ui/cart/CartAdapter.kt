package com.example.majika.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.R
import com.example.majika.db.entity.CartItemEntity
import com.example.majika.utils.observeOnce

class CartAdapter(val cartViewModel: CartViewModel) : ListAdapter<CartItemEntity, CartAdapter.CartViewHolder>(CartComparator()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int){
        val current = getItem(position)
        holder.bind(current)

        holder.addButton.setOnClickListener() {
            val newQuantity = current.quantity + 1
            cartViewModel.updateQuantity(current, newQuantity)
        }

        holder.reduceButton.setOnClickListener() {
            val newQuantity = current.quantity - 1
            cartViewModel.updateQuantity(current, newQuantity)
            if (newQuantity == 0) {
                cartViewModel.delete(current)
                holder.quantity.text = "0"
                holder.quantity.visibility = View.INVISIBLE
                holder.reduceButton.visibility = View.INVISIBLE
            }
        }
    }
    class CartViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var addButton: ImageButton = itemView.findViewById(R.id.add_button)
        var reduceButton: ImageButton = itemView.findViewById(R.id.reduce_button)
        var quantity: TextView = itemView.findViewById(R.id.quantity_buttons)
        fun bind(cartItemEntity: CartItemEntity?) {
            cartItemEntity?.let{
                itemView.findViewById<TextView>(R.id.name_cart).text = it.name
                itemView.findViewById<TextView>(R.id.price_cart).text = it.currency + " " + it.price
                itemView.findViewById<TextView>(R.id.quantity).text = it.quantity.toString()
                itemView.findViewById<TextView>(R.id.quantity_buttons).text =
                    cartItemEntity.quantity.toString()
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