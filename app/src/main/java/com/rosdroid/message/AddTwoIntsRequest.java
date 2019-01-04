package com.rosdroid.message;

import com.rosdroid.roslib.message.Message;
import com.rosdroid.roslib.message.MessageType;

@MessageType(string = "ros_demo/AddTwoIntsRequest")
public class AddTwoIntsRequest extends Message {
    public int a;
    public int b;
}
