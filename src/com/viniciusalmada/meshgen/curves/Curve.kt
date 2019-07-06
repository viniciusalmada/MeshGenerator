package com.viniciusalmada.meshgen.curves

import java.awt.Shape
import java.awt.geom.Point2D

abstract class Curve {
    var mPtInit: Point2D.Double = Point2D.Double()
    var mPtEnd: Point2D.Double = Point2D.Double()

    protected var mTotalPoints: Int = 0

    abstract fun addPoint(point: Point2D.Double)

    abstract fun shapeToDraw(): Shape

    abstract fun shapeToDraw(tempPt: Point2D.Double): Shape

    abstract fun pointAtParam(t: Double): Point2D.Double

    abstract fun intersectWithTolerance(point: Point2D.Double, tolerance: Double): Boolean
}