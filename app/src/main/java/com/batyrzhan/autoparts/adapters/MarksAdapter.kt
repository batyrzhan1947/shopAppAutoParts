package com.batyrzhan.autoparts.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.batyrzhan.autoparts.databinding.FilterItemBinding
import com.batyrzhan.autoparts.model.MarkaAuto

class MarksAdapter(private val context: Context
) : RecyclerView.Adapter<MarksAdapter.MarksViewHolder>() {
    private var marksList = mutableListOf<MarkaAuto>()


    fun setList(list: List<MarkaAuto>) {
        marksList.clear()
        marksList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarksViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FilterItemBinding.inflate(inflater, parent, false)
        return MarksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MarksViewHolder, position: Int) {
        holder.bind(marksList[position])
    }

    override fun getItemCount(): Int = marksList.size

    inner class MarksViewHolder(private val binding: FilterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MarkaAuto) = with(itemView) {
            binding.checkboxItem.text = item.markaName

        }
    }

}
