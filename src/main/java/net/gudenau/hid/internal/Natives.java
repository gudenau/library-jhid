package net.gudenau.hid.internal;

import jdk.incubator.foreign.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.function.BiFunction;

public final class Natives {
    private Natives(){}
    
    private static final MemorySegment EVERYTHING = MemorySegment.ofAddress(MemoryAddress.NULL, Long.MAX_VALUE, ResourceScope.globalScope());
    
    public static final MemoryLayout U8 = MemoryLayout.valueLayout(byte.class, ByteOrder.nativeOrder())
        .withBitAlignment(Byte.SIZE);
    public static final MemoryLayout U16 = MemoryLayout.valueLayout(short.class, ByteOrder.nativeOrder())
        .withBitAlignment(Short.SIZE);
    public static final MemoryLayout U32 = MemoryLayout.valueLayout(int.class, ByteOrder.nativeOrder())
        .withBitAlignment(Integer.SIZE);
    public static final MemoryLayout U64 = MemoryLayout.valueLayout(long.class, ByteOrder.nativeOrder())
        .withBitAlignment(Long.SIZE);
    
    public static final MemoryLayout S8 = U8;
    public static final MemoryLayout S16 = U16;
    public static final MemoryLayout S32 = U32;
    public static final MemoryLayout S64 = U64;
    
    public static final MemoryLayout F32 = MemoryLayout.valueLayout(float.class, ByteOrder.nativeOrder())
        .withBitAlignment(Float.SIZE);
    public static final MemoryLayout F64 = MemoryLayout.valueLayout(double.class, ByteOrder.nativeOrder())
        .withBitAlignment(Double.SIZE);
    
    public static final MemoryLayout SIZE = U64;
    public static final MemoryLayout POINTER = MemoryLayout.valueLayout(MemoryAddress.class, ByteOrder.nativeOrder())
        .withBitAlignment(Long.SIZE);
    
    public static VarHandle varHandle(MemoryLayout layout, String name) {
        return layout.varHandle(MemoryLayout.PathElement.groupElement(name));
    }
    
    public static String readString(MemoryAddress address) {
        if (address.equals(MemoryAddress.NULL)) {
            return null;
        }
        
        return address.getUtf8String(0);
    }
    
    public static String readWideString(MemoryAddress address) {
        if (address.equals(MemoryAddress.NULL)) {
            return null;
        }
        
        int length;
        for(length = 0; length < Integer.MAX_VALUE >> 1; length++) {
            if(address.getAtIndex(ValueLayout.JAVA_SHORT, length) == 0) {
                break;
            }
        }
        
        var bytes = new byte[length << 1];
        MemorySegment.copy(EVERYTHING, ValueLayout.JAVA_BYTE, address.toRawLongValue(), bytes, 0, length << 1);
        return new String(bytes, StandardCharsets.UTF_16);
    }
    
    public static BiFunction<String, FunctionDescriptor, MethodHandle> load() {
        System.load(Path.of("libhid.so").toAbsolutePath().toString());
        
        var lookup = SymbolLookup.loaderLookup();
        var linker = CLinker.systemCLinker();
        
        return (name, descriptor) -> {
            var symbol = lookup.lookup(name)
                .orElseThrow(() -> new RuntimeException("Failed to find symbol " + name));
            return linker.downcallHandle(symbol, descriptor);
        };
    }
    
    public static MemorySegment allocateWideString(SegmentAllocator allocator, String string) {
        var bytes = string.getBytes(StandardCharsets.UTF_16);
        var segment = allocator.allocate(bytes.length + 2L, 2);
        segment.copyFrom(MemorySegment.ofArray(bytes));
        return segment;
    }
}
