package me.sagiri.char.guiClient

import me.sagiri.char.client.Client
import me.sagiri.char.client.ClientConnectEvent
import me.sagiri.char.client.ClientMsgEvent
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import java.net.Socket
import javax.swing.*

class GuiClient : JFrame {
    var clientSocket : Client? = null;
    private var isConnected = false

    val hostJLabel = JLabel("Host: ")
    val hostJTextField = JTextField(20).apply {
        text = "localhost"
    }
    val portJLabel = JLabel("Port: ")
    val portJTextField = JTextField(6).apply {
        text = "8888"
    }
    val connectButton = JButton("connect").apply {
        addActionListener {
            if(isConnected == false) {
                val hostname = hostJTextField.text
                val port = portJTextField.text.toInt()
                val username = nameJTextField.text
                if (hostname != null && username != null) {
                    clientSocket = Client(hostname, port, username).apply {
                        setConnectEvent(object : ClientConnectEvent {
                            override fun onConnect() {
                                msgJTextArea.text += "连接成功\n"
                                isConnected = true
                                connected()
                            }

                            override fun onFailed() {
                                msgJTextArea.text += "连接失败\n"
                                isConnected = false
                                disconnected()
                            }

                            override fun onClose() {
                                isConnected = false
                                msgJTextArea.text += "断开\n"
                                disconnected()
                            }
                        })

                        setMsgEvent(object : ClientMsgEvent {
                            override fun onMsg(msg: String) {
                                msgJTextArea.text += msg + "\n"
                            }
                        })

                        connect()
                    }
                }
            } else {
                // JOptionPane.showMessageDialog(null ,"处于连接状态")
                        disconnected()
            }
        }
    }
    val topJPanel = JPanel().apply {
        add(hostJLabel)
        add(hostJTextField)
        add(portJLabel)
        add(portJTextField)
        add(connectButton)
    }
    val msgJTextArea = JTextArea(10, 50).apply {
        lineWrap = true
        isEditable = false
    }
    val msgJScrollPane = JScrollPane(msgJTextArea).apply {
        setViewportView(msgJTextArea)
        horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
    }
    val cenJPanel = JPanel().apply {
        layout = BorderLayout()
        add(msgJScrollPane)
    }
    val nameJLabel = JLabel("Name: ")
    val nameJTextField = JTextField("sagiri", 6)
    val sendMsgJLabel = JLabel("msg: ")
    val sendMsgJTextField = JTextField(30).apply {
        addActionListener {
            if(this.text.equals("clear")) {
                msgJTextArea.text = ""
                this.text = ""
            } else {
                if (isConnected) {
                    msgJTextArea.text += "${nameJTextField.text}: ${this.text}\n"
                    clientSocket?.sendMsg(this.text)
                    this.text = ""
                } else {
                    JOptionPane.showMessageDialog(this, "没有连接")
                }
            }
        }
    }
    val sendJButton = JButton("send").apply {
        addActionListener {
            sendMsgJTextField.actionListeners[0].actionPerformed(null)
        }
    }
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
        minimumSize = Dimension(600, 600)
        defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
        layout = BorderLayout()

        add(topJPanel, BorderLayout.NORTH)
        add(cenJPanel, BorderLayout.CENTER)
        add(southJPanel, BorderLayout.SOUTH)
    }

    fun connected() {
        connectButton.text = "disconnect"
        nameJTextField.isEditable = false
        hostJTextField.isEditable = false
        portJTextField.isEditable = false
    }

    fun disconnected() {
        clientSocket?.close()
        isConnected = false
        connectButton.text = "connect"
        nameJTextField.isEditable = true
        hostJTextField.isEditable = true
        portJTextField.isEditable = true
    }
}