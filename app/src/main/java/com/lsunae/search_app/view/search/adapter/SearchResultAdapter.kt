package com.lsunae.search_app.view.search.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.lsunae.search_app.data.model.SearchResultData
import com.lsunae.search_app.databinding.ItemImageBinding
import com.lsunae.search_app.util.Utils
import com.lsunae.search_app.util.glideImageSet
import com.lsunae.search_app.view.search.SearchFragment
import java.lang.ref.WeakReference

class SearchResultAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var context: Context
    private var imageList = mutableListOf<SearchResultData>()
    lateinit var searchFragment: WeakReference<SearchFragment>
    private lateinit var onItemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(item: SearchResultData)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    fun addNextData(items: List<SearchResultData>) {
        val positionStart = imageList.count() + 1
        imageList.addAll(items)
        notifyItemRangeChanged(positionStart, imageList.size)
        println("nextAddData_positionStart_ $positionStart")
        println("nextAddData_imageList_ $imageList")
    }

    fun addData(items: List<SearchResultData>) {
        imageList.clear()
        imageList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        val requestManager: RequestManager
        if (searchFragment.get() != null && !searchFragment.get()!!.isDetached) {
            requestManager = Glide.with(searchFragment.get()!!)
            if (holder is ImageHolder) {
                holder.clearRequestManager(requestManager)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        this.context = parent.context
        return ImageHolder(
            ItemImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        settingPosition(holder, position)
    }

    private fun settingPosition(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImageHolder -> {
                holder.bind(imageList[position], position)
            }
        }
    }

    inner class ImageHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SearchResultData, position: Int) {
            binding.apply {
                item.thumbnail?.let { ivImage.glideImageSet(it) }
                dateTime = item.dateTime?.let { dateTime -> Utils.dateFormat(dateTime) }

                cbFavorite.setOnClickListener {
                    onItemClickListener.onItemClick(item)
                }
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