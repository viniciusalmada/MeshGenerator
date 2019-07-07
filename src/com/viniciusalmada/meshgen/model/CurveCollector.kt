package com.viniciusalmada.meshgen.model

import com.viniciusalmada.meshgen.curves.CubicCurve
import com.viniciusalmada.meshgen.curves.Curve
import com.viniciusalmada.meshgen.curves.Line
import com.viniciusalmada.meshgen.curves.QuadCurve
import com.viniciusalmada.meshgen.utils.CurveType
import java.awt.Shape
import java.awt.geom.Point2D

class CurveCollector(val mCurveType: CurveType) {
    var mCurve: Curve?

    var isCurveAlreadyCollected = false

    init {
        when (mCurveType) {
            CurveType.LINE -> mCurve = Line()
            CurveType.QUAD_CURVE -> mCurve = QuadCurve()
            CurveType.CUBIC_CURVE -> mCurve = CubicCurve()
            CurveType.NONE -> mCurve = null
        }
    }

    fun addPoint(point: Point2D.Double) {
        mCurve?.addPoint(point)
        if (mCurve?.isCurveComplete()!!) {
            isCurveAlreadyCollected = true
        }
    }

    fun tempCurve(point: Point2D.Double): Shape? {
        return mCurve?.shapeToDraw(point)
    }

    fun reset() {
        when (mCurveType) {
            CurveType.LINE -> mCurve = Line()
            CurveType.QUAD_CURVE -> mCurve = QuadCurve()
            CurveType.CUBIC_CURVE -> mCurve = CubicCurve()
            CurveType.NONE -> mCurve = null
        }
        isCurveAlreadyCollected = false
    }
}