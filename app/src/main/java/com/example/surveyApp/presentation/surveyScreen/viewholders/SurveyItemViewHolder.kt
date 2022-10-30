package com.example.surveyApp.presentation.surveyScreen.viewholders

import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.surveyApp.common.models.SurveyItem
import com.example.surveyApp.databinding.SurveyItemLayoutBinding

class SurveyItemViewHolder(
    private val binding: SurveyItemLayoutBinding, private val submitAnswer: (SurveyItem) -> Unit, private val retry: (SurveyItem) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var answer: String = ""

    fun bindData(item: SurveyItem) {
        with(binding) {
            questionTextView.text = item.question

            with(answerEditText) {
                visibility = item.answerResult.editTextVisibility
                addTextChangedListener {
                    answer = it.toString()
                }
            }

            with(answerTextView) {
                visibility = item.answerResult.answerVisibility
                text = item.answerText
            }

            with(submitButton) {
                isSelected = item.answerResult.submitButtonClickable
                isEnabled = item.answerResult.submitButtonClickable
                text = itemView.context.getString(item.answerResult.submitButtonText)
                setTextColor(itemView.context.getColor(item.answerResult.submitButtonTextColor))
                if (item.answerResult.submitButtonClickable) setOnClickListener {
                    if (answer.isNotEmpty()) {
                        item.answerText = answer
                        answerEditText.setText("")
                        submitAnswer(item)
                    }
                }
            }

            resultField.visibility = item.answerResult.popupVisibility
            resultContainer.setBackgroundColor(ResourcesCompat.getColor(itemView.resources, item.answerResult.popUpBackgroundColour, null))
            resultText.text = itemView.resources.getString(item.answerResult.popUpText)
            retryBtn.visibility = item.answerResult.retryButtonVisibility
            retryBtn.setOnClickListener { retry(item) }

        }
    }
}