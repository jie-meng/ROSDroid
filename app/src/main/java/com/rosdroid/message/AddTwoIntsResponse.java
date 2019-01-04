package com.rosdroid.message;

import com.rosdroid.roslib.message.Message;
import com.rosdroid.roslib.message.MessageType;

@MessageType(string = "ros_demo/AddTwoIntsResponse")
public class AddTwoIntsResponse extends Message {
    public int sum;
}
