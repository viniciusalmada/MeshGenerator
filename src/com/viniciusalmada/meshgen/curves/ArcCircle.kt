package com.viniciusalmada.meshgen.curves

import com.viniciusalmada.meshgen.utils.DISCRETE_CURVE_POINTS
import com.viniciusalmada.meshgen.utils.ERROR_ONE_OR_TWO_POINT_TO_EXIST
import com.viniciusalmada.meshgen.utils.ERROR_THREE_POINTS_ONLY
import com.viniciusalmada.meshgen.utils.dist2Points
import com.viniciusalmada.meshgen.utils.plus
import com.viniciusalmada.meshgen.utils.slopeStraight
import java.awt.Shape
import java.awt.geom.Arc2D
import java.awt.geom.Line2D
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D
import kotlin.math.cos
import kotlin.math.sin

class ArcCircle : Curve() {

    override var mTotalPoints: Int = 3

    private lateinit var mPtCenter: Point2D

    override fun addPoint(point: Point2D) {
        when (mPointsCount) {
            0 -> mPtCenter = point
            1 -> mPtInit = point
            2 -> mPtEnd = arc(point).endPoint as Point2D.Double
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

    override fun intersectWithTolerance(point: Point2D, tolerance: Double): Boolean {
        val rectTol = Rectangle2D.Double(point.x - tolerance / 2, point.y - tolerance / 2, tolerance, tolerance)
        for (i in 0 until mPoints.size - 1) {
            val line = Line2D.Double(mPoints[i], mPoints[i + 1])
            if (line.intersects(rectTol))
                return true
        }
        return false
    }

    override fun pointAtParam(t: Double): Point2D {
        return when {
            t < 0.0 -> mPtInit
            t > 1.0 -> mPtEnd
            else -> {
                val startAngleDeg = slopeStraight(mPtCenter, mPtInit)
                val extendAngleDeg = slopeStraight(mPtCenter, mPtEnd, startAngleDeg)
                val tAngle = startAngleDeg + t * (extendAngleDeg)
                val tAngleRad = Math.toRadians(tAngle)
                val radius = dist2Points(mPtCenter, mPtInit)
                val ptArc = Point2D.Double(cos(tAngleRad) * radius, sin(tAngleRad) * radius)
                mPtCenter + ptArc
            }
        }
    }

    override fun shapeToDraw(): Shape {
        return arc()
    }

    override fun shapeToDraw(tempPt: Point2D): Shape {
        return when (mPointsCount) {
            1 -> Line2D.Double(mPtCenter, tempPt)
            2 -> arc(tempPt)
            else -> throw RuntimeException(ERROR_ONE_OR_TWO_POINT_TO_EXIST)
        }
    }

    private fun arc(ptEnd: Point2D = mPtEnd): Arc2D.Double {
        val radius = dist2Points(mPtCenter, mPtInit)
        val startAngleDeg = slopeStraight(mPtCenter, mPtInit)
        val extendAngleDeg = slopeStraight(mPtCenter, ptEnd, startAngleDeg)
        val arc = Arc2D.Double()
        arc.setArcByCenter(mPtCenter.x, mPtCenter.y, radius, -startAngleDeg, -extendAngleDeg, Arc2D.OPEN)
        return arc
    }
}