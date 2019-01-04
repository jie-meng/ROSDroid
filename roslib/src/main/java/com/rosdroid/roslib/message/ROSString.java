package com.rosdroid.roslib.message;

@MessageType(string = "std_msgs/String")
public class ROSString extends Message {
    public ROSString() {
    }

    public ROSString(CharSequence charSequence) {
        this.data = charSequence.toString();
    }

    public ROSString(String str) {
        this.data = str;
    }

    public String data;

    @Override
    public String toString() {
        return data;
    }
}
