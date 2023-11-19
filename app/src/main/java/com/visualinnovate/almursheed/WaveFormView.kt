package com.visualinnovate.almursheed

// WaveformView.kt

import android.content.Context
import android.graphics.*
import android.view.View

class WaveformView @JvmOverloads constructor(
    context: Context,
) : View(context) {
    private var audioData: ShortArray? = null
    private var segmentWidth = 256 // Width of each waveform segment in samples
    private var paint: Paint = Paint() // Paint object for drawing waveform segments

    fun setAudioData(data: ShortArray) {
        this.audioData = data
        invalidate() // Force view redraw
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (audioData == null || audioData!!.isEmpty()) {
            return // No audio data to draw
        }

        val segmentCount = audioData!!.size / segmentWidth // Number of waveform segments

        var xPosition = 0f
        for (i in 0 until segmentCount) {
            val segmentStartIndex = i * segmentWidth
            var segmentAmplitude = 0f // Average amplitude of the segment

            for (j in 0 until segmentWidth) {
                val sampleValue = audioData!![segmentStartIndex + j]
                segmentAmplitude += Math.abs(sampleValue.toFloat())
            }

            segmentAmplitude /= segmentWidth.toFloat() // Normalize amplitude to 0-1 range

            val segmentHeight = segmentAmplitude * height // Height of the waveform segment

            paint.color = Color.BLACK // Set waveform color to black

            canvas.drawRect(
                xPosition,
                height - segmentHeight,
                xPosition + segmentWidth.toFloat(),
                height.toFloat(),
                paint,
            )

            xPosition += segmentWidth.toFloat() // Update x-position for the next segment
        }
    }
}


