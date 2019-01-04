package com.rosdroid.message;

import com.rosdroid.roslib.message.Message;
import com.rosdroid.roslib.message.MessageType;

@MessageType(string = "ros_demo/SayHiRequest")
public class SayHiRequest extends Message {
    public String ask;
}
