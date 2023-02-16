package com.example.majika.ui.branch

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.R
import com.example.majika.model.Branch

class BranchAdapter(val context: Context) : RecyclerView.Adapter<BranchAdapter.Holder>() {
    private var branchList: List<Branch>? = null;
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mapsButton: ImageButton = itemView.findViewById(R.id.maps_button)
        fun bind(branch: Branch?) {
            branch?.let {
                itemView.findViewById<TextView>(R.id.name).text = it.name
                itemView.findViewById<TextView>(R.id.popular_food).text = it.popularFood
                itemView.findViewById<TextView>(R.id.address).text = it.address
                itemView.findViewById<TextView>(R.id.contact_person).text = it.contactPerson
                itemView.findViewById<TextView>(R.id.phone_number).text = it.phoneNumber
            }
        }
    }

    fun setBranchList(branchList: List<Branch>) {
        this.branchList = branchList.sortedWith(compareBy { it.name })
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_branch, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = branchList?.size ?: 0

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val branch = branchList?.get(position)
        holder.bind(branch)

        holder.mapsButton.setOnClickListener {
            val uri = String.format("geo:%f,%f", branch?.latitude, branch?.longitude)

            val gmmIntentUri = Uri.parse(uri)

            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")

            context.startActivity(mapIntent)
        }
    }



//        }

}
