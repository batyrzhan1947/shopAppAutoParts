package com.batyrzhan.autoparts.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.batyrzhan.autoparts.utils.Constant
import com.batyrzhan.autoparts.R
import com.batyrzhan.autoparts.databinding.CategoryItemBinding
import com.batyrzhan.autoparts.model.PartCategory
import com.squareup.picasso.Picasso


class CatalogAdapter(
    context: Context
) : RecyclerView.Adapter<CatalogAdapter.CatalogViewHolder>() {
    private var categoryList = mutableListOf<PartCategory>()
    var mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CategoryItemBinding.inflate(inflater, parent, false)
        return CatalogViewHolder(binding)
    }

    fun setList(list: List<PartCategory>) {
        categoryList.clear()
        categoryList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CatalogViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }

    override fun getItemCount(): Int = categoryList.size

    inner class CatalogViewHolder(private val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PartCategory) = with(itemView) {
            binding.categoryName.text = item.name
            Picasso.get()
                .load(Constant.BASE_URL + "/upload/category/" + item.img)
                .fit().centerCrop()
                .into(binding.categoryImg)

            binding.categoryLayout.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("catId", categoryList[bindingAdapterPosition].id.toString())
                bundle.putString("catName", categoryList[bindingAdapterPosition].name)
                this.findNavController().navigate(R.id.action_catalogFragment_to_productsFragment,bundle)
            }
        }
    }




}