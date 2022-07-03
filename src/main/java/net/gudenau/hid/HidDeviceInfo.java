package net.gudenau.hid;

import jdk.incubator.foreign.*;
import net.gudenau.hid.internal.Natives;

import java.lang.invoke.VarHandle;
import java.util.Objects;

import static net.gudenau.hid.internal.Natives.*;

public record HidDeviceInfo(MemorySegment segment) {
    static final MemoryLayout LAYOUT = MemoryLayout.structLayout(
        POINTER.withName("path"),
        U16.withName("vendor_id"),
        U16.withName("product_id"),
        MemoryLayout.paddingLayout(32),
        POINTER.withName("serial_number"),
        U16.withName("release_number"),
        MemoryLayout.paddingLayout(48),
        POINTER.withName("manufacturer_string"),
        POINTER.withName("product_string"),
        U16.withName("usage_page"),
        U16.withName("usage"),
        S32.withName("interface_number"),
        POINTER.withName("next")
    );
    
    private static final VarHandle PATH = varHandle(LAYOUT, "path");
    private static final VarHandle VENDOR_ID = MemoryHandles.asUnsigned(varHandle(LAYOUT, "vendor_id"), int.class);
    private static final VarHandle PRODUCT_ID = MemoryHandles.asUnsigned(varHandle(LAYOUT, "product_id"), int.class);
    private static final VarHandle SERIAL_NUMBER = varHandle(LAYOUT, "serial_number");
    private static final VarHandle RELEASE_NUMBER = MemoryHandles.asUnsigned(varHandle(LAYOUT, "release_number"), int.class);
    private static final VarHandle MANUFACTURER_STRING = varHandle(LAYOUT, "manufacturer_string");
    private static final VarHandle PRODUCT_STRING = varHandle(LAYOUT, "product_string");
    private static final VarHandle USAGE_PAGE = MemoryHandles.asUnsigned(varHandle(LAYOUT, "usage_page"), int.class);
    private static final VarHandle USAGE = MemoryHandles.asUnsigned(varHandle(LAYOUT, "usage"), int.class);
    private static final VarHandle INTERFACE_NUMBER = varHandle(LAYOUT, "interface_number");
    private static final VarHandle NEXT = varHandle(LAYOUT, "next");
    
    public HidDeviceInfo {
        Objects.requireNonNull(segment, "segment can't be null");
    }
    
    public HidDeviceInfo(ResourceScope scope) {
        this(MemorySegment.allocateNative(LAYOUT, Objects.requireNonNull(scope, "scope can't be null")));
    }
    
    public HidDeviceInfo(MemoryAddress address) {
        this(MemorySegment.ofAddress(
            Objects.requireNonNull(address, "address can't be null"),
            LAYOUT.byteSize(),
            ResourceScope.globalScope())
        );
    }
    
    public MemoryAddress path() {
        return (MemoryAddress) PATH.get(segment);
    }
    
    public String pathString() {
        return Natives.readString(path());
    }
    
    public int vendor_id() {
        return (int) VENDOR_ID.get(segment);
    }
    
    public int product_id() {
        return (int) PRODUCT_ID.get(segment);
    }
    
    public MemoryAddress serial_number() {
        return (MemoryAddress) SERIAL_NUMBER.get(segment);
    }
    
    public String serial_numberString() {
        return Natives.readWideString(serial_number());
    }
    
    public int release_number() {
        return (int) RELEASE_NUMBER.get(segment);
    }
    
    public MemoryAddress manufacturer_string() {
        return (MemoryAddress) MANUFACTURER_STRING.get(segment);
    }
    
    public String manufacturer_stringString() {
        return Natives.readWideString(manufacturer_string());
    }
    
    public MemoryAddress product_string() {
        return (MemoryAddress) PRODUCT_STRING.get(segment);
    }
    
    public String product_stringString() {
        return Natives.readWideString(product_string());
    }
    
    public int usage_page() {
        return (int) USAGE_PAGE.get(segment);
    }
    
    public int usage() {
        return (int) USAGE.get(segment);
    }
    
    public int interface_number() {
        return (int) INTERFACE_NUMBER.get(segment);
    }
    
    public HidDeviceInfo next() {
        var next = (MemoryAddress) NEXT.get(segment);
        if (next.equals(MemoryAddress.NULL)) {
            return null;
        } else {
            return new HidDeviceInfo(next);
        }
    }
    
    public HidDeviceInfo path(Addressable value) {
        PATH.set(segment, value);
        return this;
    }
    
    public HidDeviceInfo vendor_id(int value) {
        VENDOR_ID.set(segment, value);
        return this;
    }
    
    public HidDeviceInfo product_id(int value) {
        PRODUCT_ID.set(segment, value);
        return this;
    }
    
    public HidDeviceInfo serial_number(Addressable value) {
        SERIAL_NUMBER.set(segment, value);
        return this;
    }
    
    public HidDeviceInfo release_number(int value) {
        RELEASE_NUMBER.set(segment, value);
        return this;
    }
    
    public HidDeviceInfo manufacturer_string(Addressable value) {
        MANUFACTURER_STRING.set(segment, value);
        return this;
    }
    
    public HidDeviceInfo product_string(Addressable value) {
        PRODUCT_STRING.set(segment, value);
        return this;
    }
    
    public HidDeviceInfo usage_page(int value) {
        USAGE_PAGE.set(segment, value);
        return this;
    }
    
    public HidDeviceInfo usage(int value) {
        USAGE.set(segment, value);
        return this;
    }
    
    public HidDeviceInfo interface_number(int value) {
        INTERFACE_NUMBER.set(segment, value);
        return this;
    }
    
    public HidDeviceInfo next(HidDeviceInfo value) {
        NEXT.set(segment, value == null ? MemoryAddress.NULL : value.segment());
        return this;
    }
}
