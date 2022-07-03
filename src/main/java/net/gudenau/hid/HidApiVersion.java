package net.gudenau.hid;

import jdk.incubator.foreign.*;

import java.lang.invoke.VarHandle;
import java.util.Objects;

import static net.gudenau.hid.internal.Natives.*;

public record HidApiVersion(MemorySegment segment) {
    static final MemoryLayout LAYOUT = MemoryLayout.structLayout(
        S32.withName("major"),
        S32.withName("minor"),
        S32.withName("patch")
    );
    
    private static final VarHandle MAJOR = varHandle(LAYOUT, "major");
    private static final VarHandle MINOR = varHandle(LAYOUT, "minor");
    private static final VarHandle PATCH = varHandle(LAYOUT, "patch");
    
    public HidApiVersion {
        Objects.requireNonNull(segment, "segment can't be null");
    }
    
    public HidApiVersion(ResourceScope scope) {
        this(MemorySegment.allocateNative(LAYOUT, Objects.requireNonNull(scope, "scope can't be null")));
    }
    
    public HidApiVersion(MemoryAddress address) {
        this(MemorySegment.ofAddress(
            Objects.requireNonNull(address, "address can't be null"),
            LAYOUT.byteSize(),
            ResourceScope.globalScope())
        );
    }
    
    public int major() {
        return (int)MAJOR.get(segment);
    }
    
    public int minor() {
        return (int)MINOR.get(segment);
    }
    
    public int patch() {
        return (int)PATCH.get(segment);
    }
    
    public HidApiVersion major(int value) {
        MAJOR.set(segment, value);
        return this;
    }
    
    public HidApiVersion minor(int value) {
        MINOR.set(segment, value);
        return this;
    }
    
    public HidApiVersion patch(int value) {
        PATCH.set(segment, value);
        return this;
    }
}
