package com.ai.disruptor.height;

import com.lmax.disruptor.EventHandler;

public class Handler3 implements EventHandler<Trade>{

	@Override
	public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
		System.err.println("handler 3:Name:"+event.getName()
							+",Id:"+event.getId()
							+" INSTANCE:"+event.toString());
	}

}
