package com.ai.disruptor.height;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;

public class Main {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws InterruptedException {
		//构建一个线程池用于提交任务
		ExecutorService es1 = Executors.newFixedThreadPool(1);
		ExecutorService es2 = Executors.newFixedThreadPool(5);
		//1 构建Disruptor
		Disruptor<Trade> disruptor = new Disruptor<Trade>(
				new EventFactory<Trade>() {
					public Trade newInstance() {
						return new Trade();
					}
				}
				, 1024*1024
				, es2
				, ProducerType.SINGLE
				, new BusySpinWaitStrategy());
		//2 把消费者设置到Disruptor中handleEventsWith
		
		//2.1串行操作
		//disruptor.handleEventsWith(new Handler1()).handleEventsWith(new Handler2()).handleEventsWith(new Handler3());
		
		//2.2并行操作
//		disruptor.handleEventsWith(new Handler1(),new Handler2(),new Handler3());
//		disruptor.handleEventsWith(new Handler2());
//		disruptor.handleEventsWith(new Handler3());
		
		//2.3菱形操作（一）
//		disruptor.handleEventsWith(new Handler1(),new Handler2()).handleEventsWith(new Handler3());
		
		//2.3菱形操作（二）
//		EventHandlerGroup<Trade> eventHandlerGroup = disruptor.handleEventsWith(new Handler1(),new Handler2());
//		eventHandlerGroup.then(new Handler3());
		
		//2.4六边形操作
		Handler1 h1 = new Handler1();
		Handler2 h2 = new Handler2();
		Handler3 h3 = new Handler3();
		Handler4 h4 = new Handler4();
		Handler5 h5 = new Handler5();
 		disruptor.handleEventsWith(h1,h4);
		disruptor.after(h1).handleEventsWith(h2);
		disruptor.after(h4).handleEventsWith(h5);
		disruptor.after(h2,h5).handleEventsWith(h3);
		
		
		//3 启动Disruptor
		RingBuffer<Trade> ringBuffer = disruptor.start();
		
		CountDownLatch latch = new CountDownLatch(1);
		
		long start =System.currentTimeMillis();
		
		es1.submit(new TradePublisher(latch,disruptor));
		
		latch.await();
		
		disruptor.shutdown();
		es1.shutdown();
		es2.shutdown();
		System.out.println("总耗时："+(System.currentTimeMillis()-start));
	}
}
