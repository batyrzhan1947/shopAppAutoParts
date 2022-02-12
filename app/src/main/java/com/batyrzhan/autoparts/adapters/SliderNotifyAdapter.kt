package com.batyrzhan.autoparts.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.batyrzhan.autoparts.databinding.NotifySliderItemBinding
import com.batyrzhan.autoparts.model.ShopNotification
import com.batyrzhan.autoparts.utils.Constant
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderNotifyAdapter: SliderViewAdapter<SliderNotifyAdapter.NotifyHolder>() {

    private var notifyList = mutableListOf<ShopNotification>()

    override fun getCount(): Int = notifyList.size

    override fun onCreateViewHolder(parent: ViewGroup?): NotifyHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val binding = NotifySliderItemBinding.inflate(inflater, parent, false)
        return NotifyHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: NotifyHolder?, position: Int) {
        viewHolder?.bind(notifyList[position])
    }
    fun setList(list: List<ShopNotification>) {
        notifyList.clear()
        notifyList.addAll(list)
        notifyDataSetChanged()
    }

    inner class NotifyHolder(private val binding: NotifySliderItemBinding):SliderViewAdapter.ViewHolder(
        binding.root
    ) {
        fun bind(item: ShopNotification) = with(itemView){
            binding.notifyItemTitleTxtView.text = item.notify_title
            Glide.with(itemView)
                .load(Constant.BASE_URL + "/upload/notification/" + item.notify_img)
                .fitCenter()
                .into(binding.notifyItemImgView)
        }
    }
}