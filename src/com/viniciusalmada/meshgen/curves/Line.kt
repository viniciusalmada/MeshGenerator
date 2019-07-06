package com.viniciusalmada.meshgen.curves

import com.viniciusalmada.meshgen.utils.plus
import com.viniciusalmada.meshgen.utils.times
import java.awt.Shape
import java.awt.geom.Line2D
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D

class Line : Curve() {

    override fun addPoint(point: Point2D.Double) {
        when (mTotalPoints) {
            0 -> mPtInit = point
            1 -> mPtEnd = point
            else -> throw RuntimeException("Only two points are needed!")
        }
        mTotalPoints++
    }

    override fun shapeToDraw(): Shape {
        return Line2D.Double(mPtInit, mPtEnd)
    }

    override fun shapeToDraw(tempPt: Point2D.Double): Shape {
        when (mTotalPoints) {
            1 -> return Line2D.Double(mPtInit, tempPt)
            else -> throw RuntimeException("Only one point has to exist!")
        }
    }

    override fun pointAtParam(t: Double): Point2D.Double {
        return when {
            t < 0.0 -> mPtInit
            t > 1.0 -> mPtEnd
            else -> mPtInit * t + mPtEnd * (1.0 - t)
        }
    }

    override fun intersectWithTolerance(point: Point2D.Double, tolerance: Double): Boolean {
        val line = Line2D.Double(mPtInit, mPtEnd)
        val rectTol = Rectangle2D.Double(point.x - tolerance / 2, point.y - tolerance / 2, tolerance, tolerance)
        return line.intersects(rectTol)
    }


}