package com.viniciusalmada.meshgen.utils

import java.awt.Cursor
import java.awt.Point
import java.awt.Toolkit
import java.awt.geom.AffineTransform
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import kotlin.math.atan
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

operator fun Point2D.minus(p2: Point2D): Point2D {
    return Point2D.Double(this.x - p2.x, this.y - p2.y)
}

operator fun Point2D.plus(point2D: Point2D): Point2D {
    return Point2D.Double(this.x + point2D.x, this.y + point2D.y)
}

operator fun Point2D.times(factor: Double): Point2D {
    return Point2D.Double(this.x * factor, this.y * factor)
}

fun Point2D.dotProd(p2: Point2D): Double {
    return this.x * p2.x + this.y * p2.y
}

fun Point2D.norm(): Double {
    return sqrt(this.x.pow(2) + this.y.pow(2))
}

fun dist2Points(p1: Point2D, p2: Point2D): Double {
    val diffX = p2.x - p1.x
    val diffY = p2.y - p1.y
    return sqrt(diffX * diffX + diffY * diffY)
}

fun slopeStraight(p1: Point2D, p2: Point2D, zeroAngle: Double = 0.0): Double {
    val transform = if (zeroAngle == 0.0)
        AffineTransform()
    else
        AffineTransform.getRotateInstance(Math.toRadians(zeroAngle))

    val newP1 = transform.inverseTransform(p1, null)
    val newP2 = transform.inverseTransform(p2, null)

    val tanA = (newP2.y - newP1.y) / (newP2.x - newP1.x)
    val angle = atan(tanA)
    if (newP1.x <= newP2.x && newP1.y <= newP2.y) {            // straight crescente 0 to 90
        return Math.toDegrees(angle)
    } else if (newP1.x > newP2.x && newP1.y <= newP2.y) {    // straight crescent 90 to 180
        return 180.0 + Math.toDegrees(angle)
    } else if (newP1.x > newP2.x && newP1.y > newP2.y) {    // straight descrescent 180 to 270
        return 180.0 + Math.toDegrees(angle)
    } else if (newP1.x <= newP2.x && newP1.y > newP2.y) {    // straight descrescent 270 to 360
        return 360.0 + Math.toDegrees(angle)
    }
    return 0.0
}

fun getBlankCursor(): Cursor {
    val cursorImg = BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB)
    return Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, Point(0, 0), "blank cursor")
}

fun getDefaultCursor(): Cursor {
    return Cursor(Cursor.DEFAULT_CURSOR)
}

fun rectFromTwoPoints(p1: Point2D, p2: Point2D): Rectangle2D{
    val x0: Double = min(p1.x, p2.x)
    val y0: Double = min(p1.y, p2.y)
    val w = max(p1.x, p2.x) - x0
    val h = max(p1.y, p2.y) - y0
    return Rectangle2D.Double(x0,y0,w,h)
}
