package org.macbeth.ecommarketapp.limiter;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;


public class ConnectionRegistrar {

    private final LinkedList<Long> connectionList;
    private final int connectionLimit;
    private final int period;

    public ConnectionRegistrar(@NotNull String connectionLimit, @NotNull String period) {
        this.connectionList = new LinkedList<>();
        this.connectionLimit = Integer.parseInt(connectionLimit);
        this.period = Integer.parseInt(period) * 60;
        registerConnection();
    }

    public void registerConnection() {
        long currentTime = System.currentTimeMillis() / 1000;
        connectionList.add(currentTime);
        for (Iterator<Long> iter = connectionList.iterator(); iter.hasNext(); ) {
            if (currentTime - iter.next() > period) {
                iter.remove();
            } else {
                break;
            }
        }
    }

    public boolean isConnectionAllowed() {
        return connectionList.size() < connectionLimit;
    }

}
