package com.example.iqapp.entities

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import com.example.iqapp.R
import java.lang.reflect.Field

class Task(id: String, resources: Resources, context: Context?) {

    companion object {
        const val VARIANT_END = "variant"
        const val ANSWER_END = "answer"
        const val QUESTION_END = "question"
        const val DRAWABLE_PREFIX = "d_"

        fun availableTaskIds(): Set<String> {
            val ids = mutableSetOf<String>()

            val drawablesFields = R.drawable::class.java.fields
            for (field in drawablesFields) {
                if (field.name.startsWith(DRAWABLE_PREFIX)) {
                    val id = field.name.substring(DRAWABLE_PREFIX.length, DRAWABLE_PREFIX.length + 2)
                    ids.add(id)
                }
            }
            return ids
        }
    }

    val variants: List<Variant>
    val questions: List<Question>

    var answerIdx: Int = -1
        private set

    data class Variant(val drawable: Drawable, val isAnswer: Boolean)
    data class Question(val drawable: Drawable, val name: String)

    init {
        val drawablesFields = R.drawable::class.java.fields

        val variantsList = mutableListOf<Variant>()
        val questionsList = mutableListOf<Question>()

        for (field in drawablesFields) {
            if (field.name.startsWith("$DRAWABLE_PREFIX$id")) {
                when {
                    field.name.endsWith(VARIANT_END) -> getDrawable(field, resources, context)?.let {
                        variantsList.add(Variant(it, false))
                    }

                    field.name.endsWith(ANSWER_END) -> getDrawable(field, resources, context)?.let {
                        variantsList.add(Variant(it, true))
                        answerIdx = variantsList.size - 1
                    }

                    field.name.endsWith(QUESTION_END) -> getDrawable(field, resources, context)?.let {
                        questionsList.add(Question(it, field.name))
                    }
                }
            }
        }

        questionsList.sortBy { it.name }

        if (variantsList.size < 6) {
            for (field in drawablesFields) {
                if (field.name.startsWith(DRAWABLE_PREFIX)) {
                    getDrawable(field, resources, context)?.let {
                        variantsList.add(Variant(it, false))
                    }
                }

                if (variantsList.size == 6) break
            }
        }

        variants = variantsList
        questions = questionsList
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getDrawable(field: Field, resources: Resources, context: Context?): Drawable? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            resources.getDrawable(field.getInt(null), context?.theme)
        } else {
            null
        }
    }
}