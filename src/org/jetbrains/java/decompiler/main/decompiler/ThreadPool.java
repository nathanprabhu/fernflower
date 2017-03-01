package org.jetbrains.java.decompiler.main.decompiler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
	

	//public static ExecutorService executor = new ThreadPoolExecutor(5,5,0L,TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(50));
	public static ExecutorService executor = Executors.newCachedThreadPool();
}

