package utils

import java.awt.geom.Point2D

data class WorldLimits(
    var left: Double,
    var right: Double,
    var top: Double,
    var bot: Double
) {
    fun width(): Double = right - left
    fun height(): Double = top - bot
    fun centerX(): Double = width() / 2.0 + left
    fun centerY(): Double = height() / 2.0 + bot
    fun pointCenter(): Point2D.Double = Point2D.Double(centerX(), centerY())
    fun maxDimension(): Double = if (width() > height()) width() else height()
}