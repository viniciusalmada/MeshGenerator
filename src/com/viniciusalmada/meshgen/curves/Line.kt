package com.viniciusalmada.meshgen.curves

import com.viniciusalmada.meshgen.utils.ERROR_ONE_POINT_TO_EXIST
import com.viniciusalmada.meshgen.utils.ERROR_TWO_POINTS_ONLY
import com.viniciusalmada.meshgen.utils.dist2Points
import com.viniciusalmada.meshgen.utils.dotProd
import com.viniciusalmada.meshgen.utils.minus
import com.viniciusalmada.meshgen.utils.norm
import com.viniciusalmada.meshgen.utils.plus
import com.viniciusalmada.meshgen.utils.times
import java.awt.Shape
import java.awt.geom.Line2D
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

class Line : Curve() {

    override var mTotalPoints: Int = 2

    override fun closestPoint(pt: Point2D): Pair<Double, Point2D> {
        val AB = mPtEnd - mPtInit
        val AC = pt - mPtInit
        val t = (AB.dotProd(AC)) / (AB.norm().pow(2))
        val closestPoint = pointAtParam(t)
        val dist = dist2Points(pt, closestPoint)
        return Pair(dist, closestPoint)
    }

    override fun boundBox(): Rectangle2D {
        val x = min(mPtInit.x, mPtEnd.x)
        val y = min(mPtInit.y, mPtEnd.y)
        val maxX = max(mPtInit.x, mPtEnd.x)
        val maxY = max(mPtInit.y, mPtEnd.y)
        return Rectangle2D.Double(x, y, maxX - x, maxY - y)
    }

    override fun getDiscreteCurve(scaleFactor: Double): Shape {
        return shapeToDraw()
    }

    override fun addPoint(point: Point2D) {
        when (mPointsCount) {
            0 -> mPtInit = point
            1 -> mPtEnd = point
            else -> throw RuntimeException(ERROR_TWO_POINTS_ONLY)
        }
        mPointsCount++
    }

    override fun intersectWithTolerance(point: Point2D, tolerance: Double): Boolean {
        val line = Line2D.Double(mPtInit, mPtEnd)
        val rectTol = Rectangle2D.Double(point.x - tolerance / 2, point.y - tolerance / 2, tolerance, tolerance)
        return line.intersects(rectTol)
    }

    override fun pointAtParam(t: Double): Point2D {
        return when {
            t <= 0.0 -> mPtInit
            t >= 1.0 -> mPtEnd
            else -> mPtInit + (mPtEnd - mPtInit) * t
        }
    }

    override fun shapeToDraw(): Shape {
        return Line2D.Double(mPtInit, mPtEnd)
    }

    override fun shapeToDraw(tempPt: Point2D): Shape {
        when (mPointsCount) {
            1 -> return Line2D.Double(mPtInit, tempPt)
            else -> throw RuntimeException(ERROR_ONE_POINT_TO_EXIST)
        }
    }

}