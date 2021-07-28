package com.app.velocimetro.fontes

import android.annotation.SuppressLint
import android.widget.TextView
import android.content.Context
import android.util.AttributeSet

@SuppressLint("AppCompatCustomView")
class TypeFontDigital : TextView {
    constructor(context: Context) : super(context) {
        applyCustomFont(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        applyCustomFont(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        applyCustomFont(context)
    }

    private fun applyCustomFont(context: Context) {
        val customFont = FontCache.getTypeface("digital-7.ttf", context)
        typeface = customFont
    }
}