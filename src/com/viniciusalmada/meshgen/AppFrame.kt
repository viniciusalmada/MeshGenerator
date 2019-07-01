package com.viniciusalmada.meshgen

import com.viniciusalmada.meshgen.utils.*
import java.awt.*
import java.awt.event.ActionListener
import java.awt.event.ItemListener
import java.util.*
import javax.swing.*


class AppFrame(private val mModel: Model) : JFrame(APP_TITLE) {
    private val mLabelX: JLabel = JLabel(SPACE)
    private val mLabelY: JLabel = JLabel(SPACE)

    init {
        initSize()
        initLocation()
        initWindowComportment()
        initComponents()
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

    private fun initComponents() {
        // Canvas
        val canvas = CanvasComponent(this, mModel)
        canvas.preferredSize = Dimension((width * 0.90).toInt(), (height * 0.90).toInt())
        add(canvas, BorderLayout.CENTER)

        // Coordinates
        val panelTextsCoordinates = JPanel(FlowLayout(FlowLayout.LEADING))
        add(panelTextsCoordinates, BorderLayout.SOUTH)
        panelTextsCoordinates.add(mLabelX)
        panelTextsCoordinates.add(mLabelY)

        // Buttons
        val panelButtons = JPanel(FlowLayout(FlowLayout.LEADING))
        add(panelButtons, BorderLayout.NORTH)
        val fitButton = JButton(FIT_BUTTON_TITLE)
        val zoomInButton = JButton(ZOOM_IN_BUTTON_TITLE)
        val zoomOutButton = JButton(ZOOM_OUT_BUTTON_TITLE)
        val panLeftButton = JButton(PAN_LEFT_BUTTON_TITLE)
        val panRightButton = JButton(PAN_RIGHT_BUTTON_TITLE)
        val panUpButton = JButton(PAN_UP_BUTTON_TITLE)
        val panDownButton = JButton(PAN_DOWN_BUTTON_TITLE)
        val gridButton = JButton(GRID_BUTTON_TITLE)
        val snapCheckBox = JCheckBox(SNAP_CHECK_TEXT)
        snapCheckBox.isEnabled = false

        fitButton.addActionListener { canvas.fit() }
        zoomInButton.addActionListener(onZoomButtonClickListener(canvas))
        zoomOutButton.addActionListener(onZoomButtonClickListener(canvas))
        panLeftButton.addActionListener(onPanButtonClickListener(canvas))
        panRightButton.addActionListener(onPanButtonClickListener(canvas))
        panUpButton.addActionListener(onPanButtonClickListener(canvas))
        panDownButton.addActionListener(onPanButtonClickListener(canvas))
        gridButton.addActionListener(onGridButtonClickListener(canvas, snapCheckBox))
        snapCheckBox.addItemListener(onSnapCheckChangeListener(canvas, snapCheckBox))


        panelButtons.add(fitButton)
        panelButtons.add(zoomInButton)
        panelButtons.add(zoomOutButton)
        panelButtons.add(panLeftButton)
        panelButtons.add(panRightButton)
        panelButtons.add(panUpButton)
        panelButtons.add(panDownButton)
        panelButtons.add(gridButton)
        panelButtons.add(snapCheckBox)
    }

    private fun onSnapCheckChangeListener(canvas: CanvasComponent, box: JCheckBox): ItemListener {
        return ItemListener {
            canvas.isSnapEnabled = box.isSelected
        }
    }

    private fun onGridButtonClickListener(canvas: CanvasComponent, snapCheckbox: JCheckBox): ActionListener {
        return ActionListener {
            val gridDialog = JDialog(this, GRID_DIALOG_TITLE, true)
            val gridCheckBox = JCheckBox(GRID_DIALOG_CHECK_TEXT, canvas.isGridEnabled)
            val labelX = JLabel(GRID_X_LABEL)
            val labelY = JLabel(GRID_Y_LABEL)
            val gridNumberX = JTextField(canvas.mGridX.toString(), COLUMN_TEXT_FIELD_THREE)
            val gridNumberY = JTextField(canvas.mGridY.toString(), COLUMN_TEXT_FIELD_THREE)
            val buttonOk = JButton(CONFIRM_GRID_BUTTON_TITLE)
            val statusLabel = JLabel(SPACE)

            if (canvas.isGridEnabled) {
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
                    snapCheckbox.isEnabled = true
                } else {
                    gridNumberX.isEnabled = false
                    gridNumberY.isEnabled = false
                    snapCheckbox.isEnabled = false
                }
            }

            buttonOk.addActionListener {
                if (gridCheckBox.isSelected) {
                    val textX = gridNumberX.text
                    val textY = gridNumberY.text
                    try {
                        val gridX = textX.toDouble()
                        val gridY = textY.toDouble()
                        canvas.mGridX = gridX
                        canvas.mGridY = gridY
                        canvas.isGridEnabled = true
                        gridDialog.dispose()
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()
                        statusLabel.foreground = Color.RED
                        statusLabel.text = INCORRECT_NUMBER_MESSAGE
                    }
                } else {
                    canvas.isGridEnabled = false
                    gridDialog.dispose()
                }
                canvas.repaint()
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

    private fun onZoomButtonClickListener(canvas: CanvasComponent): ActionListener {
        return ActionListener {
            when (it.actionCommand) {
                ZOOM_IN_BUTTON_TITLE -> canvas.zoom(CanvasComponent.ZOOM_IN_FACTOR)
                ZOOM_OUT_BUTTON_TITLE -> canvas.zoom(CanvasComponent.ZOOM_OUT_FACTOR)
            }
        }
    }

    private fun onPanButtonClickListener(canvas: CanvasComponent): ActionListener {
        return ActionListener {
            when (it.actionCommand) {
                PAN_LEFT_BUTTON_TITLE -> canvas.pan(PAN_NEGATIVE_FACTOR, ZERO_DOUBLE)
                PAN_RIGHT_BUTTON_TITLE -> canvas.pan(PAN_POSITIVE_FACTOR, ZERO_DOUBLE)
                PAN_UP_BUTTON_TITLE -> canvas.pan(ZERO_DOUBLE, PAN_POSITIVE_FACTOR)
                PAN_DOWN_BUTTON_TITLE -> canvas.pan(ZERO_DOUBLE, PAN_NEGATIVE_FACTOR)
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