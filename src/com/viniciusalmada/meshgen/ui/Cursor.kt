package com.viniciusalmada.meshgen.ui

import java.awt.Graphics2D
import java.awt.geom.Line2D
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D

class Cursor(sideSize: Double, mousePoint: Point2D) {
    private val x0 = mousePoint.x
    private val y0 = mousePoint.y
    private val rect = Rectangle2D.Double(x0 - sideSize / 2, y0 - sideSize / 2, sideSize, sideSize)
    private val lineXCursor = Line2D.Double(x0 - sideSize, y0, x0 + sideSize, y0)
    private val lineYCursor = Line2D.Double(x0, y0 - sideSize, x0, y0 + sideSize)

    fun drawSelectCursor(g2: Graphics2D) {
        g2.draw(rect)
        g2.draw(lineXCursor)
        g2.draw(lineYCursor)
    }

    fun drawCreateCursor(g2: Graphics2D) {
        g2.draw(lineXCursor)
        g2.draw(lineYCursor)
    }
}