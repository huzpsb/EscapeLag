package com.mcml.space.util;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import javax.management.MBeanServer;

public class HeapDumper {
	private static volatile Object MXBean;

	public static void dumpHeap(File file) {
		if(MXBean == null) {
			if(!HeapDumper.init()) {
				AzureAPI.log("Dump内存堆失败!");
				return;
			}
		}
		try {
			Method method = Class.forName("com.sun.management.HotSpotDiagnosticMXBean").getMethod("dumpHeap",
					new Class[] { String.class, Boolean.TYPE });
			method.invoke(MXBean, new Object[] { file.toString(), true });
		} catch (Exception rex) {
			AzureAPI.log("Dump内存堆失败!");
			AzureAPI.log(rex.toString());
		}
	}

	private static boolean init() {
		try {
			Class<?> clazz = Class.forName("com.sun.management.HotSpotDiagnosticMXBean");
			MBeanServer server = ManagementFactory.getPlatformMBeanServer();
			MXBean = ManagementFactory.newPlatformMXBeanProxy(server, "com.sun.management:type=HotSpotDiagnostic",
					clazz);
			return true;
		} catch (Exception rex) {
			AzureAPI.log("无法初始化Dump内存堆系统!");
			AzureAPI.log(rex.toString());
			return false;
		}
	}
}
