package com.viniciusalmada.meshgen.model

import com.viniciusalmada.meshgen.curves.Curve
import com.viniciusalmada.meshgen.ui.CanvasComponent
import com.viniciusalmada.meshgen.utils.dist2Points
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D
import kotlin.math.abs

class Model {

    val mCurvesList: ArrayList<Curve> = ArrayList()

    fun add(curve: Curve) {
        mCurvesList.add(curve)
    }

    fun unselectCurves() {
        for (s in mCurvesList) {
            s.isSelected = false
        }
    }

    fun deleteSelectedCurves() {
        if (mCurvesList.isEmpty())
            return

        val iterator = mCurvesList.iterator()
        for (i in iterator) {
            if (i.isSelected) {
                iterator.remove()
            }
        }
    }

    fun isEmpty(): Boolean {
        return mCurvesList.isEmpty()
    }

    fun snapToCurve(pt: Point2D, tolerance: Double): Point2D {

        if (isEmpty()) return pt

        var dmin = tolerance
        var d: Double
        var ptClosest: Point2D = pt
        for (c in mCurvesList) {
            var xC = c.mPtInit.x
            var yC = c.mPtInit.y

            if (abs(pt.x - xC) < tolerance && abs(pt.x - xC) < tolerance) {
                d = dist2Points(pt, c.mPtInit)
                if (d < dmin) {
                    ptClosest = Point2D.Double(xC, yC)
                    dmin = d
                }
                continue
            }

            xC = c.mPtEnd.x
            yC = c.mPtEnd.y
            if (abs(pt.x - xC) < tolerance && abs(pt.y - yC) < tolerance) {
                d = dist2Points(pt, c.mPtEnd)
                if (d < dmin) {
                    ptClosest = Point2D.Double(xC, yC)
                    dmin = d
                }
                continue
            }

            val distAndClosestPoint = c.closestPoint(pt)
            d = distAndClosestPoint.first
            if (d < dmin) {
                ptClosest = distAndClosestPoint.second
                dmin = d
            }
        }

        return ptClosest

    }

    fun boundBox(): Rectangle2D {
        if (mCurvesList.size == 0)
            return CanvasComponent.RECTANGLE_DEFAULT

        if (mCurvesList.size == 1)
            return mCurvesList[0].boundBox()

        var minX = mCurvesList[0].boundBox().minX
        var minY = mCurvesList[0].boundBox().minY
        var maxX = mCurvesList[0].boundBox().maxX
        var maxY = mCurvesList[0].boundBox().maxY
        for (s in mCurvesList) {
            minX = if (s.boundBox().minX < minX) s.boundBox().minX else minX
            minY = if (s.boundBox().minY < minY) s.boundBox().minY else minY
            maxX = if (s.boundBox().maxX > maxX) s.boundBox().maxX else maxX
            maxY = if (s.boundBox().maxY > maxY) s.boundBox().maxY else maxY
        }

        val w = maxX - minX
        val h = maxY - minY
        return Rectangle2D.Double(minX, minY, w, h)
    }
}


