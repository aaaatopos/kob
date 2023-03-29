package com.kob.botrunningsystem.service.impl.utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xzt
 * @version 1.0
 * 是一个生产者消费者模型，如果队列为空，则等待，否则立即执行。
 */
public class BotPool extends Thread {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private Queue<Bot> bots = new LinkedList<>();  // 队列, 生产者加任务，消费者减任务

    /**
     * 将Bot加入消息队列
     * @param userId
     * @param botCode
     * @param input
     */
    public void addBot(Integer userId, String botCode, String input) {
        lock.lock();
        try {
            bots.add(new Bot(userId, botCode, input));
            condition.signalAll(); // 唤醒另外一个阻塞住的线程
        } finally {
            lock.unlock();
        }
    }

    /**
     * 消费一个Bot，进行编译执行，使用joor对代码进行编译
     * @param bot
     */
    private void consume(Bot bot) {
        Consumer consumer = new Consumer();
        consumer.startTimeout(2000, bot);
    }

    @Override
    public void run() {
        while (true) {
            lock.lock();
            if(bots.isEmpty()) {  // 如果队列为空，则线程阻塞
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    lock.unlock();
                    break;
                }
            } else {
                Bot bot = bots.remove();  // 取出队头并移除
                lock.unlock();
                consume(bot);  // 必须放在unlock后面，因为比较耗时，可能会执行几秒中。
            }
        }
    }
}
