package com.viniciusalmada.meshgen.ui

import com.viniciusalmada.meshgen.utils.WorldLimits
import java.awt.geom.Line2D

class Grid(mLimits: WorldLimits, mGridX: Double, mGridY: Double) {
    val gridPoints = ArrayList<Line2D>()

    init {
        val x0 = 0.0
        val y0 = 0.0
        var x: Double
        var y: Double

        x = x0 - (((x0 - mLimits.left) / mGridX).toInt() * mGridX) - mGridX
        while (x <= mLimits.right) {
            y = y0 - (((y0 - mLimits.bot) / mGridY).toInt() * mGridY) - mGridY
            while (y <= mLimits.top) {
                val pt = Line2D.Double(x, y, x, y)
                gridPoints.add(pt)
                y += mGridY
            }
            x += mGridX
        }
    }
}