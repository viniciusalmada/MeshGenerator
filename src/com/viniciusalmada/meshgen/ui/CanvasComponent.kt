package com.viniciusalmada.meshgen.ui

import com.viniciusalmada.meshgen.model.CurveCollector
import com.viniciusalmada.meshgen.model.Model
import com.viniciusalmada.meshgen.utils.*
import java.awt.*
import java.awt.Color.*
import java.awt.event.*
import java.awt.geom.AffineTransform
import java.awt.geom.Line2D
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D
import javax.swing.JPanel
import javax.swing.SwingUtilities
import kotlin.math.roundToInt


class CanvasComponent(private val mAppFrame: AppFrame, private val mModel: Model) : JPanel(),
        MouseMotionListener,
        MouseWheelListener,
        MouseListener {

    companion object {
        val RECTANGLE_NULL = Rectangle2D.Float(0f, 0f, 10f, 10f)
        const val FIT_SCALE_FACTOR = 1.10
        const val ZOOM_IN_FACTOR = 0.95
        const val ZOOM_OUT_FACTOR = 1.05
        const val TOLERANCE_MOUSE_MOVEMENT = 0.015
    }

    private val mCurrentTransform: AffineTransform = AffineTransform()
    private var isComponentReady: Boolean = false
    private var isCollecting: Boolean = false
    private var isMidMouseButtonPressed: Boolean = false
    private var isCursorOnCanvas: Boolean = false
    private val mLimits: WorldLimits = WorldLimits(-1.0, 11.0, 11.0, -1.0)
    private var mCurrentMousePosition: Point2D = Point2D.Double()

    private lateinit var mPointPressedInSpace: Point2D
    private lateinit var mTempCollectedCurve: Shape

    var isGridEnabled: Boolean = false
    var isSnapEnabled: Boolean = false
    var mCanvasMode: CanvasMode = CanvasMode.SELECT_MODE
    var mCurveCollector: CurveCollector = CurveCollector(CurveType.NONE)
    var mGridX: Double = 1.00
    var mGridY: Double = 1.00

    init {
        initMouseListener()
    }

    private fun initMouseListener() {
        addMouseMotionListener(this)
        addMouseWheelListener(this)
        addMouseListener(this)
    }

    private fun initWindowListener() {
        addComponentListener(
                object : ComponentAdapter() {
                    override fun componentResized(e: ComponentEvent?) {
                        super.componentResized(e)
                        scaleWorldWindow()
                    }
                }
        )
    }

    /*private fun initKeyboardListener() {
        addKeyListener(this)
    }*/

    override fun paintComponent(g: Graphics?) {
        val g2 = g?.create() as Graphics2D
        g2.clearRect(0, 0, width, height)
        g2.color = Color.WHITE
        g2.fillRect(0, 0, width, height)

        if (!isComponentReady) {
            isComponentReady = true
            initWindowListener()
            scaleWorldWindow()
        }

        val backupTransform = g2.transform
        updateCurrentTransform()
        g2.transform(mCurrentTransform)

        val rh = RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2.setRenderingHints(rh)
        g2.paint = BLACK
        g2.stroke = BasicStroke(1f / mCurrentTransform.scaleX.toFloat())

        drawModel(g2)

        drawCursor(g2)

        displayGrid(g2)

        if (isCollecting) {
            g2.paint = RED
            g2.draw(mTempCollectedCurve)
        }

        // Restore and dispose graphics
        g2.transform(backupTransform)
        g2.dispose()
    }

    override fun mouseMoved(e: MouseEvent?) {
        if (e == null)
            return

        setupCursor()

        setupCurrentMousePosition(e.point)

        mAppFrame.updateCoordinates(mCurrentMousePosition.x, mCurrentMousePosition.y)

        if (isCollecting)
            mTempCollectedCurve = mCurveCollector.tempCurve(mCurrentMousePosition)!!

        repaint()
    }

    override fun mouseDragged(e: MouseEvent?) {
        if (SwingUtilities.isMiddleMouseButton(e)) {
            val point = Point2D.Double(e!!.point.x.toDouble(), e.point.y.toDouble())
            val pointSpace = mCurrentTransform.inverseTransform(point, null)
            val diffX = mPointPressedInSpace.x - pointSpace.x
            val diffY = mPointPressedInSpace.y - pointSpace.y
            panAbs(diffX, diffY)
        }
    }

    override fun mouseReleased(e: MouseEvent?) {
        if (SwingUtilities.isMiddleMouseButton(e)) {
            isMidMouseButtonPressed = false
        }
    }

    override fun mouseEntered(e: MouseEvent?) {
        isCursorOnCanvas = true
    }

    override fun mouseClicked(e: MouseEvent?) {
        if (e == null) return

        if (dealDoubleClick(e)) fit()

        var pointOnWorld = convertCanvasPoint2WorldPoint(e.point)

        when (mCanvasMode) {
            CanvasMode.SELECT_MODE -> {
                if (mModel.isEmpty())
                    return

                val tolerance = mLimits.maxDimension() * TOLERANCE_MOUSE_MOVEMENT
                for (s in mModel.mCurvesList) {
                    if (s.intersectWithTolerance(pointOnWorld, tolerance)) {
                        mModel.unselectCurves()
                        s.isSelected = true
                        break
                    } else {
                        mModel.unselectCurves()
                    }
                }
                repaint()
            }

            CanvasMode.CREATE_MODE -> {
                /* if (isSnapEnabled)
                     pointOnWorld = round2Snap(pointOnWorld)*/

                mCurveCollector.addPoint(mCurrentMousePosition)
                isCollecting = true

                if (mCurveCollector.isCurveAlreadyCollected) {
                    isCollecting = false
                    mModel.add(mCurveCollector.mCurve!!)
                    mCurveCollector.reset()
                }
            }
        }
    }

    override fun mouseExited(e: MouseEvent?) {
        isCursorOnCanvas = false
        repaint()
    }

    override fun mousePressed(e: MouseEvent?) {
        if (SwingUtilities.isMiddleMouseButton(e)) {
            isMidMouseButtonPressed = true
            val point = Point2D.Double(e!!.point.x.toDouble(), e.point.y.toDouble())
            mPointPressedInSpace = mCurrentTransform.inverseTransform(point, null) as Point2D.Double
        }
    }

    override fun mouseWheelMoved(e: MouseWheelEvent?) {
        val point = Point2D.Double(e!!.point.x.toDouble(), e.point.y.toDouble())
        val pointSpace = mCurrentTransform.inverseTransform(point, null)
        if (e.preciseWheelRotation < 0.0) {
            zoom(ZOOM_IN_FACTOR, pointSpace)
        } else if (e.preciseWheelRotation > 0.0) {
            zoom(ZOOM_OUT_FACTOR, pointSpace)
        }
    }

    private fun updateCurrentTransform(): AffineTransform {
        val scaleX = 1.0 / (mLimits.width() / width.toDouble())

        val scaleY = 1.0 / (mLimits.height() / height.toDouble())

        val transX = -mLimits.left
        val transY = (mLimits.bot + mLimits.height()) // to reflect y axis

        val transform = AffineTransform()
        transform.setToIdentity()
        transform.scale(scaleX, scaleY)
        transform.translate(transX, transY)
        transform.scale(1.0, -1.0)

        mCurrentTransform.setTransform(transform)

        return transform
    }

    private fun scaleWorldWindow() {
        val vpr = height.toDouble() / width.toDouble()

        val box = mModel.getBoundBox()
        mLimits.left = box.minX
        mLimits.right = box.maxX
        mLimits.top = box.maxY
        mLimits.bot = box.minY

        val sizeX = mLimits.width() * FIT_SCALE_FACTOR
        val sizeY = mLimits.height() * FIT_SCALE_FACTOR
        val centerX = mLimits.width() / 2.0 + mLimits.left
        val centerY = mLimits.height() / 2.0 + mLimits.bot

        val vprBox = sizeY / sizeX
        if (vpr <= vprBox) {
            mLimits.left = centerX - (sizeY / vpr) / 2.0
            mLimits.right = centerX + (sizeY / vpr) / 2.0
            mLimits.bot = centerY - sizeY / 2.0
            mLimits.top = centerY + sizeY / 2.0
        } else {
            mLimits.bot = centerY - (sizeX * vpr) / 2.0
            mLimits.top = centerY + (sizeX * vpr) / 2.0
            mLimits.right = centerX + (sizeX) / 2.0
            mLimits.left = centerX - (sizeX) / 2.0
        }

    }

    private fun scaleToPoint(scaleFactor: Double, point: Point2D.Double) {
        val newWidth = mLimits.width() * scaleFactor
        val newHeight = mLimits.height() * scaleFactor

        val diffX = mLimits.width() - newWidth
        val diffY = mLimits.height() - newHeight

        val pointMin = Point2D.Double(-mLimits.left, -mLimits.bot)
        val pointAbs = pointMin + point
        val newPt = pointAbs * scaleFactor
        val gapLeft = pointAbs.x - newPt.x
        val gapBot = pointAbs.y - newPt.y
        val gapRight = diffX - gapLeft
        val gapTop = diffY - gapBot

        mLimits.left += gapLeft
        mLimits.bot += gapBot
        mLimits.right -= gapRight
        mLimits.top -= gapTop

    }

    private fun panAbs(panX: Double, panY: Double) {
        mLimits.left += panX
        mLimits.right += panX
        mLimits.bot += panY
        mLimits.top += panY

        repaint()
    }

    private fun drawModel(g2: Graphics2D) {
        mModel.mCurvesList.forEach {
            if (!it.isSelected) {
                g2.paint = BLACK
            } else {
                g2.paint = RED
            }
            g2.draw(it.shapeToDraw())
            g2.fill(it.getInitVertex(mCurrentTransform.scaleX))
            g2.fill(it.getEndVertex(mCurrentTransform.scaleX))
        }
        g2.paint = BLACK
    }

    private fun displayGrid(g2: Graphics2D) {
        val strokeBackup = g2.stroke
        val line = 12.0
        val lineX = Line2D.Double(0.0, 0.0, line / mCurrentTransform.scaleX, 0.0)
        val lineY = Line2D.Double(0.0, 0.0, 0.0, line / mCurrentTransform.scaleX)
        g2.draw(lineX)
        g2.draw(lineY)

        if (!isGridEnabled) {
            g2.stroke = strokeBackup
            return
        }

        val grid = Grid(mLimits, mGridX, mGridY)
        for (pt in grid.gridPoints) {
            g2.draw(pt)
        }
        g2.stroke = strokeBackup
    }

    private fun drawCursor(g2: Graphics2D) {
        if (!isCursorOnCanvas)
            return

        val sideSize = mLimits.maxDimension() * TOLERANCE_MOUSE_MOVEMENT
        val cursor = Cursor(sideSize, mCurrentMousePosition)
        val paintBkp = g2.paint
        if (mCanvasMode == CanvasMode.SELECT_MODE) {
            g2.paint = RED
            cursor.drawSelectCursor(g2)
        } else if (mCanvasMode == CanvasMode.CREATE_MODE) {
            g2.paint = BLUE
            cursor.drawCreateCursor(g2)
        }
        g2.paint = paintBkp
    }

    private fun round2Snap(pt: Point2D): Point2D {
        val x0 = (pt.x / mGridX).roundToInt() * mGridX
        val y0 = (pt.y / mGridY).roundToInt() * mGridY
        return Point2D.Double(x0, y0)
    }

    private fun round2CurvesVertex(pt: Point2D): Point2D {
        return mModel.snap2Curve(pt, mLimits.maxDimension() * TOLERANCE_MOUSE_MOVEMENT)
    }

    private fun setupCursor() {
        cursor = if (isCursorOnCanvas) {
            getBlankCursor()
        } else {
            getDefaultCursor()
        }
    }

    private fun setupCurrentMousePosition(pt: Point2D) {
        val pointOnWorld = convertCanvasPoint2WorldPoint(pt)

        if (isSnapEnabled && mCanvasMode == CanvasMode.CREATE_MODE) {
            mCurrentMousePosition = round2Snap(pointOnWorld)
        } else if (mCanvasMode == CanvasMode.CREATE_MODE) {
            mCurrentMousePosition = round2CurvesVertex(pointOnWorld)
        } else if (mCanvasMode == CanvasMode.SELECT_MODE) {
            mCurrentMousePosition = pointOnWorld
        } else {
            mCurrentMousePosition = pointOnWorld
        }
        /*mCurrentMousePosition = when {
            isSnapEnabled && mCanvasMode == CanvasMode.CREATE_MODE -> round2Snap(pointOnWorld)
            mCanvasMode == CanvasMode.CREATE_MODE -> round2CurvesVertex(pointOnWorld)
            mCanvasMode == CanvasMode.SELECT_MODE -> pointOnWorld
            else -> pointOnWorld
        }
        mCurrentMousePosition = if (!isSnapEnabled || mCanvasMode == CanvasMode.SELECT_MODE)
            pointOnWorld
        else
            round2Snap(pointOnWorld)*/
    }

    private fun convertCanvasPoint2WorldPoint(point: Point2D): Point2D {
        val pSrc = point
        val pDst = Point2D.Double()
        mCurrentTransform.inverseTransform(pSrc, pDst)
        return pDst
    }

    private fun dealDoubleClick(e: MouseEvent): Boolean {
        return SwingUtilities.isMiddleMouseButton(e) && e.clickCount == 2
    }

    fun fit() {
        scaleWorldWindow()
        repaint()
    }

    fun zoom(factor: Double, point: Point2D = mLimits.pointCenter()) {
        scaleToPoint(factor, point as Point2D.Double)
        repaint()
    }

    fun pan(panFactorX: Double, panFactorY: Double) {
        val panX = mLimits.width() * panFactorX
        val panY = mLimits.height() * panFactorY

        panAbs(panX, panY)
    }

    fun deleteCurveSelected() {
        mModel.deleteSelectedCurves()
        repaint()
    }

}
