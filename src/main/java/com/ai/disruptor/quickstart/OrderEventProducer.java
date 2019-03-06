package com.ai.disruptor.quickstart;

import java.nio.ByteBuffer;
import com.lmax.disruptor.RingBuffer;

public class OrderEventProducer {
	
	private RingBuffer<OrderEvent> ringBuffer;
	
	public OrderEventProducer(RingBuffer<OrderEvent> ringBuffer) {
		this.ringBuffer = ringBuffer;
	}
	
	public void sendData(ByteBuffer bb) {
		//1在生产者发送消息的时候，首先需要从我们的ringBuffer里面获取一个可用序列号
		long sequence = ringBuffer.next();
		try {
			//2根据这个序列号，找到具体的"OrderEvent"元素，注意此时获取的OrderEvent对象是一个没有被赋值的空对象
			OrderEvent event = ringBuffer.get(sequence);
			//3进行实际的赋值处理
			event.setValue(bb.getLong(0));
		} finally {
			//4提交操作
			ringBuffer.publish(sequence);
		}
	}
}
