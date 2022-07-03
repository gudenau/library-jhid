package net.gudenau.hid;

import jdk.incubator.foreign.MemoryAddress;

import java.util.Objects;

public record HidDevice(MemoryAddress address) {
    public HidDevice {
        Objects.requireNonNull(address, "address can't be null");
    }
}
