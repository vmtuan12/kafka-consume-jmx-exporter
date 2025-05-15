package com.jmxExport;

public class UserResource implements UserResourceMBean {
    Double cpuTime = 0.0;
    Double executionTime = 0.0;
    Long peakUserMemoryBytes = 0L;
    Long peakTaskTotalMemory = 0L;

    @Override
    public Double getCpuTime() {
        return this.cpuTime;
    }

    @Override
    public Long getPeakUserMemoryBytes() {
        return this.peakUserMemoryBytes;
    }

    @Override
    public Double getExecutionTime() {
        return this.executionTime;
    }

    @Override
    public Long getPeakTaskTotalMemory() {
        return this.peakTaskTotalMemory;
    }

    @Override
    public Double setCpuTime(Double cpuTime) {
        this.cpuTime = cpuTime;
        return this.cpuTime;
    }

    @Override
    public Long setPeakUserMemoryBytes(Long peakUserMemoryBytes) {
        this.peakUserMemoryBytes = peakUserMemoryBytes;
        return this.peakUserMemoryBytes;
    }

    @Override
    public Double setExecutionTime(Double executionTime) {
        this.executionTime = executionTime;
        return this.executionTime;
    }

    @Override
    public Long setPeakTaskTotalMemory(Long peakTaskTotalMemory) {
        this.peakTaskTotalMemory = peakTaskTotalMemory;
        return this.peakTaskTotalMemory;
    }
}
