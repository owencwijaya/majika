package com.example.majika.ui.branch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.R
import com.example.majika.model.Branch

class BranchAdapter(val data: List<Branch>) : RecyclerView.Adapter<BranchAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_branch, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(data[position])
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(branch: Branch?){
            branch?.let {
                itemView.findViewById<TextView>(R.id.name).text = it.name
                itemView.findViewById<TextView>(R.id.popular_food).text = it.popularFood
                itemView.findViewById<TextView>(R.id.address).text = it.address
                itemView.findViewById<TextView>(R.id.contact_person).text = it.contactPerson
                itemView.findViewById<TextView>(R.id.phone_number).text = it.phoneNumber
            }
        }
    }
}
