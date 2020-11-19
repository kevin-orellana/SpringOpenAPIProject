package dev.codescreen.ServiceInterfaces;

import java.util.concurrent.locks.ReadWriteLock;

/*
* Emulate a distributed locking system that can be used across multiple computing nodes.
* Ideally, a service like Zookeeper or RabbitMQ is used for this purpose.
* */
public interface DistributedLockService {
    public ReadWriteLock getReadWriteLock(String path);

    public void releaseReadLock();
    public void releaseWriteLock();
}
