package com.gjjbook.dao;

import com.gjjbook.dao.connectionPool.ConcurrentConnectionPool;
import com.gjjbook.dao.connectionPool.ConnectionPool;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

@SuppressWarnings("Duplicates")
public class ConcurrentConnectionPoolTest {
    private ConnectionPool connectionPool;

    @Before
    public void setUp() throws Exception, DaoException {
        connectionPool = ConcurrentConnectionPool.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        connectionPool.close();
    }

    @Test
    public void getConnection() throws DaoException {
        Assert.assertNotNull(connectionPool.getConnection());
    }

    @Test
    public void threadLocalConnection() throws InterruptedException, SQLException, DaoException {
        final Connection[] connections = new Connection[4];
        Thread threadA = new Thread(
                () -> {
                    try {
                        connections[0] = connectionPool.getConnection();
                        connections[1] = connectionPool.getConnection();
                    } catch (DaoException e) {
                        e.printStackTrace();
                    }
                }
        );
        Thread threadB = new Thread(
                () -> {
                    try {
                        connections[2] = connectionPool.getConnection();
                        connections[3] = connectionPool.getConnection();
                    } catch (DaoException e) {
                        e.printStackTrace();
                    }
                }
        );

        threadA.start();
        threadB.start();
        threadA.join();
        threadB.join();

        Assert.assertEquals(connections[0], connections[1]);
        Assert.assertEquals(connections[2], connections[3]);
        Assert.assertNotEquals(connections[0], connections[2]);
    }

    @Test
    public void reuseConnection() throws DaoException, InterruptedException, SQLException {
        final Connection[] connections = new Connection[4];
        Thread threadA = new Thread(
                () -> {
                    try {
                        connections[0] = connectionPool.getConnection();
                        connections[1] = connectionPool.getConnection();
                    } catch (DaoException e) {
                        e.printStackTrace();
                    }
                }
        );
        Thread threadB = new Thread(
                () -> {
                    try {
                        connections[2] = connectionPool.getConnection();
                        connections[3] = connectionPool.getConnection();
                    } catch (DaoException e) {
                        e.printStackTrace();
                    }
                }
        );

        threadA.start();
        threadA.join();
        connectionPool.recycle(connections[0]);

        threadB.start();
        threadB.join();

        Assert.assertEquals(connections[0], connections[1]);
        Assert.assertEquals(connections[0], connections[2]);
        Assert.assertEquals(connections[0], connections[3]);
    }

    @Test
    public void doubleRecycle() throws InterruptedException, SQLException, DaoException {
        final Connection[] connections = new Connection[2];
        Thread threadA = new Thread(
                () -> {
                    try {
                        connections[0] = connectionPool.getConnection();
                        connectionPool.recycle(connections[0]);
                        Thread.sleep(2000);
                        connectionPool.recycle(connections[0]);
                    } catch (DaoException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );
        Thread threadB = new Thread(
                () -> {
                    try {
                        Thread.sleep(1000);
                        connections[1] = connectionPool.getConnection();
                        Thread.sleep(3000);
                        connectionPool.recycle(connections[1]);
                    } catch (DaoException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );

        threadA.start();
        threadB.start();
        threadB.join();
        threadA.join();

        Assert.assertEquals(connections[0], connections[1]);
    }

    @Test
    public void takeAllConnections() throws InterruptedException, SQLException, DaoException {
        final Connection[] connections = new Connection[3];
        Thread threadA = new Thread(
                () -> {
                    try {
                        connections[0] = connectionPool.getConnection();
                        Thread.sleep(2000);
                        connectionPool.recycle(connections[0]);
                    } catch (DaoException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );
        Thread threadB = new Thread(
                () -> {
                    try {
                        connections[1] = connectionPool.getConnection();
                        Thread.sleep(3000);
                        connectionPool.recycle(connections[1]);
                    } catch (DaoException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );
        Thread threadC = new Thread(
                () -> {
                    try {
                        Thread.sleep(1000);
                        connections[2] = connectionPool.getConnection();
                        connectionPool.recycle(connections[2]);
                    } catch (DaoException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );

        threadA.start();
        threadB.start();
        threadC.start();
        threadA.join();
        threadB.join();
        threadC.join();

        Assert.assertEquals(connections[0], connections[2]);
    }
}
