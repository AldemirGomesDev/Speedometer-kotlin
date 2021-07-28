package com.app.velocimetro.fontes

import android.content.Context
import android.graphics.Typeface
import java.util.*

/**
 * Criado por AFTI Soluções Tecnológicas em 30/10/2018.
 *
 * @Author AFTI Soluções Tecnológicas
 * @Email appafti@aftisolucoes.com.br
 * @Web http://aftisolucoes.com.br
 */
object FontCache {
    private val fontCache = HashMap<String, Typeface?>()
    fun getTypeface(fontName: String, context: Context): Typeface? {
        var typeface = fontCache[fontName]
        if (typeface == null) {
            typeface = try {
                Typeface.createFromAsset(context.assets, fontName)
            } catch (e: Exception) {
                return null
            }
            fontCache[fontName] = typeface
        }
        return typeface
    }
}