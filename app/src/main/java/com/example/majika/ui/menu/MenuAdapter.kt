package com.example.majika.ui.menu

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.R
import com.example.majika.model.Menu

class MenuAdapter(val data: List<Menu>, val context: Context) : RecyclerView.Adapter<MenuAdapter.Holder>() {
    private var menuData: List<Menu> = data
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(menu: Menu?) {
            menu?.let {
                itemView.findViewById<TextView>(R.id.name).text = it.name
                itemView.findViewById<TextView>(R.id.price).text =
                    it.currency + " " + it.price
                itemView.findViewById<TextView>(R.id.sold).text = it.sold.toString() + " sold"
                itemView.findViewById<TextView>(R.id.description).text = it.description
            }
        }
    }

    fun search(query: String) {
        val filteredList = data.filter {
            it.name!!.contains(query, true)
        }

        menuData = emptyList()
        menuData = filteredList
        Log.d("MenuAdapter", menuData.toString())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val menu = data[position]
        holder.bind(menu)
    }
}
