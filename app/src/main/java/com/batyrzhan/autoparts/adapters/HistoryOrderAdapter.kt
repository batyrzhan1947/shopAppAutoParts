package com.batyrzhan.autoparts.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.batyrzhan.autoparts.R
import com.batyrzhan.autoparts.database.model.Order
import com.batyrzhan.autoparts.database.model.StatusOrder
import com.batyrzhan.autoparts.databinding.HistoryItemBinding


class HistoryOrderAdapter(
    private val mContext: Context,
    private val clickListener: (item: Order) -> Unit
) : RecyclerView.Adapter<HistoryOrderAdapter.HistoryViewHolder>() {
    private var ordersList = mutableListOf<Order>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HistoryItemBinding.inflate(inflater, parent, false)
        return HistoryViewHolder(binding)
    }

    fun setList(list: List<Order>) {
        ordersList.clear()
        ordersList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: HistoryOrderAdapter.HistoryViewHolder, position: Int) {
        holder.bind(ordersList[position], clickListener)
    }

    override fun getItemCount(): Int = ordersList.size

    inner class HistoryViewHolder(private val binding: HistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Order, clickListener: (order: Order) -> Unit) = with(itemView) {

            binding.txtPurchaseCode.text = item.codeOrder
            binding.txtOrderTotal.text = item.orderTotal
            binding.txtOrderDate.text = item.dateTime
            when (item.statusOrder) {
                StatusOrder.PROCESSED.status -> {
                    binding.statusOrderTxt.setTextColor(
                        resources.getColor(
                            R.color.green,
                            resources.newTheme()
                        )
                    )
                    binding.statusOrderTxt.text = item.statusOrder

                }
                StatusOrder.CANCELED.status -> {
                    binding.statusOrderTxt.setTextColor(
                        resources.getColor(
                            R.color.red,
                            resources.newTheme()
                        )
                    )
                    binding.statusOrderTxt.text = item.statusOrder
                }
                else -> binding.statusOrderTxt.text = item.statusOrder
            }

            itemView.setOnClickListener {
                clickListener(item)
            }
        }
    }
}