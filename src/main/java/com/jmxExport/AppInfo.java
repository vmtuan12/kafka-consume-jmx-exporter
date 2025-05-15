package com.jmxExport;

public class AppInfo implements AppInfoMBean {
    private long counter = 0l;

    @Override
    public long getCounter() {
        return this.counter;
    }

    @Override
    public long incrementCounter() {
        this.counter += 1;
        return this.counter;
    }
}
