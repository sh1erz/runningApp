package com.karyna.feature.social.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.karyna.core.domain.run.Run
import com.karyna.feature.core.utils.base.BaseItemCallback
import com.karyna.feature.core.utils.base.BaseViewHolder
import com.karyna.feature.core.utils.utils.extensions.showImage
import com.karyna.feature.social.databinding.ItemSocialRunBinding
import com.karyna.feature.core.utils.utils.DateUtils as CoreDateUtils

class RunsAdapter : ListAdapter<Run, BaseViewHolder<ItemSocialRunBinding, Run>>(BaseItemCallback<Run>()) {

    override fun onBindViewHolder(holder: BaseViewHolder<ItemSocialRunBinding, Run>, position: Int) =
        holder.bind(getItem(position))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ItemSocialRunBinding, Run> =
        RunViewHolder(
            ItemSocialRunBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    class RunViewHolder(binding: ItemSocialRunBinding) :
        BaseViewHolder<ItemSocialRunBinding, Run>(binding) {
        override fun bind(item: Run) {
            with(binding) {
                ivAvatar.showImage(
                    item.userAvatarUrl,
                    com.bumptech.glide.request.RequestOptions.centerCropTransform().circleCrop()
                )
                tvName.text = item.userName
                tvDistance.text = root.context.getString(com.karyna.feature.core.R.string.n_meters, item.distanceMeters)
                tvDuration.text = android.text.format.DateUtils.formatElapsedTime(item.durationS)
                tvCity.text = item.location.city
                tvDate.text = CoreDateUtils.formatIsoDate(item.date)
            }
        }
    }
}