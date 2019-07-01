package utils

import java.awt.geom.Point2D
import kotlin.math.sqrt

operator fun Point2D.plus(point2D: Point2D): Point2D.Double {
    return Point2D.Double(this.x + point2D.x, this.y + point2D.y)
}

operator fun Point2D.times(factor: Double): Point2D.Double {
    return Point2D.Double(this.x * factor, this.y * factor)
}

fun dist2Points(p1: Point2D, p2: Point2D): Double {
    val diffX = p2.x - p1.x
    val diffY = p2.y - p1.y
    return sqrt(diffX * diffX + diffY * diffY)
}