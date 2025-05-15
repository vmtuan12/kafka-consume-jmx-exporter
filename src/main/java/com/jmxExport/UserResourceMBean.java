package com.jmxExport;

public interface UserResourceMBean {
    Double getCpuTime();
    Long getPeakUserMemoryBytes();
    Double getExecutionTime();
    Long getPeakTaskTotalMemory();

    Double setCpuTime(Double cpuTime);
    Long setPeakUserMemoryBytes(Long peakUserMemoryBytes);
    Double setExecutionTime(Double executionTime);
    Long setPeakTaskTotalMemory(Long peakTaskTotalMemory);
}
