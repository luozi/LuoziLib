package com.luozi.lib;

import android.util.Log;

public class LogUtils {
	public final static boolean DEBUG = true;

	private static void log (int type, String message) {
		StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[4];
		String className = stackTrace.getClassName();
		String tag = className.substring(className.lastIndexOf('.') + 1) ;
		message = stackTrace.getMethodName() + "#" + stackTrace.getLineNumber()+" [" + message +"]";
		switch (type) {
			case Log.DEBUG:
				Log.d(tag, message);
				break;
			case Log.INFO:
				Log.i(tag, message);
				break;
			case Log.WARN:
				Log.w(tag, message);
				break;
			case Log.ERROR:
				Log.e(tag, message);
				break;
			case Log.VERBOSE:
				Log.v(tag, message);
				break;
		}
	}

	private static void log (int type, String message, Throwable tr) {
		StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[4];
		String className = stackTrace.getClassName();
		String tag = className.substring(className.lastIndexOf('.') + 1) ;
		message = stackTrace.getMethodName() + "#" + stackTrace.getLineNumber()+" [" + message +"]";
		switch (type) {
			case Log.DEBUG:
				Log.d(tag, message, tr);
				break;
			case Log.INFO:
				Log.i(tag, message, tr);
				break;
			case Log.WARN:
				Log.w(tag, message, tr);
				break;
			case Log.ERROR:
				Log.e(tag, message, tr);
				break;
			case Log.VERBOSE:
				Log.v(tag, message, tr);
				break;
		}
	}

	public static void d (String message) {
		if (DEBUG) log(Log.DEBUG, message);
	}

	public static void i (String message) {
		if (DEBUG) log(Log.INFO, message);
	}

	public static void w (String message) {
		if (DEBUG) log(Log.WARN, message);
	}

	public static void e (String message) {
		log(Log.ERROR, message);
	}

	public static void v (String message) {
		if (DEBUG) log(Log.VERBOSE, message);
	}

	public static void d (String message, Throwable tr) {
		if (DEBUG) log(Log.DEBUG, message, tr);
	}

	public static void i (String message, Throwable tr) {
		if (DEBUG) log(Log.INFO, message, tr);
	}

	public static void w (String message, Throwable tr) {
		if (DEBUG) log(Log.WARN, message, tr);
	}

	public static void e (String message, Throwable tr) {
		log(Log.ERROR, message, tr);
	}

	public static void v (String message, Throwable tr) {
		if (DEBUG) log(Log.VERBOSE, message, tr);
	}
}
