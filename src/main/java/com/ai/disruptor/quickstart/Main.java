package com.ai.disruptor.quickstart;


import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

public class Main {
	public static void main(String[] args) {
		
		OrderEventFactory eventFactory = new OrderEventFactory();
		int ringBufferSize = 1024 * 1024;
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		/**
		 * eventFactory:消息工厂对象
		 * ringBufferSize:容器长度
		 * executor:线程池
		 * ProducerType:单生产者还是多生产者
		 * waitStrategy:等待策略
		 */
		//1:实例化disruptor对象
		Disruptor<OrderEvent> disruptor = new Disruptor<OrderEvent>(eventFactory
				, ringBufferSize
				, executor
				, ProducerType.SINGLE
				, new BlockingWaitStrategy()); 
		//2:添加消费者的监听
		disruptor.handleEventsWith(new OrderEventHandler());
		
		//3:启动disruptor
		disruptor.start();
		
		//4:获取实际存储数据的容器：RingBuffer
		RingBuffer<OrderEvent> rignBuffer = disruptor.getRingBuffer();
		
		OrderEventProducer producer = new OrderEventProducer(rignBuffer);
		
		ByteBuffer bb = ByteBuffer.allocate(8);
		
		for(long i=0;i<100;i++) {
			bb.putLong(0,i);
			producer.sendData(bb);
		}
		
		disruptor.shutdown();
		executor.shutdown();
	}
}