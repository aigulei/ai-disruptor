package com.ai.disruptor.height;

import com.lmax.disruptor.EventHandler;

public class Handler4 implements EventHandler<Trade>{

	@Override
	public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
		System.err.println("Handler 4:set Price");
		event.setPrice(66.0);
	}

}
