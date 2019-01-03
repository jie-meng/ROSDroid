package com.rosdroid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.rosdroid.roslib.ROSClient
import com.rosdroid.roslib.Topic
import com.rosdroid.roslib.message.ROSString
import com.rosdroid.roslib.rosbridge.ROSBridgeClient
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private val client = ROSBridgeClient()
    private lateinit var chatterTopic: Topic<ROSString>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        connect.setOnClickListener {
            clickConnect()
        }

        send_topic.setOnClickListener {
            sendTopic()
        }
    }

    private fun clickConnect() {
        client.uriString = "ws://${ros_bridge_ip.text}:9090"
        val connect = client.connect(object : ROSClient.ConnectionStatusListener {
            override fun onConnect() {
                runOnUiThread {
                    connect_status.setBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            android.R.color.holo_green_dark
                        )
                    )

                    subscribeChatterTopic()
                }
            }

            override fun onDisconnect(normal: Boolean, reason: String?, code: Int) {
                runOnUiThread {
                    connect_status.setBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            android.R.color.darker_gray
                        )
                    )
                }
            }

            override fun onError(ex: Exception?) {
                runOnUiThread {
                    connect_status.setBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            android.R.color.holo_red_dark
                        )
                    )
                }
            }
        })

        if (!connect) {
            connect_status.setBackgroundColor(
                ContextCompat.getColor(
                    this@MainActivity,
                    android.R.color.darker_gray
                )
            )
        }
    }

    private fun subscribeChatterTopic() {
        chatterTopic = Topic("/chatter", ROSString::class.java, client)
        chatterTopic.subscribe { message ->
            runOnUiThread {
                receive_topic.text = message!!.data + "\n" + receive_topic.text.toString()
            }
        }
    }

    private fun sendTopic() {
        val rosString = ROSString()
        rosString.data = text_send.text.toString()
        chatterTopic.publish(rosString)
    }

    private fun disconnect() {
        chatterTopic.unsubscribe()
        client.disconnect()
    }

    override fun onDestroy() {
        disconnect()
        super.onDestroy()
    }
}
