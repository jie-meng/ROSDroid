package com.rosdroid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.rosdroid.message.AddTwoIntsRequest
import com.rosdroid.message.AddTwoIntsResponse
import com.rosdroid.message.SayHiRequest
import com.rosdroid.message.SayHiResponse
import com.rosdroid.roslib.ROSClient
import com.rosdroid.roslib.Service
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
        initROS()
        initListeners()
    }

    private fun initROS() {
        chatterTopic = Topic("/chatter", ROSString::class.java, client)
    }

    private fun initListeners() {
        connect.setOnClickListener {
            if (isConnected()) {
                disconnect()
            } else {
                connect()
            }
        }

        subscribe.setOnClickListener {
            if (isSubscribed()) {
                unsubscribe()
            } else {
                subscribe()
            }
        }

        send_topic.setOnClickListener {
            sendTopic()
        }

        call_service_add_two_ints.setOnClickListener {
            callServiceAddTwoInts()
        }

        call_service_say_hi.setOnClickListener {
            callServiceSayHi()
        }
    }

    private fun checkSrvInterface() {
        client.services.forEach { it ->
            println(">>>>>>>> $it")
            client.getServiceRequestDetails(it).print()
            client.getServiceResponseDetails(it).print()
            println("")
        }
    }

    private fun callServiceAddTwoInts() {
        val addTwoIntsService = Service<AddTwoIntsRequest, AddTwoIntsResponse>("/add_two_ints", AddTwoIntsRequest::class.java, AddTwoIntsResponse::class.java, client)
        val addTwoIntRequest = AddTwoIntsRequest()
        addTwoIntRequest.a = 9
        addTwoIntRequest.b = 5
        addTwoIntsService.callWithHandler(addTwoIntRequest) {
            res -> call_server_result.text = res.sum.toString()
        }
    }

    private fun callServiceSayHi() {
        val sayHiService = Service<SayHiRequest, SayHiResponse>("/say_hi", SayHiRequest::class.java, SayHiResponse::class.java, client)
        val sayHiRequest = SayHiRequest()
        sayHiRequest.ask = "hello"
        sayHiService.callWithHandler(sayHiRequest) {
                res -> call_server_result.text = res.answer.toString()
        }
    }

    private fun subscribe() {
        chatterTopic.subscribe { message ->
            runOnUiThread {
                receive_topic.text = message!!.data + "\n" + receive_topic.text.toString()
            }
        }

        subscribe.text = getString(R.string.unsubscribe)
    }

    private fun unsubscribe() {
        chatterTopic.unsubscribe()
        subscribe.text = getString(R.string.subscribe)
    }

    private val connectionStatusListener = object : ROSClient.ConnectionStatusListener {
        override fun onConnect() {
            runOnUiThread {
                setConnectStatus()
            }
        }

        override fun onDisconnect(normal: Boolean, reason: String?, code: Int) {
            runOnUiThread {
                setDisconnectStatus()
            }
        }

        override fun onError(ex: Exception?) {
            runOnUiThread {
                setConnectErrorStatus()
            }
        }
    }

    private fun connect() {
        client.uriString = "ws://${ros_bridge_ip.text}:9090"
        if (!client.connect(connectionStatusListener)) {
            setDisconnectStatus()
        }
    }

    private fun setConnectStatus() {
        connect.text = getString(R.string.disconnect)
        connect_status.setBackgroundColor(
            ContextCompat.getColor(
                this@MainActivity,
                android.R.color.holo_green_dark
            )
        )
        subscribe.isEnabled = true
        send_topic.isEnabled = true
        call_service_add_two_ints.isEnabled = true
        call_service_say_hi.isEnabled = true
        ros_bridge_ip.isEnabled = false
    }

    private fun setDisconnectStatus() {
        connect.text = getString(R.string.connect)
        connect_status.setBackgroundColor(
            ContextCompat.getColor(
                this@MainActivity,
                android.R.color.darker_gray
            )
        )
        subscribe.isEnabled = false
        send_topic.isEnabled = false
        call_service_add_two_ints.isEnabled = false
        call_service_say_hi.isEnabled = false
        ros_bridge_ip.isEnabled = true
    }

    private fun setConnectErrorStatus() {
        connect.text = getString(R.string.connect)
        connect_status.setBackgroundColor(
            ContextCompat.getColor(
                this@MainActivity,
                android.R.color.holo_red_dark
            )
        )
        subscribe.isEnabled = false
        send_topic.isEnabled = false
        call_service_add_two_ints.isEnabled = false
        call_service_say_hi.isEnabled = false
        ros_bridge_ip.isEnabled = true
    }

    private fun sendTopic() {
        val rosString = ROSString(text_send.text)
        chatterTopic.publish(rosString)
    }

    private fun disconnect() {
        if (isSubscribed()) {
            unsubscribe()
        }

        client.disconnect()
    }

    private fun isConnected(): Boolean {
        return connect.text == getString(R.string.disconnect)
    }

    private fun isSubscribed(): Boolean {
        return subscribe.text == getString(R.string.unsubscribe)
    }

    override fun onDestroy() {
        if (isConnected()) {
            disconnect()
        }
        super.onDestroy()
    }
}
