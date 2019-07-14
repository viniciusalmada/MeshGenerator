package com.viniciusalmada.meshgen.model

import com.viniciusalmada.meshgen.curves.*
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
            CurveType.ARC_CIRCLE -> mCurve = ArcCircle()
            CurveType.NONE -> mCurve = null
        }
    }

    fun addPoint(point: Point2D) {
        mCurve?.addPoint(point)
        if (mCurve?.isCurveComplete()!!) {
            isCurveAlreadyCollected = true
        }
    }

    fun tempCurve(point: Point2D): Shape? {
        return mCurve?.shapeToDraw(point)
    }

    fun reset() {
        when (mCurveType) {
            CurveType.LINE -> mCurve = Line()
            CurveType.QUAD_CURVE -> mCurve = QuadCurve()
            CurveType.CUBIC_CURVE -> mCurve = CubicCurve()
            CurveType.ARC_CIRCLE -> mCurve = ArcCircle()
            CurveType.NONE -> mCurve = null
        }
        isCurveAlreadyCollected = false
    }
}