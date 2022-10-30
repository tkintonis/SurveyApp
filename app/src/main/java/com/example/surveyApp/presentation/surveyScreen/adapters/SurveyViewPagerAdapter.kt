package com.example.surveyApp.presentation.surveyScreen.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.surveyApp.common.models.SurveyItem
import com.example.surveyApp.databinding.SurveyItemLayoutBinding
import com.example.surveyApp.presentation.surveyScreen.viewholders.SurveyItemViewHolder

class SurveyViewPagerAdapter(private val submitAnswer: (SurveyItem) -> Unit, private val retry: (SurveyItem) -> Unit) : RecyclerView.Adapter<SurveyItemViewHolder>() {

    private val surveyItemList = mutableListOf<SurveyItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(newList: List<SurveyItem>) {
        surveyItemList.clear()
        surveyItemList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyItemViewHolder {
        val binding = SurveyItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SurveyItemViewHolder(binding, submitAnswer, retry)
    }

    override fun onBindViewHolder(holder: SurveyItemViewHolder, position: Int) {
        holder.bindData(surveyItemList[position])
    }

    override fun getItemCount(): Int = surveyItemList.size
}