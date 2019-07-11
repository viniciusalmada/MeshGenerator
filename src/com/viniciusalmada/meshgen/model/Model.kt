package com.viniciusalmada.meshgen.model

import com.viniciusalmada.meshgen.curves.Curve
import com.viniciusalmada.meshgen.ui.CanvasComponent
import java.awt.geom.Rectangle2D

class Model {

    val mCurvesList = ArrayList<Curve>()

    init {
//        mCurvesList.add(arc)
//        mCurvesList.add(TriangleShape())
//        mCurvesList.add(Rectangle2D.Double(10.0, 10.0, 30.0, 30.0))
//        mCurvesList.add(Ellipse2D.Double(0.0, 0.0, 10.0, 10.0))
//        val quad = QuadCurve2D.Double(0.0, 0.0, 0.0, 10.0, 4.9103,12.2325)
//        val tol = Rectangle2D.Double(-1.0, 9.0, 2.0,2.0)
//        val tol2 = Rectangle2D.Double(-0.1784, 7.0645, 2.0,2.0)
//        mCurvesList.add(quad)
//        mCurvesList.add(tol)
//        mCurvesList.add(tol2)
    }

    fun add(curve: Curve) {
        mCurvesList.add(curve)
    }

    fun getBoundBox(): Rectangle2D {
        if (mCurvesList.size == 0)
            return CanvasComponent.RECTANGLE_NULL

        if (mCurvesList.size == 1)
            return mCurvesList[0].shapeToDraw().bounds2D

        var minX = mCurvesList[0].shapeToDraw().bounds2D.minX
        var minY = mCurvesList[0].shapeToDraw().bounds2D.minY
        var maxX = mCurvesList[0].shapeToDraw().bounds2D.maxX
        var maxY = mCurvesList[0].shapeToDraw().bounds2D.maxY
        for (s in mCurvesList) {
            minX = if (s.shapeToDraw().bounds2D.minX < minX) s.shapeToDraw().bounds2D.minX else minX
            minY = if (s.shapeToDraw().bounds2D.minY < minY) s.shapeToDraw().bounds2D.minY else minY
            maxX = if (s.shapeToDraw().bounds2D.maxX > maxX) s.shapeToDraw().bounds2D.maxX else maxX
            maxY = if (s.shapeToDraw().bounds2D.maxY > maxY) s.shapeToDraw().bounds2D.maxY else maxY
        }

        val w = maxX - minX
        val h = maxY - minY
        return Rectangle2D.Double(minX, minY, w, h)
    }
}


