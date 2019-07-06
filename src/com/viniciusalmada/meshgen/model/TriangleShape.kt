package com.viniciusalmada.meshgen.model

import java.awt.Polygon

class TriangleShape : Polygon() {
    init {
        xpoints = intArrayOf(10, 20, 15)
        ypoints = intArrayOf(10, 10, 17.5.toInt())
        npoints = 3
    }
}