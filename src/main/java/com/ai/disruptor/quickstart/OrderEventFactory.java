package com.ai.disruptor.quickstart;

import com.lmax.disruptor.EventFactory;

/**
 *
 * @author Administrator
 *
 */
public class OrderEventFactory implements EventFactory<OrderEvent> {
	
	@Override
	public OrderEvent newInstance() {
		return new OrderEvent();
	}

}
