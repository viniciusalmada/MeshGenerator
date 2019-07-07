package com.viniciusalmada.meshgen.model

import com.viniciusalmada.meshgen.curves.Curve
import com.viniciusalmada.meshgen.ui.CanvasComponent
import java.awt.geom.Rectangle2D

class Model {

    val mCurvesList = ArrayList<Curve>()

    init {
//        mCurvesList.add(QuadCurve(Point2D.Double(0.0, 0.0), Point2D.Double(0.0, 10.0), Point2D.Double(4.9103, 12.2325)))
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

        var x = mCurvesList[0].shapeToDraw().bounds2D.x
        var y = mCurvesList[0].shapeToDraw().bounds2D.y
        var w = mCurvesList[0].shapeToDraw().bounds2D.width
        var h = mCurvesList[0].shapeToDraw().bounds2D.height
        for (s in mCurvesList) {
            x = if (s.shapeToDraw().bounds2D.minX < x) s.shapeToDraw().bounds2D.minX else x
            y = if (s.shapeToDraw().bounds2D.minY < y) s.shapeToDraw().bounds2D.minY else y
            w = if (s.shapeToDraw().bounds2D.maxX > w) s.shapeToDraw().bounds2D.maxX else w
            h = if (s.shapeToDraw().bounds2D.maxY > h) s.shapeToDraw().bounds2D.maxY else h
        }
        return Rectangle2D.Double(x, y, w, h)
    }
}