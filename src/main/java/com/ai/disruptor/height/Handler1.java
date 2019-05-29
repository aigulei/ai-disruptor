package com.ai.disruptor.height;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

public class Handler1 implements EventHandler<Trade>,WorkHandler<Trade>{
	
	//EventHandler
	@Override
	public void onEvent(Trade event) throws Exception {
		this.onEvent(event);
	}
	
	//WorkHandler
	@Override
	public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
		System.err.println("handler 1:set Name");
		event.setName("H1");
		Thread.sleep(1000);
	}

}
