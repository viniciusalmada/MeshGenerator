package com.viniciusalmada.meshgen

import java.awt.Shape
import java.awt.geom.Ellipse2D
import java.awt.geom.QuadCurve2D
import java.awt.geom.Rectangle2D

class Model {
    val mShapesList = ArrayList<Shape>()

    init {
        mShapesList.add(TriangleShape())
        mShapesList.add(Rectangle2D.Double(10.0, 10.0, 30.0, 30.0))
        mShapesList.add(Ellipse2D.Double(0.0, 0.0, 10.0, 10.0))
        val quad = QuadCurve2D.Double(20.0, 10.0, -5.54687, 26.56094, 25.0, 40.0)
        mShapesList.add(quad)
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