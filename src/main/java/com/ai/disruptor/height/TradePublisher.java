package com.ai.disruptor.height;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;

public class TradePublisher implements Runnable{
	
	private Disruptor<Trade> disruptor;
	private CountDownLatch countDownLatch;
	private static int PUBLISH_COUNT = 1;
	public TradePublisher(CountDownLatch countDownLatch, Disruptor<Trade> disruptor) {
		this.disruptor = disruptor;
		this.countDownLatch = countDownLatch;
	}
	
	@Override
	public void run() {
		TradeEventTranslator eventTranslator = new TradeEventTranslator();
		for(int i =0;i<PUBLISH_COUNT;i++) {
			//新的提交任务方式
			disruptor.publishEvent(eventTranslator);
		}
		countDownLatch.countDown();
	}
}

class TradeEventTranslator implements EventTranslator<Trade>{
	
	private Random random = new Random();
	@Override
	public void translateTo(Trade event, long sequence) {
		this.generateTrade(event);
	}
	
	private void generateTrade(Trade event) {
		event.setPrice(random.nextDouble() * 9999);
	}
}













