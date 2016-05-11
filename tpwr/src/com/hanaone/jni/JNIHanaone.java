package com.hanaone.jni;

public class JNIHanaone {
	static{
		System.loadLibrary("JNIHanaone");
	}
	public native String stringFromJNI();
}
