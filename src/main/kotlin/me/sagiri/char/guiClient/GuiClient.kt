package me.sagiri.char.guiClient

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import javax.swing.*

class GuiClient : JFrame {
    val hostJLabel = JLabel("Host: ")
    val hostJTextField = JTextField(20)
    val portJLabel = JLabel("Port: ")
    val portJTextField = JTextField(6)
    val connectButton = JButton("connect")
    val topJPanel = JPanel().apply {
        add(hostJLabel)
        add(hostJTextField)
        add(portJLabel)
        add(portJTextField)
        add(connectButton)
    }
    val msgJTextArea = JTextArea("Hello", 20, 50)
    val cenJPanel = JPanel().apply {
        add(msgJTextArea)
    }
    val nameJLabel = JLabel("Name: ")
    val nameJTextField = JTextField("sagiri", 6)
    val sendMsgJLabel = JLabel("msg: ")
    val sendMsgJTextField = JTextField(30)
    val sendJButton = JButton("send")
    val southJPanel = JPanel().apply {
        add(nameJLabel)
        add(nameJTextField)
        add(sendMsgJLabel)
        add(sendMsgJTextField)
        add(sendJButton)
    }
    constructor(windowName : String = "char") {
        title = windowName
        isVisible = true
        size = Dimension(600, 600)
        minimumSize = Dimension(600, 500)
        defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
        layout = BorderLayout()

        add(topJPanel, BorderLayout.NORTH)
        add(cenJPanel, BorderLayout.CENTER)
        add(southJPanel, BorderLayout.SOUTH)
        msgJTextArea.text += "\nhi\n"
        pack()
    }
}