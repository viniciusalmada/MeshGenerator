package com.viniciusalmada.meshgen.curves

import com.viniciusalmada.meshgen.utils.*
import java.awt.Shape
import java.awt.geom.CubicCurve2D
import java.awt.geom.Line2D
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D
import kotlin.math.pow

class CubicCurve : Curve() {

    override var mTotalPoints: Int = 4

    private lateinit var mPtCtrl1: Point2D
    private lateinit var mPtCtrl2: Point2D

    override fun addPoint(point: Point2D) {
        when (mPointsCount) {
            0 -> mPtInit = point
            1 -> mPtEnd = point
            2 -> mPtCtrl2 = point
            3 -> mPtCtrl1 = point
            else -> throw RuntimeException(ERROR_FOUR_POINTS_ONLY)
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
        return CubicCurve2D.Double(mPtInit.x, mPtInit.y, mPtCtrl1.x, mPtCtrl1.y, mPtCtrl2.x, mPtCtrl2.y, mPtEnd.x, mPtEnd.y)
    }

    override fun shapeToDraw(tempPt: Point2D): Shape {
        return when (mPointsCount) {
            1 -> Line2D.Double(mPtInit, tempPt)
            2 -> CubicCurve2D.Double(mPtInit.x, mPtInit.y, tempPt.x, tempPt.y, tempPt.x, tempPt.y, mPtEnd.x, mPtEnd.y)
            3 -> CubicCurve2D.Double(mPtInit.x, mPtInit.y, tempPt.x, tempPt.y, mPtCtrl2.x, mPtCtrl2.y, mPtEnd.x, mPtEnd.y)
            else -> throw RuntimeException(ERROR_ONE_OR_TWO_OR_THREE_POINT_TO_EXIST)
        }
    }

    override fun pointAtParam(t: Double): Point2D {
        return when {
            t <= 0.0 -> mPtInit
            t >= 1.0 -> mPtEnd
            else -> mPtInit * -(t - 1).pow(3) +
                    mPtCtrl1 * 3.0 * t * (t - 1).pow(2) +
                    mPtCtrl2 * -t.pow(2) * (3 * t - 3) +
                    mPtEnd * t.pow(3)
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