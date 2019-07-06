package com.viniciusalmada.meshgen.curves

import com.viniciusalmada.meshgen.utils.plus
import com.viniciusalmada.meshgen.utils.times
import java.awt.Shape
import java.awt.geom.Line2D
import java.awt.geom.Point2D
import java.awt.geom.QuadCurve2D

class QuadCurve : Curve() {
    private var mPtCtrl = Point2D.Double()

    override fun addPoint(point: Point2D.Double) {
        when (mTotalPoints) {
            0 -> mPtInit = point
            1 -> mPtEnd = point
            2 -> mPtCtrl = point
            else -> throw RuntimeException("Only three points are needed!")
        }
        mTotalPoints++
    }

    override fun shapeToDraw(): Shape {
        return  QuadCurve2D.Double(mPtInit.x, mPtInit.y, mPtCtrl.x, mPtCtrl.y, mPtEnd.x, mPtEnd.y)
    }

    override fun shapeToDraw(tempPt: Point2D.Double): Shape {
        when (mTotalPoints) {
            1 -> return Line2D.Double(mPtInit, tempPt)
            2 -> return QuadCurve2D.Double(mPtInit.x, mPtInit.y, tempPt.x, tempPt.y, mPtEnd.x, mPtEnd.y)
            else -> throw RuntimeException("Only one or two point has to exist!")
        }
    }

    override fun pointAtParam(t: Double): Point2D.Double {
        return when {
            t < 0.0 -> mPtInit
            t > 1.0 -> mPtEnd
            else -> mPtInit * (t - 1)^2
        }
    }

    override fun intersectWithTolerance(point: Point2D.Double, tolerance: Double): Boolean {
        TODO()
    }
}