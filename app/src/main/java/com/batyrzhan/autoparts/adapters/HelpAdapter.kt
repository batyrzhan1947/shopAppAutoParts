package com.batyrzhan.autoparts.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.batyrzhan.autoparts.R
import com.batyrzhan.autoparts.databinding.HelpItemBinding
import com.batyrzhan.autoparts.model.Help


class HelpAdapter(
    context: Context
) : RecyclerView.Adapter<HelpAdapter.HelpViewHolder>() {
    private var helpsList = mutableListOf<Help>()
    var mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HelpViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HelpItemBinding.inflate(inflater, parent, false)
        return HelpViewHolder(binding)
    }

    fun setList(list: List<Help>) {
        helpsList.clear()
        helpsList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: HelpAdapter.HelpViewHolder, position: Int) {
        holder.bind(helpsList[position])
    }

    override fun getItemCount(): Int = helpsList.size

    inner class HelpViewHolder(private val binding: HelpItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Help) = with(itemView) {

            binding.titleHelp.text = item.title

            itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.apply {
                    putInt("helpId", helpsList[bindingAdapterPosition].id)
                    putString("helpTitle", helpsList[bindingAdapterPosition].title)

                }
                this.findNavController().navigate(R.id.action_infoFragment_to_detailHelpFragment, bundle)

            }
        }
    }


}