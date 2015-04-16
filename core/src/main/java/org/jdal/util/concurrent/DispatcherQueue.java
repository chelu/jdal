/*
 * Copyright 2009-2012 Jose Luis Martin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jdal.util.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.task.SyncTaskExecutor;

/**
 * Wrapper for a BlockingQueue that dispatchs incoming objets to a Task Executor.
 * You need to configure a Dispatcher or override dispatch(T t) mehod.   
 * 
 * @author Jose Luis Martin
 * @param <T> Objetc type stored in Queue.
 * @since 1.3
 */
public class DispatcherQueue<T>  {

	private static final Log log = LogFactory.getLog(DispatcherQueue.class);
	private BlockingQueue<T> queue = new LinkedBlockingQueue<T>();
	private Thread dispatcherThread;
	/** true when dispatching messages */
	private volatile boolean running = false;
	/** Task executor to execute Dispatchers as Runnables */
	private Executor executor = new SyncTaskExecutor();
	/** Dispatcher instance */
	private Dispatcher<T> dispatcher;
	
	public DispatcherQueue() {
		
	}
	
	public DispatcherQueue(Dispatcher<T> dispatcher) {
		this.dispatcher = dispatcher;
	}

	/**
	 * Create and start dispatcher thread
	 */
	public void init() {
		start();
	}
	
	/**
	 * Dispatch incomming objects to a Executor
	 */
	private void dispatch() {
		while (running) {
			T t = null;
			try {
				t = queue.take();
				
				if (log.isDebugEnabled())
					log.debug("Dispatching object: " + t.toString());
				
				dispatcher.dispatch(t);
				
			} 
			catch (InterruptedException e) {
				log.error(e);
				break;
			}
			catch (Exception e) {
				log.error("Dispather failed. Readding object to queue again.");
				log.error(e.getMessage());
				queue.offer(t);
			}
		}
	}
	
	/**
	 * Create a Runnable to dispatch incomming messages
	 * @param msg message to be dispatched
	 * @return Runnable
	 */
	protected Runnable createWorker(final T t) {
		return new Runnable() {
			public void run() {
				dispatch(t);
			}
		};
	}
	
	/**
	 * Dispatch objects in Queue
	 * @param msg 
	 */
	protected void dispatch(T t) {
		if (dispatcher != null) {
			try  {
				dispatcher.dispatch(t);
			} 
			catch (Exception e) {
				log.error(e);
			}
		}
		else {
			log.error("Dispatcher is null, Override default dispatch method or " +
					"configure a dispatcher.");
		}
	}

	/**
	 * Gets the number of messages on the queue.
	 * @return queue size
	 * @see java.util.Collection#size()
	 */
	public int size() {
		return queue.size();
	}

	/**
	 * Insert a Object to the queue, waiting if necessary.
	 * @param t object to put on queue
	 * @see java.util.concurrent.BlockingQueue#put(java.lang.Object)
	 */
	public void put(T t)  {
		try {
			queue.put(t);
		} catch (InterruptedException e) {
			log.error(e);
		}
	}

	/**
	 * Clear all messages on the queue
	 * @see java.util.Collection#clear()
	 */
	public void clear() {
		queue.clear();
	}

	/** 
	 * Stop Dispatcher
	 */
	public void stop() {
		running = false;
		dispatcherThread.interrupt();
		try {
			dispatcherThread.join();
		} catch (InterruptedException e) {
			log.error(e);
		}
	}
	
	public void start() {
		if (running) {
			log.warn("Already running");
			return;
		}
		
		dispatcherThread = new Thread(new Runnable() {
			public void run() {
				dispatch();
			}
		});
		
		running = true;
		log.info("Starting dispatcher thread");
		dispatcherThread.start();
	}

	/**
	 * @return the dispatcher
	 */
	public Dispatcher<T> getDispatcher() {
		return dispatcher;
	}

	/**
	 * @param dispatcher the dispatcher to set
	 */
	public void setDispatcher(Dispatcher<T> dispatcher) {
		this.dispatcher = dispatcher;
	}
	
	/**
	 * @return the queue
	 */
	public BlockingQueue<T> getQueue() {
		return queue;
	}

	/**
	 * @param queue the queue to set
	 */
	public void setQueue(BlockingQueue<T> queue) {
		this.queue = queue;
	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * @param running the running to set
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}

	/**
	 * @return the executor
	 */
	public Executor getExecutor() {
		return executor;
	}

	/**
	 * @param executor the executor to set
	 */
	public void setExecutor(Executor executor) {
		this.executor = executor;
	}
	
	public interface Dispatcher<T> {
		void dispatch(T t);
	}
}
