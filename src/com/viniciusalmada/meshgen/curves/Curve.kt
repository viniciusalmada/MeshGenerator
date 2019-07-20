package com.viniciusalmada.meshgen.curves

import com.viniciusalmada.meshgen.utils.dist2Points
import java.awt.Shape
import java.awt.geom.Ellipse2D
import java.awt.geom.Path2D
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D

abstract class Curve {

	abstract val mTotalPoints: Int

	protected val mPoints: ArrayList<Point2D> = ArrayList()
	protected var mPointsCount: Int = 0

	private val mVertexRadius: Double = 2.5

	var isSelected: Boolean = false
	var mPtInit: Point2D = Point2D.Double()
	var mPtEnd: Point2D = Point2D.Double()

	abstract fun addPoint(point: Point2D)

	abstract fun intersectWithTolerance(point: Point2D, tolerance: Double): Boolean

	abstract fun pointAtParam(t: Double): Point2D

	abstract fun shapeToDraw(): Shape

	abstract fun shapeToDraw(tempPt: Point2D): Shape

	open fun closestPoint(pt: Point2D): Pair<Double, Point2D> {
		var closestPoint: Point2D = mPoints[0]
		var dist: Double = dist2Points(pt, mPoints[0])
		for (point in mPoints) {
			val currDist = dist2Points(pt, point)
			if (currDist < dist) {
				dist = currDist
				closestPoint = point
			}
		}

		return Pair(dist, closestPoint)
	}

	open fun boundBox(): Rectangle2D {
		var minX = mPoints[0].x
		var maxX = mPoints[0].x
		var minY = mPoints[0].y
		var maxY = mPoints[0].y
		for (pt in mPoints) {
			minX = if (pt.x < minX) pt.x else minX
			maxX = if (pt.x > maxX) pt.x else maxX
			minY = if (pt.y < minY) pt.y else minY
			maxY = if (pt.y > maxY) pt.y else maxY
		}
		return Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY)
	}

	open fun getDiscreteCurve(scaleFactor: Double): Shape {
		val path2D = Path2D.Double()
		path2D.moveTo(mPoints[0].x, mPoints[0].y)
		for (i in 1..mPoints.lastIndex) {
			path2D.lineTo(mPoints[i].x, mPoints[i].y)
			path2D.lineTo(mPoints[i].x, mPoints[i].y * 1.01)
			path2D.lineTo(mPoints[i].x, mPoints[i].y)
		}
		return path2D
	}

	fun isCurveComplete(): Boolean {
		return mPointsCount == mTotalPoints
	}

	fun getInitVertex(scaleFactor: Double): Ellipse2D {
		val radius = mVertexRadius / scaleFactor
		return Ellipse2D.Double(mPtInit.x - radius, mPtInit.y - radius, radius * 2, radius * 2)
	}

	fun getEndVertex(scaleFactor: Double): Ellipse2D {
		val radius = mVertexRadius / scaleFactor
		return Ellipse2D.Double(mPtEnd.x - radius, mPtEnd.y - radius, radius * 2, radius * 2)
	}
}