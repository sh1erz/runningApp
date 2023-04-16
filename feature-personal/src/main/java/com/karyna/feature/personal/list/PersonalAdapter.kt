package com.karyna.feature.personal.list

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.request.RequestOptions
import com.karyna.core.domain.User
import com.karyna.core.domain.run.Run
import com.karyna.feature.core.utils.base.BaseItemCallback
import com.karyna.feature.core.utils.base.BaseViewHolder
import com.karyna.feature.core.utils.utils.extensions.showImage
import com.karyna.feature.personal.databinding.ItemListTitleBinding
import com.karyna.feature.personal.databinding.ItemRunBinding
import com.karyna.feature.personal.databinding.ItemUserBinding
import com.karyna.feature.core.R as RCore
import com.karyna.feature.core.utils.utils.DateUtils as CoreDateUtils

class PersonalAdapter(private val onSettingsClick: () -> Unit, private val onRunClick: (Run) -> Unit) :
    ListAdapter<PersonalItem, BaseViewHolder<*, PersonalItem>>(BaseItemCallback<PersonalItem>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*, PersonalItem> =
        when (viewType) {
            PersonalItemType.USER.ordinal -> UserInfoViewHolder(
                ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onSettingsClick
            )
            PersonalItemType.LIST_TITLE.ordinal -> ListTitleViewHolder(
                ItemListTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            PersonalItemType.RUN_ITEM.ordinal -> RunViewHolder(
                ItemRunBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onRunClick
            )
            else -> throw IllegalStateException("Unsupported view type")
        }

    override fun onBindViewHolder(holder: BaseViewHolder<*, PersonalItem>, position: Int) =
        holder.bind(getItem(position))

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type.ordinal
    }

    class UserInfoViewHolder(binding: ItemUserBinding, private val onSettingsClick: () -> Unit) :
        BaseViewHolder<ItemUserBinding, PersonalItem>(binding) {
        override fun bind(item: PersonalItem) {
            val user = item.data as? User ?: return
            with(binding) {
                ivAvatar.showImage(user.avatarUrl, RequestOptions.centerCropTransform().circleCrop())
                tvName.text = user.name
                tvEmail.text = user.email
                ivSettings.setOnClickListener { onSettingsClick() }
            }
        }
    }

    class ListTitleViewHolder(binding: ItemListTitleBinding) :
        BaseViewHolder<ItemListTitleBinding, PersonalItem>(binding) {
        override fun bind(item: PersonalItem) {
            val title = item.data as? String ?: return
            binding.tvListTitle.text = title
        }
    }

    class RunViewHolder(binding: ItemRunBinding, private val onRunClick: (Run) -> Unit) :
        BaseViewHolder<ItemRunBinding, PersonalItem>(binding) {
        override fun bind(item: PersonalItem) {
            val run = item.data as? Run ?: return
            with(binding) {
                tvDistance.text = root.context.getString(RCore.string.n_meters, run.distanceMeters)
                tvDuration.text = DateUtils.formatElapsedTime(run.durationS)
                tvCity.text = run.location.city
                tvDate.text = CoreDateUtils.formatIsoDate(run.date)
                root.setOnClickListener { onRunClick(run) }
            }
        }
    }
}