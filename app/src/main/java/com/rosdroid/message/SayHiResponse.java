package com.rosdroid.message;

import com.rosdroid.roslib.message.Message;
import com.rosdroid.roslib.message.MessageType;

@MessageType(string = "ros_demo/SayHiResponse")
public class SayHiResponse extends Message {
    public String answer;
}
