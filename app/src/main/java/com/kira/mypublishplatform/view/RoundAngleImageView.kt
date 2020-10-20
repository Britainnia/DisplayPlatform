package com.kira.mypublishplatform.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class RoundAngleImageView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(
    context!!, attrs, defStyleAttr
) {
    var width = 30f
    var height = 30f
    private val standard = 12f
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        width = getWidth().toFloat()
        height = getHeight().toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        if (width > standard && height > standard) {
            val path = Path()
            path.moveTo(standard, 0f)
            path.lineTo(width - standard, 0f)
            path.quadTo(width, 0f, width, standard)
            path.lineTo(width, height - standard)
            path.quadTo(width, height, width - standard, height)
            path.lineTo(standard, height)
            path.quadTo(0f, height, 0f, height - standard)
            path.lineTo(0f, standard)
            path.quadTo(0f, 0f, standard, 0f)
            canvas.clipPath(path)
        }
        super.onDraw(canvas)
    }
}