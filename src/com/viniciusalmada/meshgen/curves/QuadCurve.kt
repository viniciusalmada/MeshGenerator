package com.viniciusalmada.meshgen.curves

import com.viniciusalmada.meshgen.utils.*
import java.awt.Shape
import java.awt.geom.Line2D
import java.awt.geom.Point2D
import java.awt.geom.QuadCurve2D
import java.awt.geom.Rectangle2D
import kotlin.math.pow

class QuadCurve : Curve() {

    override var mTotalPoints: Int = 3

    private lateinit var mPtCtrl: Point2D

    override fun addPoint(point: Point2D) {
        when (mPointsCount) {
            0 -> mPtInit = point
            1 -> mPtEnd = point
            2 -> mPtCtrl = point
            else -> throw RuntimeException(ERROR_THREE_POINTS_ONLY)
        }
        mPointsCount++

        if (mPointsCount == mTotalPoints) {
            for (i in 0..DISCRETE_CURVE_POINTS) {
                val t = i.toDouble() / DISCRETE_CURVE_POINTS.toDouble()
                val pt = pointAtParam(t)
                mPoints.add(pt)
            }
        }
    }

    override fun shapeToDraw(): Shape {
        return QuadCurve2D.Double(mPtInit.x, mPtInit.y, mPtCtrl.x, mPtCtrl.y, mPtEnd.x, mPtEnd.y)
    }

    override fun shapeToDraw(tempPt: Point2D): Shape {
        return when (mPointsCount) {
            1 -> Line2D.Double(mPtInit, tempPt)
            2 -> QuadCurve2D.Double(mPtInit.x, mPtInit.y, tempPt.x, tempPt.y, mPtEnd.x, mPtEnd.y)
            else -> throw RuntimeException(ERROR_ONE_OR_TWO_POINT_TO_EXIST)
        }
    }

    override fun pointAtParam(t: Double): Point2D {
        return when {
            t <= 0.0 -> mPtInit
            t >= 1.0 -> mPtEnd
            else -> mPtInit * (t - 1).pow(2) +
                    mPtCtrl * -t * (2 * t - 2) +
                    mPtEnd * t.pow(2)
        }
    }

    override fun intersectWithTolerance(point: Point2D, tolerance: Double): Boolean {
        val rectTol = Rectangle2D.Double(point.x - tolerance / 2, point.y - tolerance / 2, tolerance, tolerance)
        for (i in 0 until mPoints.size - 1) {
            val line = Line2D.Double(mPoints[i], mPoints[i + 1])
            if (line.intersects(rectTol))
                return true
        }
        return false
    }
}