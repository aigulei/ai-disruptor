package com.ai.disruptor.height.multi;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WorkerPool;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;


public class Main {
	
	public static void main(String[] args) throws InterruptedException {
		//1创建RingBuffer
		RingBuffer<Order> ringBuffer = 
				RingBuffer.create(ProducerType.MULTI, new EventFactory<Order>() {

					@Override
					public Order newInstance() {
						return new Order();
					}
				}, 1024 * 1024, new YieldingWaitStrategy());
		//2通过ringBuffer创建一个内存屏障
		SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
		//3构建多消费者
		Consumer[] consumers = new Consumer[10];
		
		for(int i=0;i < consumers.length;i++) {
			consumers[i] = new Consumer("C"+i);
		}
		//4构建多消费工作池
		WorkerPool<Order> workerPool = new WorkerPool<Order>(ringBuffer
				, sequenceBarrier
				, new EventExceptionhandler()
				, consumers);
		//5设置多个消费者的sequence序号 用于单独统计消费进度,并且设置到ringBuffer中
		ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
		//6 启动workerPool
		workerPool.start(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
		
		CountDownLatch latch = new CountDownLatch(1);
		for(int i =0;i<100;i++) {
			Producer producer = new Producer(ringBuffer);
			new Thread(()->{
				try {
					latch.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				for(int j=0;j<100;j++) {
					producer.sendData(UUID.randomUUID().toString());
				}
			}).start();
		}
		Thread.sleep(3000);
		
		System.err.println("----------线程创建完毕，开始生产数据----------");
		
		latch.countDown();
		Thread.sleep(10000);
		
		System.err.println("任务总数:"+consumers[2].getCount());
		
	}
	static class EventExceptionhandler implements ExceptionHandler<Order>{

		@Override
		public void handleEventException(Throwable ex, long sequence, Order event) {
			
		}

		@Override
		public void handleOnStartException(Throwable ex) {
			
		}

		@Override
		public void handleOnShutdownException(Throwable ex) {
			
		}
		
	}
	
}
