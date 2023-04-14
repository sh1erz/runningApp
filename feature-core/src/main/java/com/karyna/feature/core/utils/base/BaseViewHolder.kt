package com.karyna.feature.core.utils.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<VB : ViewBinding, ITEM>(protected val binding: VB) :
    RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(item: ITEM)
}