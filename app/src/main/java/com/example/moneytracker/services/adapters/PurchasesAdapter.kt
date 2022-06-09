package com.example.moneytracker.services.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moneytracker.R
import com.example.moneytracker.model.plain_object.purchase.PurchasePlainObject
import com.example.moneytracker.model.plain_object.purchase.PurchaseResponsePlainObject

class PurchasesAdapter(
    private val purchases: MutableList<PurchasePlainObject>
) : RecyclerView.Adapter<PurchasesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val purchaseTitle: TextView = itemView.findViewById(R.id.purchaseTitle)
        val purchaseAmount: TextView = itemView.findViewById(R.id.purchaseAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchasesAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.purchase_view_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PurchasesAdapter.ViewHolder, position: Int) {
        val purchase = purchases[position]
        Log.e("purchase:", purchase.toString())
        holder.purchaseTitle.text = "Имя товара: " + purchase.title
        holder.purchaseAmount.text = "Стоимость: " + purchase.amount.toString()
    }

    override fun getItemCount(): Int = purchases.size
}