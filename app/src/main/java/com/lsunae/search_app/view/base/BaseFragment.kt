package com.lsunae.search_app.view.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment


abstract class BaseFragment<B : ViewDataBinding>(private val layoutId: Int) : Fragment() {
    protected lateinit var binding: B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("[${javaClass.name}]", "onViewCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.i("[${javaClass.name}]", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i("[${javaClass.name}]", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("[${javaClass.name}]", "onPause")
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}