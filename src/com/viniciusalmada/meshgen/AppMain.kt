package com.viniciusalmada.meshgen

import com.viniciusalmada.meshgen.model.Model
import com.viniciusalmada.meshgen.ui.AppFrame
import javax.swing.SwingUtilities
import javax.swing.UIManager


class AppMain {
    init {
        try {
            getLaF()
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel")
        } catch (ignored: Exception) {
            println(ignored.message)
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel")
            } catch (ig: Exception) {
                println(ig.message)
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")
                } catch (last: java.lang.Exception) {
                    println(last.message)
                }
            }
        }

        SwingUtilities.invokeLater {
            val model = Model()
            val app = AppFrame(model)
            app.isVisible = true
            app.pack()
        }
    }

    private fun getLaF() {
        val plafs = UIManager.getInstalledLookAndFeels()
        for (p in plafs) {
            println(p.className)
        }
        println(UIManager.getSystemLookAndFeelClassName())
    }
}