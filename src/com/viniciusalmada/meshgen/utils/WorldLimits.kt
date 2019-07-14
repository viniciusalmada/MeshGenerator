package com.viniciusalmada.meshgen.utils

import java.awt.geom.Point2D

data class WorldLimits(var left: Double, var right: Double, var top: Double, var bot: Double) {

    private fun centerX(): Double = width() / 2.0 + left

    private fun centerY(): Double = height() / 2.0 + bot

    fun height(): Double = top - bot

    fun maxDimension(): Double = if (width() > height()) width() else height()

    fun width(): Double = right - left

    fun pointCenter(): Point2D.Double = Point2D.Double(centerX(), centerY())
}