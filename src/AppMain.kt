import javax.swing.SwingUtilities
import javax.swing.UIManager


class AppMain {
    init {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel")

        SwingUtilities.invokeLater {
            val model = Model()
            val app = AppFrame(model)
            app.isVisible = true
            app.pack()
        }
    }
}