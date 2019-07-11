package com.viniciusalmada.meshgen.curves

import com.viniciusalmada.meshgen.utils.ERROR_ONE_OR_TWO_POINT_TO_EXIST
import com.viniciusalmada.meshgen.utils.ERROR_THREE_POINTS_ONLY
import com.viniciusalmada.meshgen.utils.dist2Points
import com.viniciusalmada.meshgen.utils.slopeStraight
import java.awt.Shape
import java.awt.geom.Arc2D
import java.awt.geom.Line2D
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D
import kotlin.math.cos
import kotlin.math.sin

class ArcCircle() : Curve() {

    constructor(ptCenter: Point2D.Double, ptInit: Point2D.Double, ptEnd: Point2D.Double) : this() {
        this.mPtCenter = ptCenter
        this.mPtInit = ptInit
        this.mPtEnd = ptEnd
    }

    override var mTotalPoints: Int = 3

    private var mPtCenter = Point2D.Double()

    override fun addPoint(point: Point2D.Double) {
        when (mPointsCount) {
            0 -> mPtCenter = point
            1 -> mPtInit = point
            2 -> mPtEnd = arc(point).endPoint as Point2D.Double
            else -> throw RuntimeException(ERROR_THREE_POINTS_ONLY)
        }
        mPointsCount++
    }

    override fun shapeToDraw(): Shape {
        return arc()
    }

    override fun shapeToDraw(tempPt: Point2D.Double): Shape {
        return when (mPointsCount) {
            1 -> Line2D.Double(mPtCenter, tempPt)
            2 -> arc(tempPt)
            else -> throw RuntimeException(ERROR_ONE_OR_TWO_POINT_TO_EXIST)
        }
    }

    override fun pointAtParam(t: Double): Point2D.Double {
        return when {
            t < 0.0 -> mPtInit
            t > 1.0 -> mPtEnd
            else -> {
                val extAngle = arc().angleExtent
                val tAngle = t * extAngle + arc().angleStart
                val tAngleRad = Math.toRadians(tAngle)
                Point2D.Double(cos(tAngleRad), sin(tAngleRad))
            }
        }
    }

    override fun intersectWithTolerance(point: Point2D.Double, tolerance: Double): Boolean {
        val arc = arc()
        val rectTol = Rectangle2D.Double(point.x - tolerance / 2, point.y - tolerance / 2, tolerance, tolerance)
        return arc.intersects(rectTol)
    }

    private fun arc(ptEnd: Point2D.Double = mPtEnd): Arc2D.Double {
        val radius = dist2Points(mPtCenter, mPtInit)
        val startAngleDeg = slopeStraight(mPtCenter, mPtInit)
        val extendAngleDeg = slopeStraight(mPtCenter, ptEnd, startAngleDeg)
        val arc = Arc2D.Double()
        arc.setArcByCenter(mPtCenter.x, mPtCenter.y, radius, -startAngleDeg, -extendAngleDeg, Arc2D.OPEN)
        return arc
    }
}