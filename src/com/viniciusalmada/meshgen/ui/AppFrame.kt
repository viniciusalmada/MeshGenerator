package com.viniciusalmada.meshgen.ui

import com.viniciusalmada.meshgen.model.Model
import com.viniciusalmada.meshgen.utils.*
import java.awt.*
import java.awt.event.ActionListener
import java.awt.event.ItemListener
import java.util.*
import javax.swing.*


class AppFrame(private val mModel: Model) : JFrame(APP_TITLE) {
    private val mLabelX: JLabel = JLabel(SPACE)
    private val mLabelY: JLabel = JLabel(SPACE)
    private val mFitButton = JButton(FIT_BUTTON_TITLE)
    private val mZoomInButton = JButton(ZOOM_IN_BUTTON_TITLE)
    private val mZoomOutButton = JButton(ZOOM_OUT_BUTTON_TITLE)
    private val mPanLeftButton = JButton(PAN_LEFT_BUTTON_TITLE)
    private val mPanRightButton = JButton(PAN_RIGHT_BUTTON_TITLE)
    private val mPanUpButton = JButton(PAN_UP_BUTTON_TITLE)
    private val mPanDownButton = JButton(PAN_DOWN_BUTTON_TITLE)
    private val mGridButton = JButton(GRID_BUTTON_TITLE)
    private val mSnapCheckBox = JCheckBox(SNAP_CHECK_TEXT)
    private val mSelectButton = JButton(SELECT_BUTTON_TITLE)
    private val mLineButton = JButton(LINE_BUTTON_TITLE)
    private val mPolylineButton = JButton(POLYLINE_BUTTON_TITLE)
    private val mQuadCurveButton = JButton(QUAD_CURVE_BUTTON_TITLE)
    private val mCubicCurveButton = JButton(CUBIC_CURVE_BUTTON_TITLE)
    private val mArcCircleButton = JButton(ARC_CIRCLE_BUTTON_TITLE)
    private val mDeleteButton = JButton(DELETE_BUTTON_TITLE)
    private val mCanvas = CanvasComponent(this, mModel)

    init {
        initSize()
        initLocation()
        initWindowComportment()
        initComponentsSetup()
        initComponentsOnLayout()
    }

    private fun initSize() {
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val heightScreen = screenSize.height
        val widthScreen = screenSize.width
        size = Dimension(widthScreen / 2, heightScreen / 2)
        minimumSize = Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT)
    }

    private fun initLocation() {
        setLocationRelativeTo(null)
    }

    private fun initWindowComportment() {
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
    }

    private fun initComponentsOnLayout() {
        this.contentPane.layout = BoxLayout(this.contentPane, BoxLayout.Y_AXIS)
        val panelVisualBar = JPanel(FlowLayout(FlowLayout.LEADING))
        val panelCreateBar = JPanel(FlowLayout(FlowLayout.LEADING))
        val panelCanvas = JPanel(FlowLayout(FlowLayout.LEADING))
        val panelBottomBar = JPanel(FlowLayout(FlowLayout.LEADING))

        panelVisualBar.add(mFitButton)
        panelVisualBar.add(mZoomInButton)
        panelVisualBar.add(mZoomOutButton)
        panelVisualBar.add(mPanLeftButton)
        panelVisualBar.add(mPanRightButton)
        panelVisualBar.add(mPanUpButton)
        panelVisualBar.add(mPanDownButton)
        panelVisualBar.add(mGridButton)
        panelVisualBar.add(mSnapCheckBox)
        this.add(panelVisualBar)

        panelCreateBar.add(mSelectButton)
        panelCreateBar.add(mLineButton)
        panelCreateBar.add(mPolylineButton)
        panelCreateBar.add(mQuadCurveButton)
        panelCreateBar.add(mCubicCurveButton)
        panelCreateBar.add(mArcCircleButton)
        panelCreateBar.add(mDeleteButton)
        this.add(panelCreateBar)

        panelCanvas.add(mCanvas)
        this.add(panelCanvas)

        panelBottomBar.add(mLabelX)
        panelBottomBar.add(mLabelY)
        this.add(panelBottomBar)
    }

    private fun initComponentsSetup(){
        mCanvas.preferredSize = Dimension((width * 0.90).toInt(), (height * 0.90).toInt())

        mSnapCheckBox.isEnabled = false

        mFitButton.addActionListener { mCanvas.fit() }
        mZoomInButton.addActionListener(onZoomButtonClickListener())
        mZoomOutButton.addActionListener(onZoomButtonClickListener())
        mPanLeftButton.addActionListener(onPanButtonClickListener())
        mPanRightButton.addActionListener(onPanButtonClickListener())
        mPanUpButton.addActionListener(onPanButtonClickListener())
        mPanDownButton.addActionListener(onPanButtonClickListener())
        mGridButton.addActionListener(onGridButtonClickListener())
        mSnapCheckBox.addItemListener(onSnapCheckChangeListener())
    }

    private fun onSnapCheckChangeListener(): ItemListener {
        return ItemListener {
            mCanvas.isSnapEnabled = mSnapCheckBox.isSelected
        }
    }

    private fun onGridButtonClickListener(): ActionListener {
        return ActionListener {
            val gridDialog = JDialog(this, GRID_DIALOG_TITLE, true)
            val gridCheckBox = JCheckBox(GRID_DIALOG_CHECK_TEXT, mCanvas.isGridEnabled)
            val labelX = JLabel(GRID_X_LABEL)
            val labelY = JLabel(GRID_Y_LABEL)
            val gridNumberX = JTextField(mCanvas.mGridX.toString(), COLUMN_TEXT_FIELD_THREE)
            val gridNumberY = JTextField(mCanvas.mGridY.toString(), COLUMN_TEXT_FIELD_THREE)
            val buttonOk = JButton(CONFIRM_GRID_BUTTON_TITLE)
            val statusLabel = JLabel(SPACE)

            if (mCanvas.isGridEnabled) {
                gridNumberX.isEnabled = true
                gridNumberY.isEnabled = true
            } else {
                gridNumberX.isEnabled = false
                gridNumberY.isEnabled = false
            }

            gridCheckBox.addItemListener {
                if (gridCheckBox.isSelected) {
                    gridNumberX.isEnabled = true
                    gridNumberY.isEnabled = true
                    mSnapCheckBox.isEnabled = true
                } else {
                    gridNumberX.isEnabled = false
                    gridNumberY.isEnabled = false
                    mSnapCheckBox.isEnabled = false
                }
            }

            buttonOk.addActionListener {
                if (gridCheckBox.isSelected) {
                    val textX = gridNumberX.text
                    val textY = gridNumberY.text
                    try {
                        val gridX = textX.toDouble()
                        val gridY = textY.toDouble()
                        mCanvas.mGridX = gridX
                        mCanvas.mGridY = gridY
                        mCanvas.isGridEnabled = true
                        gridDialog.dispose()
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()
                        statusLabel.foreground = Color.RED
                        statusLabel.text = INCORRECT_NUMBER_MESSAGE
                    }
                } else {
                    mCanvas.isGridEnabled = false
                    gridDialog.dispose()
                }
                mCanvas.repaint()
            }

            gridDialog.layout = BoxLayout(gridDialog.contentPane, BoxLayout.PAGE_AXIS)
            val boxX = Box.createHorizontalBox()
            boxX.add(Box.createRigidArea(Dimension(GAP_20, ZERO)))
            boxX.add(labelX)
            boxX.add(gridNumberX)
            boxX.add(Box.createRigidArea(Dimension(GAP_20, ZERO)))

            val boxY = Box.createHorizontalBox()
            boxY.add(Box.createRigidArea(Dimension(GAP_20, ZERO)))
            boxY.add(labelY)
            boxY.add(gridNumberY)
            boxY.add(Box.createRigidArea(Dimension(GAP_20, ZERO)))

            gridDialog.add(gridCheckBox)
            gridDialog.add(Box.createRigidArea(Dimension(ZERO, GAP_10)))
            gridDialog.add(boxX)
            gridDialog.add(Box.createRigidArea(Dimension(ZERO, GAP_10)))
            gridDialog.add(boxY)
            gridDialog.add(Box.createRigidArea(Dimension(ZERO, GAP_20)))
            gridDialog.add(buttonOk)
            gridDialog.add(Box.createRigidArea(Dimension(ZERO, GAP_5)))
            gridDialog.add(statusLabel)
            gridDialog.add(Box.createRigidArea(Dimension(ZERO, GAP_10)))

            gridDialog.setLocationRelativeTo(this)
            gridDialog.pack()
            gridDialog.isVisible = true
        }

    }

    private fun onZoomButtonClickListener(): ActionListener {
        return ActionListener {
            when (it.actionCommand) {
                ZOOM_IN_BUTTON_TITLE -> mCanvas.zoom(CanvasComponent.ZOOM_IN_FACTOR)
                ZOOM_OUT_BUTTON_TITLE -> mCanvas.zoom(CanvasComponent.ZOOM_OUT_FACTOR)
            }
        }
    }

    private fun onPanButtonClickListener(): ActionListener {
        return ActionListener {
            when (it.actionCommand) {
                PAN_LEFT_BUTTON_TITLE -> mCanvas.pan(PAN_NEGATIVE_FACTOR, ZERO_DOUBLE)
                PAN_RIGHT_BUTTON_TITLE -> mCanvas.pan(PAN_POSITIVE_FACTOR, ZERO_DOUBLE)
                PAN_UP_BUTTON_TITLE -> mCanvas.pan(ZERO_DOUBLE, PAN_POSITIVE_FACTOR)
                PAN_DOWN_BUTTON_TITLE -> mCanvas.pan(ZERO_DOUBLE, PAN_NEGATIVE_FACTOR)
            }
        }
    }

    fun updateCoordinates(x: Double, y: Double) {
        var value = TWO_DECIMALS_FORMAT.format(Locale.ENGLISH, x)
        mLabelX.text = "X = $value"
        value = TWO_DECIMALS_FORMAT.format(Locale.ENGLISH, y)
        mLabelY.text = "Y = $value"
    }
}