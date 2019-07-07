package com.viniciusalmada.meshgen.curves

import com.viniciusalmada.meshgen.utils.plus
import com.viniciusalmada.meshgen.utils.times
import java.awt.Shape
import java.awt.geom.Line2D
import java.awt.geom.Point2D
import java.awt.geom.QuadCurve2D
import java.awt.geom.Rectangle2D
import kotlin.math.pow

class QuadCurve() : Curve() {

    override var mTotalPoints: Int = 3

    constructor(ptInit: Point2D.Double, ptCtrl: Point2D.Double, ptEnd: Point2D.Double) : this() {
        this.mPtInit = ptInit
        this.mPtCtrl = ptCtrl
        this.mPtEnd = ptEnd
    }

    private var mPtCtrl = Point2D.Double()

    override fun addPoint(point: Point2D.Double) {
        when (mPointsCount) {
            0 -> mPtInit = point
            1 -> mPtEnd = point
            2 -> mPtCtrl = point
            else -> throw RuntimeException("Only three points are needed!")
        }
        mPointsCount++
    }

    override fun shapeToDraw(): Shape {
        return QuadCurve2D.Double(mPtInit.x, mPtInit.y, mPtCtrl.x, mPtCtrl.y, mPtEnd.x, mPtEnd.y)
    }

    override fun shapeToDraw(tempPt: Point2D.Double): Shape {
        return when (mPointsCount) {
            1 -> Line2D.Double(mPtInit, tempPt)
            2 -> QuadCurve2D.Double(mPtInit.x, mPtInit.y, tempPt.x, tempPt.y, mPtEnd.x, mPtEnd.y)
            else -> throw RuntimeException("Only one or two point has to exist!")
        }
    }

    override fun pointAtParam(t: Double): Point2D.Double {
        return when {
            t < 0.0 -> mPtInit
            t > 1.0 -> mPtEnd
            else -> mPtInit * (t - 1).pow(2) + mPtCtrl * -t * (2 * t - 2) + mPtEnd * t.pow(2)
        }
    }

    override fun intersectWithTolerance(point: Point2D.Double, tolerance: Double): Boolean {
        val qCurve = QuadCurve2D.Double(mPtInit.x, mPtInit.y, mPtCtrl.x, mPtCtrl.y, mPtEnd.x, mPtEnd.y)
        val rectTol = Rectangle2D.Double(point.x - tolerance / 2, point.y - tolerance / 2, tolerance, tolerance)
        return qCurve.intersects(rectTol)
    }
}