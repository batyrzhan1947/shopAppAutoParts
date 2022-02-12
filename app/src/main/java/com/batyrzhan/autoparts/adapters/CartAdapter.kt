package com.batyrzhan.autoparts.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.batyrzhan.autoparts.database.model.Cart
import com.batyrzhan.autoparts.databinding.ItemCartBinding
import com.batyrzhan.autoparts.utils.Constant
import com.squareup.picasso.Picasso

class CartAdapter(
    context: Context, private val clickListener: (cart: Cart, pos:Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    val mContext = context
    private val cartList = mutableListOf<Cart>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCartBinding.inflate(inflater, parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartList[position], clickListener)
    }

    fun setList(list: List<Cart>) {
        cartList.clear()
        cartList.addAll(list)
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int){
        cartList.removeAt(position)
        notifyItemRemoved(position)
    }



    override fun getItemCount(): Int = cartList.size

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(cart: Cart, clickListener: (item: Cart, pos:Int) -> Unit) = with(itemView) {
            binding.nameProductCart.text = cart.productName
            Picasso.get()
                .load(Constant.BASE_URL + "/upload/product/" + cart.productImg)
                .fit().centerCrop()
                .into(binding.imgProductCart)

            binding.quantityPriceProductCart.text =
                (cart.totalProductsPrice / cart.productQuantity).toInt()
                    .toString() + " ${cart.currency}" + " x " + cart.productQuantity.toString() + " шт."

            binding.totalPriceCart.text =
                cart.totalProductsPrice.toInt().toString() + " ${cart.currency}"

           binding.deleteCartBtn.setOnClickListener {

               val position = bindingAdapterPosition
               if (position != RecyclerView.NO_POSITION) {
                   deleteItem(position)
                   clickListener(cart,position)
               }
            }
        }
    }


}