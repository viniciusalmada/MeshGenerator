package com.viniciusalmada.meshgen.curves

import java.awt.Shape
import java.awt.geom.Point2D

abstract class Curve {

    protected var mPtInit: Point2D.Double = Point2D.Double()
    protected var mPtEnd: Point2D.Double = Point2D.Double()
    var isSelected: Boolean = false

    protected var mPointsCount: Int = 0

    abstract val mTotalPoints: Int

    abstract fun addPoint(point: Point2D.Double)

    abstract fun shapeToDraw(): Shape

    abstract fun shapeToDraw(tempPt: Point2D.Double): Shape

    abstract fun pointAtParam(t: Double): Point2D.Double

    abstract fun intersectWithTolerance(point: Point2D.Double, tolerance: Double): Boolean

    fun isCurveComplete(): Boolean {
        return mPointsCount == mTotalPoints
    }
}