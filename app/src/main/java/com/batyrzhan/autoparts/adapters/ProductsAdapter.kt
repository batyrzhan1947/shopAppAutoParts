package com.batyrzhan.autoparts.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.batyrzhan.autoparts.R
import com.batyrzhan.autoparts.databinding.ProductItemBinding
import com.batyrzhan.autoparts.fragments.SearchFragment
import com.batyrzhan.autoparts.model.Product
import com.batyrzhan.autoparts.utils.Constant
import com.squareup.picasso.Picasso

class ProductsAdapter(
    private val mContext: Context
) : RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {
    private var productsList = mutableListOf<Product>()

    fun setList(list: List<Product>) {
        productsList.clear()
        productsList.addAll(list)
        notifyDataSetChanged()
    }

    fun filtered(filterList: MutableList<Product>) {
        productsList = filterList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ProductItemBinding.inflate(inflater, parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productsList[position])
    }

    override fun getItemCount(): Int = productsList.size

    inner class ProductViewHolder(private val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: Product) = with(itemView) {
            binding.productNameTxtView.text = item.p_name
            binding.productArticulTxtView.text = item.p_articul
            binding.productTypeTxtView.text = item.p_type
            binding.productPriceTxtView.text =
                item.p_price.toInt().toString() + " ${item.currency_code}"

            Picasso.get().load(Constant.BASE_URL + "/upload/product/" + item.p_img)
                .centerCrop().fit().into(binding.productImgView)

            binding.contentProductLayout.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("productId", productsList[bindingAdapterPosition].p_id.toString())

                val inputMethodManager =
                    mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)

                if (it.findFragment<Fragment>() is SearchFragment)
                    this.findNavController()
                        .navigate(R.id.action_searchFragment_to_detailProductFragment, bundle)
                else this.findNavController()
                    .navigate(R.id.action_productsFragment_to_detailProductFragment, bundle)
            }
        }
    }
}

