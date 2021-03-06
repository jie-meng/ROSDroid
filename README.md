# ROSDroid -- An ROS libray for Android to connect with ROS bridge

[![](https://jitpack.io/v/jie-meng/ROSDroid.svg)](https://jitpack.io/#jie-meng/ROSDroid)

Library code comes from [ROSBridgeClient](https://github.com/djilk/ROSBridgeClient), ROSDroid project is a wrapper and you can add it to your project from __jitpack.io__.

Do not define Message class with __Kotlin__, use __java__, otherwise communication with ROS bridge would fail. 

## How to import ROSDroid library into Android project

1. Add the JitPack repository to your build file
    - gradle

        Add it in your root build.gradle at the end of repositories:

        ```
        allprojects {
            repositories {
                ...
                maven { url 'https://jitpack.io' }
            }
        }
        ```

    - maven

        ```
        <repositories>
            <repository>
                <id>jitpack.io</id>
                <url>https://jitpack.io</url>
            </repository>
        </repositories>
        ```

2. Add the dependency
    - gradle

        ```
        dependencies {
                implementation 'com.github.jie-meng:ROSDroid:v1.0.0'
        }
        ```

    - maven

        ```
        <dependency>
            <groupId>com.github.jie-meng</groupId>
            <artifactId>ROSDroid</artifactId>
            <version>v1.0.0</version>
        </dependency>
        ```