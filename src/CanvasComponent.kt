import utils.WorldLimits
import utils.plus
import utils.times
import java.awt.*
import java.awt.event.*
import java.awt.geom.AffineTransform
import java.awt.geom.Line2D
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D
import javax.swing.JComponent
import javax.swing.SwingUtilities


class CanvasComponent(
    private val mAppFrame: AppFrame,
    private val mModel: Model
) : JComponent(),
    MouseMotionListener,
    MouseWheelListener,
    MouseListener {

    companion object {
        val RECTANGLE_NULL = Rectangle2D.Float(0f, 0f, 10f, 10f)
        const val FIT_SCALE_FACTOR = 1.10
        const val ZOOM_IN_FACTOR = 0.95
        const val ZOOM_OUT_FACTOR = 1.05
        const val TOLERANCE_MOUSE_MOVEMENT = 0.01
    }

    private val mCurrentTransform = AffineTransform()
    private val mLimits = WorldLimits(-1.0, 11.0, 11.0, -1.0)
    private var isComponentReady = false
    private var isMidMouseButtonPressed = false
    private var mPointPressedInSpace = Point2D.Double()
    private var mCurrentMousePosition = Point()
    var isGridEnabled = false
    var isSnapEnabled = false
    var mGridX = 1.00
    var mGridY = 1.00

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

    override fun paintComponent(g: Graphics?) {
        if (!isComponentReady) {
            isComponentReady = true
            initWindowListener()
            scaleWorldWindow()
        }

        val g2 = g?.create() as Graphics2D
        g2.clearRect(0, 0, width, height)

        val rh = RenderingHints(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        )
        g2.setRenderingHints(rh)

        val backupTransform = g2.transform
        updateCurrentTransform()
        g2.transform(mCurrentTransform)

        g2.paint = Color.BLACK
        g2.stroke = BasicStroke(1f / mCurrentTransform.scaleX.toFloat())
        mModel.mShapesList.forEach {
            g2.draw(it)
        }

        if (isGridEnabled) {
            displayGrid(g2)
        }

//        drawCursor(g2)

        g2.transform(backupTransform)
        g2.dispose()
    }

    override fun mouseMoved(e: MouseEvent?) {
        if (e != null) {
            val pSrc = e.point
            val pDst = Point2D.Double()
            mCurrentTransform.inverseTransform(pSrc, pDst)
            mAppFrame.updateCoordinates(pDst.x, pDst.y)

//            mCurrentMousePosition = e.point
//            repaint()
        }
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
    }

    override fun mouseClicked(e: MouseEvent?) {
        if (SwingUtilities.isMiddleMouseButton(e) && e?.clickCount == 2) {
            fit()
        }
    }

    override fun mouseExited(e: MouseEvent?) {
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

        val vprBox = sizeY / sizeX
        if (vpr <= vprBox) {
            mLimits.left = mLimits.centerX() - (sizeY / vpr) / 2.0
            mLimits.right = mLimits.centerX() + (sizeY / vpr) / 2.0
            mLimits.bot = mLimits.centerY() - sizeY / 2.0
            mLimits.top = mLimits.centerY() + sizeY / 2.0
        } else {
            mLimits.bot = mLimits.centerY() - (sizeX * vpr) / 2.0
            mLimits.top = mLimits.centerY() + (sizeX * vpr) / 2.0
            mLimits.right = mLimits.centerX() + (sizeX) / 2.0
            mLimits.left = mLimits.centerX() - (sizeX) / 2.0
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

    private fun displayGrid(g2: Graphics2D) {
        val x0 = 0.0
        val y0 = 0.0
        var x: Double
        var y: Double

        val strokeBackup = g2.stroke
        g2.stroke = BasicStroke(1f / mCurrentTransform.scaleX.toFloat())

        x = x0 - (((x0 - mLimits.left) / mGridX).toInt() * mGridX) - mGridX
        while (x <= mLimits.right) {
            y = y0 - (((y0 - mLimits.bot) / mGridY).toInt() * mGridY) - mGridY
            while (y <= mLimits.top) {
                val pt = Line2D.Double(x, y, x, y)
                g2.draw(pt)
                y += mGridY
            }
            x += mGridX
        }

        g2.stroke = BasicStroke(1f / mCurrentTransform.scaleX.toFloat())

        val lineX = Line2D.Double(x0 - 0.25, y0, x0 + 0.25, y0)
        val lineY = Line2D.Double(x0, y0 - 0.25, x0, y0 + 0.25)
        g2.draw(lineX)
        g2.draw(lineY)

        g2.stroke = strokeBackup
    }

    private fun drawCursor(g2: Graphics2D) {
        val halfSide = mLimits.maxDimension() * TOLERANCE_MOUSE_MOVEMENT / 2.0
        val x0 = mCurrentMousePosition.x.toDouble()
        val y0 = mCurrentMousePosition.y.toDouble()
        val rectCursor = Rectangle2D.Double(x0 - halfSide, y0 - halfSide, halfSide * 2.0, halfSide * 2.0)
        val paintBkp = g2.paint
        g2.paint = Color.BLUE
        g2.fill(rectCursor)
        g2.paint = paintBkp
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

        mLimits.left += panX
        mLimits.right += panX
        mLimits.bot += panY
        mLimits.top += panY
        repaint()
    }

}