package com.lsunae.search_app.view.storage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.lsunae.search_app.data.model.SearchResultData
import com.lsunae.search_app.databinding.ItemImageBinding
import com.lsunae.search_app.util.OnSingleClickListener
import com.lsunae.search_app.util.Utils
import com.lsunae.search_app.util.glideImageSet
import com.lsunae.search_app.view.storage.StorageFragment
import java.lang.ref.WeakReference

class StorageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var context: Context
    private var items = mutableListOf<SearchResultData>()
    lateinit var storageFragment: WeakReference<StorageFragment>
    private lateinit var onFavoriteClickListener: OnFavoriteClickListener

    interface OnFavoriteClickListener {
        fun onFavoriteClick(item: SearchResultData, position: Int, isChecked: Boolean)
    }

    fun setOnFavoriteClickListener(listener: OnFavoriteClickListener) {
        onFavoriteClickListener = listener
    }

    fun addItems(itemList: ArrayList<SearchResultData>) {
        items.clear()
        if (!itemList.isNullOrEmpty()) items.addAll(itemList)
        notifyDataSetChanged()
    }

    fun notifyChange() {
        notifyDataSetChanged()
    }

    fun notifyRemovedItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        val requestManager: RequestManager
        if (storageFragment.get() != null && !storageFragment.get()!!.isDetached) {
            requestManager = Glide.with(storageFragment.get()!!)
            if (holder is Holder) {
                holder.clearRequestManager(requestManager)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        this.context = parent.context
        return Holder(
            ItemImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        settingPosition(holder, position)
    }

    private fun settingPosition(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is Holder -> {
                holder.bind(items[position], position)
            }
        }
    }

    inner class Holder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SearchResultData, position: Int) {
            binding.apply {
                item.thumbnail?.let { ivImage.glideImageSet(it) }
                dateTime = item.dateTime?.let { dateTime -> Utils.dateFormat(dateTime) }

                cbFavorite.isChecked = true
                cbFavorite.setOnClickListener(object : OnSingleClickListener() {
                    override fun onSingleClick(v: View) {
                        onFavoriteClickListener.onFavoriteClick(
                            item,
                            position,
                            cbFavorite.isChecked
                        )
                    }
                })
            }
        }

        fun clearRequestManager(requestManager: RequestManager) {
            binding.apply {
                requestManager.clear(ivImage)
                requestManager.clear(tvDateTime)
                requestManager.clear(cbFavorite)
            }
        }
    }
}