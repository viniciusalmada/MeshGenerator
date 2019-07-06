package com.viniciusalmada.meshgen.model

import com.viniciusalmada.meshgen.ui.CanvasComponent
import java.awt.Shape
import java.awt.geom.QuadCurve2D
import java.awt.geom.Rectangle2D

class Model {
    val mShapesList = ArrayList<Shape>()

    init {
//        mShapesList.add(TriangleShape())
//        mShapesList.add(Rectangle2D.Double(10.0, 10.0, 30.0, 30.0))
//        mShapesList.add(Ellipse2D.Double(0.0, 0.0, 10.0, 10.0))
        val quad = QuadCurve2D.Double(0.0, 0.0, 0.0, 10.0, 4.9103,12.2325)
        val tol = Rectangle2D.Double(-1.0, 9.0, 2.0,2.0)
        val tol2 = Rectangle2D.Double(-0.1784, 7.0645, 2.0,2.0)
        mShapesList.add(quad)
        mShapesList.add(tol)
        mShapesList.add(tol2)
    }

    fun getBoundBox(): Rectangle2D {
        if (mShapesList.size == 0)
            return CanvasComponent.RECTANGLE_NULL

        if (mShapesList.size == 1)
            return mShapesList[0].bounds2D

        var x = mShapesList[0].bounds2D.x
        var y = mShapesList[0].bounds2D.y
        var w = mShapesList[0].bounds2D.width
        var h = mShapesList[0].bounds2D.height
        for (s in mShapesList) {
            x = if (s.bounds2D.minX < x) s.bounds2D.minX else x
            y = if (s.bounds2D.minY < y) s.bounds2D.minY else y
            w = if (s.bounds2D.maxX > w) s.bounds2D.maxX else w
            h = if (s.bounds2D.maxY > h) s.bounds2D.maxY else h
        }
        return Rectangle2D.Double(x, y, w, h)
    }
}