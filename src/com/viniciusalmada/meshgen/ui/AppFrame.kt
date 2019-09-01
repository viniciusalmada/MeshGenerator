package com.viniciusalmada.meshgen.ui

import com.viniciusalmada.meshgen.model.CurveCollector
import com.viniciusalmada.meshgen.model.Model
import com.viniciusalmada.meshgen.utils.APP_TITLE
import com.viniciusalmada.meshgen.utils.ARC_CIRCLE_BUTTON_TITLE
import com.viniciusalmada.meshgen.utils.COLUMN_TEXT_FIELD_THREE
import com.viniciusalmada.meshgen.utils.CONFIRM_GRID_BUTTON_TITLE
import com.viniciusalmada.meshgen.utils.CUBIC_CURVE_BUTTON_TITLE
import com.viniciusalmada.meshgen.utils.CanvasMode
import com.viniciusalmada.meshgen.utils.CurveType
import com.viniciusalmada.meshgen.utils.DEFAULT_HEIGHT
import com.viniciusalmada.meshgen.utils.DEFAULT_WIDTH
import com.viniciusalmada.meshgen.utils.DELETE_BUTTON_TITLE
import com.viniciusalmada.meshgen.utils.FIT_BUTTON_TITLE
import com.viniciusalmada.meshgen.utils.GAP_10
import com.viniciusalmada.meshgen.utils.GAP_20
import com.viniciusalmada.meshgen.utils.GAP_5
import com.viniciusalmada.meshgen.utils.GRID_BUTTON_TITLE
import com.viniciusalmada.meshgen.utils.GRID_DIALOG_CHECK_TEXT
import com.viniciusalmada.meshgen.utils.GRID_DIALOG_TITLE
import com.viniciusalmada.meshgen.utils.GRID_X_LABEL
import com.viniciusalmada.meshgen.utils.GRID_Y_LABEL
import com.viniciusalmada.meshgen.utils.INCORRECT_NUMBER_MESSAGE
import com.viniciusalmada.meshgen.utils.LINE_BUTTON_TITLE
import com.viniciusalmada.meshgen.utils.PAN_DOWN_BUTTON_TITLE
import com.viniciusalmada.meshgen.utils.PAN_LEFT_BUTTON_TITLE
import com.viniciusalmada.meshgen.utils.PAN_NEGATIVE_FACTOR
import com.viniciusalmada.meshgen.utils.PAN_POSITIVE_FACTOR
import com.viniciusalmada.meshgen.utils.PAN_RIGHT_BUTTON_TITLE
import com.viniciusalmada.meshgen.utils.PAN_UP_BUTTON_TITLE
import com.viniciusalmada.meshgen.utils.QUAD_CURVE_BUTTON_TITLE
import com.viniciusalmada.meshgen.utils.SELECT_BUTTON_TITLE
import com.viniciusalmada.meshgen.utils.SNAP_CHECK_TEXT
import com.viniciusalmada.meshgen.utils.SPACE
import com.viniciusalmada.meshgen.utils.TWO_DECIMALS_FORMAT
import com.viniciusalmada.meshgen.utils.ZERO
import com.viniciusalmada.meshgen.utils.ZERO_DOUBLE
import com.viniciusalmada.meshgen.utils.ZOOM_IN_BUTTON_TITLE
import com.viniciusalmada.meshgen.utils.ZOOM_OUT_BUTTON_TITLE
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Toolkit
import java.util.*
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.SwingConstants
import javax.swing.WindowConstants


class AppFrame(val mModel: Model) : JFrame(APP_TITLE) {
	private val mCanvas: CanvasComponent = CanvasComponent(this, mModel)
	private val mFitButton: JButton = JButton(FIT_BUTTON_TITLE)
	private val mZoomInButton: JButton = JButton(ZOOM_IN_BUTTON_TITLE)
	private val mZoomOutButton: JButton = JButton(ZOOM_OUT_BUTTON_TITLE)
	private val mPanLeftButton: JButton = JButton(PAN_LEFT_BUTTON_TITLE)
	private val mPanRightButton: JButton = JButton(PAN_RIGHT_BUTTON_TITLE)
	private val mPanUpButton: JButton = JButton(PAN_UP_BUTTON_TITLE)
	private val mPanDownButton: JButton = JButton(PAN_DOWN_BUTTON_TITLE)
	private val mGridButton: JButton = JButton(GRID_BUTTON_TITLE)
	private val mSelectButton: JButton = JButton(SELECT_BUTTON_TITLE)
	private val mLineButton: JButton = JButton(LINE_BUTTON_TITLE)
	//    private val mPolylineButton: JButton = JButton(POLYLINE_BUTTON_TITLE)
	private val mQuadCurveButton: JButton = JButton(QUAD_CURVE_BUTTON_TITLE)
	private val mCubicCurveButton: JButton = JButton(CUBIC_CURVE_BUTTON_TITLE)
	private val mArcCircleButton: JButton = JButton(ARC_CIRCLE_BUTTON_TITLE)
	private val mDeleteButton: JButton = JButton(DELETE_BUTTON_TITLE)
	private val mSnapCheckBox: JCheckBox = JCheckBox(SNAP_CHECK_TEXT)
	private val mLabelX: JLabel = JLabel(SPACE)
	private val mLabelY: JLabel = JLabel(SPACE)
	private val mLabelStatus: JLabel = JLabel(SPACE)

	init {
		initSize()
		initLocation()
		initWindowComportment()
		setupComponents()
		initComponentsOnLayout()
	}

	private fun initSize() {
		val screenSize = Toolkit.getDefaultToolkit().screenSize
		val heightScreen = screenSize.height
		val widthScreen = screenSize.width
		size = Dimension(widthScreen / 2, heightScreen / 2)
		println(size)
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
		val panelCoordinates = JPanel(FlowLayout(FlowLayout.LEADING))
		val panelStatusBar = JPanel(FlowLayout(FlowLayout.LEADING))

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
//        panelCreateBar.add(mPolylineButton)
		panelCreateBar.add(mQuadCurveButton)
		panelCreateBar.add(mCubicCurveButton)
		panelCreateBar.add(mArcCircleButton)
		panelCreateBar.add(mDeleteButton)
		this.add(panelCreateBar)

		this.add(mCanvas)

		panelCoordinates.add(mLabelX)
		panelCoordinates.add(mLabelY)
		this.add(panelCoordinates)

		panelStatusBar.add(mLabelStatus)
		this.add(panelStatusBar)
	}

	private fun setupComponents() {
		mCanvas.preferredSize = Dimension((width * 0.90).toInt(), (height * 0.90).toInt())

		mLabelStatus.horizontalAlignment = SwingConstants.RIGHT

		mSnapCheckBox.isEnabled = false

		mFitButton.addActionListener { mCanvas.fit() }
		mZoomInButton.addActionListener { onZoomButtonClickListener(mZoomInButton) }
		mZoomOutButton.addActionListener { onZoomButtonClickListener(mZoomOutButton) }
		mPanLeftButton.addActionListener { onPanButtonClickListener(mPanLeftButton) }
		mPanRightButton.addActionListener { onPanButtonClickListener(mPanRightButton) }
		mPanUpButton.addActionListener { onPanButtonClickListener(mPanUpButton) }
		mPanDownButton.addActionListener { onPanButtonClickListener(mPanDownButton) }
		mGridButton.addActionListener { onGridButtonClickListener() }
		mSnapCheckBox.addItemListener { onSnapCheckChangeListener() }

		mSelectButton.addActionListener { onSelectButtonClickListener() }
		mLineButton.addActionListener { onLineButtonClickListener() }
		mQuadCurveButton.addActionListener { onQuadCurveButtonClickListener() }
		mCubicCurveButton.addActionListener { onCubicCurveButtonClickListener() }
		mArcCircleButton.addActionListener { onArcCircleButtonClickListener() }
		mDeleteButton.addActionListener { onDeleteButtonClickListener() }
	}

	private fun onSnapCheckChangeListener() {
		mCanvas.isSnapEnabled = mSnapCheckBox.isSelected
	}

	private fun onGridButtonClickListener() {
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

		gridDialog.setLocationRelativeTo(mCanvas)
		gridDialog.pack()
		gridDialog.isVisible = true
	}

	private fun onZoomButtonClickListener(bt: JButton) {
		when (bt.actionCommand) {
			ZOOM_IN_BUTTON_TITLE -> mCanvas.zoom(CanvasComponent.ZOOM_IN_FACTOR)
			ZOOM_OUT_BUTTON_TITLE -> mCanvas.zoom(CanvasComponent.ZOOM_OUT_FACTOR)
		}
	}

	private fun onPanButtonClickListener(bt: JButton) {
		when (bt.actionCommand) {
			PAN_LEFT_BUTTON_TITLE -> mCanvas.pan(PAN_NEGATIVE_FACTOR, ZERO_DOUBLE)
			PAN_RIGHT_BUTTON_TITLE -> mCanvas.pan(PAN_POSITIVE_FACTOR, ZERO_DOUBLE)
			PAN_UP_BUTTON_TITLE -> mCanvas.pan(ZERO_DOUBLE, PAN_POSITIVE_FACTOR)
			PAN_DOWN_BUTTON_TITLE -> mCanvas.pan(ZERO_DOUBLE, PAN_NEGATIVE_FACTOR)
		}
	}

	private fun onSelectButtonClickListener() {
		mCanvas.mCanvasMode = CanvasMode.SELECT_MODE
		mModel.unselectCurves()
		mLabelStatus.text = "Select a curve"
	}

	private fun onLineButtonClickListener() {
		mCanvas.mCanvasMode = CanvasMode.CREATE_MODE
		mCanvas.mCurveCollector = CurveCollector(CurveType.LINE)
		mLabelStatus.text = "Creating Line"
	}

	private fun onQuadCurveButtonClickListener() {
		mCanvas.mCanvasMode = CanvasMode.CREATE_MODE
		mCanvas.mCurveCollector = CurveCollector(CurveType.QUAD_CURVE)
		mLabelStatus.text = "Creating Quadratic Curve"
	}

	private fun onCubicCurveButtonClickListener() {
		mCanvas.mCanvasMode = CanvasMode.CREATE_MODE
		mCanvas.mCurveCollector = CurveCollector(CurveType.CUBIC_CURVE)
		mLabelStatus.text = "Creating Cubic Curve"
	}

	private fun onArcCircleButtonClickListener() {
		mCanvas.mCanvasMode = CanvasMode.CREATE_MODE
		mCanvas.mCurveCollector = CurveCollector(CurveType.ARC_CIRCLE)
		mLabelStatus.text = "Creating Arc Circle"
	}

	private fun onDeleteButtonClickListener() {
		mCanvas.mCanvasMode = CanvasMode.SELECT_MODE
		mCanvas.deleteCurveSelected()
		mLabelStatus.text = "Select a curve to delete"
	}

	fun updateCoordinates(x: Double, y: Double) {
		var value = TWO_DECIMALS_FORMAT.format(Locale.ENGLISH, x)
		mLabelX.text = "X = $value"
		value = TWO_DECIMALS_FORMAT.format(Locale.ENGLISH, y)
		mLabelY.text = "Y = $value"
	}
}