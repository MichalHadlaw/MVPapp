package com.example.kotlinfirebaseapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinfirebaseapp.R
import com.example.kotlinfirebaseapp.models.DrugModel

class DrugAdapter (private val drugList: ArrayList<DrugModel>) :
    RecyclerView.Adapter<DrugAdapter.ViewHolder>() {

        private lateinit var dListener: onItemClickListener


        interface onItemClickListener{
            fun onItemCick(position: Int)
        }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        dListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val  itemView = LayoutInflater.from(parent.context).inflate(R.layout.drug_list_item,parent,false)
        return ViewHolder(itemView, dListener)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val  currentDrug =  drugList[position]
        holder.tvDrugName.text = currentDrug.DrugName
    }

    override fun getItemCount(): Int {
        return drugList.size
    }

    class ViewHolder (itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView){

        val tvDrugName : TextView = itemView.findViewById(R.id.tvDrugName)
        init {
            itemView.setOnClickListener{
                clickListener.onItemCick(adapterPosition)
            }
        }

    }

}
