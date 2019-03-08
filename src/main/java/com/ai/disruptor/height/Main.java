package com.ai.disruptor.height;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		//构建一个线程池用于提交任务
		ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()/2);
		//1 构建Disruptor
		Disruptor<Trade> disruptor = new Disruptor<Trade>(
				new EventFactory<Trade>() {
					public Trade newInstance() {
						return new Trade();
					}
				}
				, 1024*1024
				, Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()/2)
				, ProducerType.SINGLE
				, new BusySpinWaitStrategy());
		//2 把消费者设置到Disruptor中handleEventsWith
		
		
		//3 启动Disruptor
		RingBuffer<Trade> ringBuffer = disruptor.start();
		
		CountDownLatch latch = new CountDownLatch(1);
		
		long start =System.currentTimeMillis();
		
		es.submit(new TradePublisher(latch,disruptor));
		
		latch.await();
		
		disruptor.shutdown();
		es.shutdown();
		System.out.println("总耗时："+(System.currentTimeMillis()-start));
	}
}
