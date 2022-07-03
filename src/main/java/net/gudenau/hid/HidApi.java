package net.gudenau.hid;

import jdk.incubator.foreign.*;
import net.gudenau.hid.internal.Natives;

import java.lang.invoke.MethodHandle;

import static net.gudenau.hid.internal.Natives.*;

public final class HidApi {
    private static final MethodHandle HID_INIT;
    private static final MethodHandle HID_EXIT;
    private static final MethodHandle HID_ENUMERATE;
    private static final MethodHandle HID_FREE_ENUMERATION;
    private static final MethodHandle HID_OPEN;
    private static final MethodHandle HID_OPEN_PATH;
    private static final MethodHandle HID_WRITE;
    private static final MethodHandle HID_READ_TIMEOUT;
    private static final MethodHandle HID_READ;
    private static final MethodHandle HID_SET_NONBLOCKING;
    private static final MethodHandle HID_SEND_FEATURE_REPORT;
    private static final MethodHandle HID_GET_FEATURE_REPORT;
    private static final MethodHandle HID_GET_INPUT_REPORT;
    private static final MethodHandle HID_CLOSE;
    private static final MethodHandle HID_GET_MANUFACTURER_STRING;
    private static final MethodHandle HID_GET_PRODUCT_STRING;
    private static final MethodHandle HID_GET_SERIAL_NUMBER_STRING;
    private static final MethodHandle HID_GET_INDEXED_STRING;
    private static final MethodHandle HID_ERROR;
    private static final MethodHandle HID_VERSION;
    private static final MethodHandle HID_VERSION_STR;
    
    static {
        var binder = Natives.load();
        HID_INIT = binder.apply("hid_init", FunctionDescriptor.of(S32));
        HID_EXIT = binder.apply("hid_exit", FunctionDescriptor.of(S32));
        HID_ENUMERATE = binder.apply("hid_enumerate", FunctionDescriptor.of(POINTER, U32, U32));
        HID_FREE_ENUMERATION = binder.apply("hid_free_enumeration", FunctionDescriptor.ofVoid(POINTER));
        HID_OPEN = binder.apply("hid_open", FunctionDescriptor.of(POINTER, U32, U32, POINTER));
        HID_OPEN_PATH = binder.apply("hid_open_path", FunctionDescriptor.of(POINTER, POINTER));
        HID_WRITE = binder.apply("hid_write", FunctionDescriptor.of(S32, POINTER, POINTER, SIZE));
        HID_READ_TIMEOUT = binder.apply("hid_read_timeout", FunctionDescriptor.of(S32, POINTER, POINTER, SIZE, S32));
        HID_READ = binder.apply("hid_read", FunctionDescriptor.of(S32, POINTER, POINTER, SIZE));
        HID_SET_NONBLOCKING = binder.apply("hid_set_nonblocking", FunctionDescriptor.of(S32, POINTER, S32));
        HID_SEND_FEATURE_REPORT = binder.apply("hid_send_feature_report", FunctionDescriptor.of(S32, POINTER, POINTER, SIZE));
        HID_GET_FEATURE_REPORT = binder.apply("hid_get_feature_report", FunctionDescriptor.of(S32, POINTER, POINTER, SIZE));
        HID_GET_INPUT_REPORT = binder.apply("hid_get_input_report", FunctionDescriptor.of(S32, POINTER, POINTER, SIZE));
        HID_CLOSE = binder.apply("hid_close", FunctionDescriptor.ofVoid(POINTER));
        HID_GET_MANUFACTURER_STRING = binder.apply("hid_get_manufacturer_string", FunctionDescriptor.of(S32, POINTER, POINTER, SIZE));
        HID_GET_PRODUCT_STRING = binder.apply("hid_get_product_string", FunctionDescriptor.of(S32, POINTER, POINTER, SIZE));
        HID_GET_SERIAL_NUMBER_STRING = binder.apply("hid_get_serial_number_string", FunctionDescriptor.of(S32, POINTER, POINTER, SIZE));
        HID_GET_INDEXED_STRING = binder.apply("hid_get_indexed_string", FunctionDescriptor.of(S32, POINTER, S32, POINTER, SIZE));
        HID_ERROR = binder.apply("hid_error", FunctionDescriptor.of(POINTER, POINTER));
        HID_VERSION = binder.apply("hid_version", FunctionDescriptor.of(POINTER));
        HID_VERSION_STR = binder.apply("hid_version_str", FunctionDescriptor.of(POINTER));
    }
    
    /**
     * Static/compile-time major version of the library.
     */
    public static final int HID_API_VERSION_MAJOR = 0;
    /**
     * Static/compile-time minor version of the library.
     */
    public static final int HID_API_VERSION_MINOR = 12;
    /**
     * Static/compile-time patch version of the library.
     */
    public static final int HID_API_VERSION_PATCH = 0;
    
    /* Helper macros */
    public static String HID_API_AS_STR_IMPL(int x) {
        return String.valueOf(x);
    }
    
    public static String HID_API_AS_STR(int x) {
        return HID_API_AS_STR_IMPL(x);
    }
    
    public static String HID_API_TO_VERSION_STR(int v1, int v2, int v3) {
        return "%d.%d.%d".formatted(v1, v2, v3);
    }
    
    /**
     * Coverts a version as Major/Minor/Patch into a number:
     * <8 bit major><16 bit minor><8 bit patch>.
     * <p>
     * This macro was added in version 0.12.0.
     * <p>
     * Convenient function to be used for compile-time checks, like:
     * #if HID_API_VERSION >= HID_API_MAKE_VERSION(0, 12, 0)
     */
    public static int HID_API_MAKE_VERSION(int mj, int mn, int p) {
        return (((mj) << 24) | ((mn) << 8) | (p));
    }
    
    /**
     * Static/compile-time version of the library.
     * <p>
     * This macro was added in version 0.12.0.
     *
     * @see #HID_API_MAKE_VERSION
     */
    public static final int HID_API_VERSION = HID_API_MAKE_VERSION(HID_API_VERSION_MAJOR, HID_API_VERSION_MINOR, HID_API_VERSION_PATCH);
    
    /**
     * Static/compile-time string version of the library.
     */
    public static final String HID_API_VERSION_STR = HID_API_TO_VERSION_STR(HID_API_VERSION_MAJOR, HID_API_VERSION_MINOR, HID_API_VERSION_PATCH);
    
    /**
     * Initialize the HIDAPI library.
     * <p>
     * This function initializes the HIDAPI library. Calling it is not
     * strictly necessary, as it will be called automatically by
     * hid_enumerate() and any of the hid_open_*() functions if it is
     * needed.  This function should be called at the beginning of
     * execution however, if there is a chance of HIDAPI handles
     * being opened by different threads simultaneously.
     *
     * @return This function returns 0 on success and -1 on error.
     */
    public static int hid_init() {
        try {
            return (int) HID_INIT.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException("Failed to execute hid_init", e);
        }
    }
    
    /**
     * Finalize the HIDAPI library.
     * <p>
     * This function frees all of the static data associated with
     * HIDAPI. It should be called at the end of execution to avoid
     * memory leaks.
     *
     * @return This function returns 0 on success and -1 on error.
     */
    public static int hid_exit() {
        try {
            return (int) HID_EXIT.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException("Failed to execute hid_exit", e);
        }
    }
    
    /**
     * Enumerate the HID Devices.
     * <p>
     * This function returns a linked list of all the HID devices
     * attached to the system which match vendor_id and product_id.
     * If @p vendor_id is set to 0 then any vendor matches.
     * If @p product_id is set to 0 then any product matches.
     * If @p vendor_id and @p product_id are both set to 0, then
     * all HID devices will be returned.
     *
     * @param vendor_id  The Vendor ID (VID) of the types of device
     *                   to open.
     * @param product_id The Product ID (PID) of the types of
     *                   device to open.
     * @return This function returns a pointer to a linked list of type
     * struct #hid_device_info, containing information about the HID devices
     * attached to the system, or NULL in the case of failure. Free
     * this linked list by calling hid_free_enumeration().
     */
    public static HidDeviceInfo hid_enumerate(int vendor_id, int product_id) {
        try {
            var address = (MemoryAddress) HID_ENUMERATE.invokeExact(vendor_id, product_id);
            return address.equals(MemoryAddress.NULL) ? null : new HidDeviceInfo(address);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to execute hid_enumerate", e);
        }
    }
    
    /**
     * Free an enumeration Linked List
     * <p>
     * This function frees a linked list created by hid_enumerate().
     *
     * @param devs Pointer to a list of struct_device returned from
     *             hid_enumerate().
     */
    public static void hid_free_enumeration(HidDeviceInfo devs) {
        try {
            HID_FREE_ENUMERATION.invokeExact((Addressable) devs.segment());
        } catch (Throwable e) {
            throw new RuntimeException("Failed to execute hid_free_enumeration", e);
        }
    }
    
    /**
     * Open a HID device using a Vendor ID (VID), Product ID
     * (PID) and optionally a serial number.
     * <p>
     * If @p serial_number is NULL, the first device with the
     * specified VID and PID is opened.
     * <p>
     * This function sets the return value of hid_error().
     *
     * @param vendor_id     The Vendor ID (VID) of the device to open.
     * @param product_id    The Product ID (PID) of the device to open.
     * @param serial_number The Serial Number of the device to open
     *                      (Optionally NULL).
     * @return This function returns a pointer to a #hid_device object on
     * success or NULL on failure.
     */
    public static HidDevice hid_open(int vendor_id, int product_id, String serial_number) {
        try (var scope = ResourceScope.newConfinedScope()) {
            var allocator = SegmentAllocator.nativeAllocator(scope);
            var serial_numberNative = serial_number == null ? MemoryAddress.NULL : Natives.allocateWideString(allocator, serial_number).address();
            var address = (MemoryAddress) HID_OPEN.invokeExact(vendor_id, product_id, (Addressable) serial_numberNative);
            return address.equals(MemoryAddress.NULL) ? null : new HidDevice(address);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to execute hid_open", e);
        }
    }
    
    /**
     * Open a HID device by its path name.
     * <p>
     * The path name be determined by calling hid_enumerate(), or a
     * platform-specific path name can be used (eg: /dev/hidraw0 on
     * Linux).
     * <p>
     * This function sets the return value of hid_error().
     *
     * @param path The path name of the device to open
     * @return This function returns a pointer to a #hid_device object on
     * success or NULL on failure.
     */
    public static HidDevice hid_open_path(String path) {
        try (var scope = ResourceScope.newConfinedScope()) {
            var allocator = SegmentAllocator.nativeAllocator(scope);
            var pathNative = allocator.allocateUtf8String(path);
            var address = (MemoryAddress) HID_OPEN_PATH.invokeExact((Addressable) pathNative);
            return address.equals(MemoryAddress.NULL) ? null : new HidDevice(address);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to execute hid_open_path", e);
        }
    }
    
    /**
     * Write an Output report to a HID device.
     * <p>
     * The first byte of @p data[] must contain the Report ID. For
     * devices which only support a single report, this must be set
     * to 0x0. The remaining bytes contain the report data. Since
     * the Report ID is mandatory, calls to hid_write() will always
     * contain one more byte than the report contains. For example,
     * if a hid report is 16 bytes long, 17 bytes must be passed to
     * hid_write(), the Report ID (or 0x0, for devices with a
     * single report), followed by the report data (16 bytes). In
     * this example, the length passed in would be 17.
     * <p>
     * hid_write() will send the data on the first OUT endpoint, if
     * one exists. If it does not, it will send the data through
     * the Control Endpoint (Endpoint 0).
     * <p>
     * This function sets the return value of hid_error().
     *
     * @param dev  A device handle returned from hid_open().
     * @param data The data to send, including the report number as
     *             the first byte.
     * @return This function returns the actual number of bytes written and
     * -1 on error.
     */
    public static int hid_write(HidDevice dev, MemorySegment data) {
        try {
            return (int) HID_WRITE.invokeExact((Addressable) dev.address(), (Addressable)data, data.byteSize());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Read an Input report from a HID device with timeout.
     * <p>
     * Input reports are returned
     * to the host through the INTERRUPT IN endpoint. The first byte will
     * contain the Report number if the device uses numbered reports.
     * <p>
     * This function sets the return value of hid_error().
     *
     * @param dev          A device handle returned from hid_open().
     * @param data         A buffer to put the read data into. For devices with
     *                     multiple reports, make sure to read an extra byte for
     *                     the report number.
     * @param milliseconds timeout in milliseconds or -1 for blocking wait.
     * @return This function returns the actual number of bytes read and
     * -1 on error. If no packet was available to be read within
     * the timeout period, this function returns 0.
     */
    public static int hid_read_timeout(HidDevice dev, MemorySegment data, int milliseconds) {
        try {
            return (int) HID_READ_TIMEOUT.invokeExact((Addressable) dev.address(), (Addressable)data, data.byteSize(), milliseconds);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to execute hid_read_timeout", e);
        }
    }
    
    /**
     * Read an Input report from a HID device.
     * <p>
     * Input reports are returned
     * to the host through the INTERRUPT IN endpoint. The first byte will
     * contain the Report number if the device uses numbered reports.
     * <p>
     * This function sets the return value of hid_error().
     *
     * @param dev  A device handle returned from hid_open().
     * @param data A buffer to put the read data into. For devices with
     *             multiple reports, make sure to read an extra byte for
     *             the report number.
     * @return This function returns the actual number of bytes read and
     * -1 on error. If no packet was available to be read and
     * the handle is in non-blocking mode, this function returns 0.
     */
    public static int hid_read(HidDevice dev, MemorySegment data) {
        try {
            return (int) HID_READ.invokeExact((Addressable) dev.address(), (Addressable) data, data.byteSize());
        } catch (Throwable e) {
            throw new RuntimeException("Failed to execute hid_read", e);
        }
    }
    
    /**
     * Set the device handle to be non-blocking.
     * <p>
     * In non-blocking mode calls to hid_read() will return
     * immediately with a value of 0 if there is no data to be
     * read. In blocking mode, hid_read() will wait (block) until
     * there is data to read before returning.
     * <p>
     * Nonblocking can be turned on and off at any time.
     *
     * @param dev      A device handle returned from hid_open().
     * @param nonblock enable or not the nonblocking reads
     * @return This function returns 0 on success and -1 on error.
     */
    public static int hid_set_nonblocking(HidDevice dev, boolean nonblock) {
        try {
            return (int) HID_SET_NONBLOCKING.invokeExact((Addressable) dev.address(), nonblock ? 1 : 0);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to execute hid_set_nonblocking", e);
        }
    }
    
    /**
     * Send a Feature report to the device.
     * <p>
     * Feature reports are sent over the Control endpoint as a
     * Set_Report transfer.  The first byte of @p data[] must
     * contain the Report ID. For devices which only support a
     * single report, this must be set to 0x0. The remaining bytes
     * contain the report data. Since the Report ID is mandatory,
     * calls to hid_send_feature_report() will always contain one
     * more byte than the report contains. For example, if a hid
     * report is 16 bytes long, 17 bytes must be passed to
     * hid_send_feature_report(): the Report ID (or 0x0, for
     * devices which do not use numbered reports), followed by the
     * report data (16 bytes). In this example, the length passed
     * in would be 17.
     * <p>
     * This function sets the return value of hid_error().
     *
     * @param dev  A device handle returned from hid_open().
     * @param data The data to send, including the report number as
     *             the first byte.
     * @return This function returns the actual number of bytes written and
     * -1 on error.
     */
    public static int hid_send_feature_report(HidDevice dev, MemorySegment data) {
        try {
            return (int) HID_SEND_FEATURE_REPORT.invokeExact((Addressable) dev.address(), (Addressable) data, data.byteSize());
        } catch (Throwable e) {
            throw new RuntimeException("Failed to execute hid_send_feature_report", e);
        }
    }
    
    /**
     * Get a feature report from a HID device.
     * <p>
     * Set the first byte of @p data[] to the Report ID of the
     * report to be read.  Make sure to allow space for this
     * extra byte in @p data[]. Upon return, the first byte will
     * still contain the Report ID, and the report data will
     * start in data[1].
     * <p>
     * This function sets the return value of hid_error().
     *
     * @param dev  A device handle returned from hid_open().
     * @param data A buffer to put the read data into, including
     *             the Report ID. Set the first byte of @p data[] to the
     *             Report ID of the report to be read, or set it to zero
     *             if your device does not use numbered reports.
     * @return This function returns the number of bytes read plus
     * one for the report ID (which is still in the first
     * byte), or -1 on error.
     */
    public static int hid_get_feature_report(HidDevice dev, MemorySegment data) {
        try {
            return (int) HID_GET_FEATURE_REPORT.invokeExact((Addressable) dev.address(), (Addressable) data, data.byteSize());
        } catch (Throwable e) {
            throw new RuntimeException("Failed to execute hid_get_feature_report", e);
        }
    }
    
    /**
     * Get a input report from a HID device.
     * <p>
     * Since version 0.10.0, @ref HID_API_VERSION >= HID_API_MAKE_VERSION(0, 10, 0)
     * <p>
     * Set the first byte of @p data[] to the Report ID of the
     * report to be read. Make sure to allow space for this
     * extra byte in @p data[]. Upon return, the first byte will
     * still contain the Report ID, and the report data will
     * start in data[1].
     *
     * @param device A device handle returned from hid_open().
     * @param data   A buffer to put the read data into, including
     *               the Report ID. Set the first byte of @p data[] to the
     *               Report ID of the report to be read, or set it to zero
     *               if your device does not use numbered reports.
     * @return This function returns the number of bytes read plus
     * one for the report ID (which is still in the first
     * byte), or -1 on error.
     */
    public static int hid_get_input_report(HidDevice device, MemorySegment data) {
        try {
            return (int) HID_GET_INPUT_REPORT.invokeExact((Addressable) device.address(), (Addressable) data, data.byteSize());
        } catch (Throwable e) {
            throw new RuntimeException("Failed to execute hid_get_input_report", e);
        }
    }
    
    /**
     * Close a HID device.
     * <p>
     * This function sets the return value of hid_error().
     *
     * @param dev A device handle returned from hid_open().
     */
    public static void hid_close(HidDevice dev) {
        try {
            HID_CLOSE.invokeExact((Addressable) dev.address());
        } catch (Throwable e) {
            throw new RuntimeException("Failed to execute hid_close", e);
        }
    }
    
    /**
     * Get The Manufacturer String from a HID device.
     *
     * @param dev    A device handle returned from hid_open().
     * @param string A wide string buffer to put the data into.
     * @return This function returns 0 on success and -1 on error.
     */
    public static int hid_get_manufacturer_string(HidDevice dev, MemorySegment string) {
        try {
            return (int) HID_GET_MANUFACTURER_STRING.invokeExact((Addressable) dev.address(), (Addressable)string, string.byteSize() >> 1);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to execute hid_get_manufacturer_string", e);
        }
    }
    
    /**
     * Get The Product String from a HID device.
     *
     * @param dev    A device handle returned from hid_open().
     * @param string A wide string buffer to put the data into.
     * @return This function returns 0 on success and -1 on error.
     */
    public static int hid_get_product_string(HidDevice dev, MemorySegment string) {
        try {
            return (int) HID_GET_PRODUCT_STRING.invokeExact((Addressable) dev.address(), (Addressable) string, string.byteSize() >> 1);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to execute hid_get_product_string", e);
        }
    }
    
    /**
     * Get The Serial Number String from a HID device.
     *
     * @param dev    A device handle returned from hid_open().
     * @param string A wide string buffer to put the data into.
     * @return This function returns 0 on success and -1 on error.
     */
    public static int hid_get_serial_number_string(HidDevice dev, MemorySegment string) {
        try {
            return (int) HID_GET_SERIAL_NUMBER_STRING.invokeExact((Addressable) dev.address(), (Addressable) string, string.byteSize() >> 1);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to execute hid_get_serial_number_string", e);
        }
    }
    
    /**
     * Get a string from a HID device, based on its string index.
     *
     * @param dev          A device handle returned from hid_open().
     * @param string_index The index of the string to get.
     * @param string       A wide string buffer to put the data into.
     * @return This function returns 0 on success and -1 on error.
     */
    public static int hid_get_indexed_string(HidDevice dev, int string_index, MemorySegment string) {
        try {
            return (int) HID_GET_INDEXED_STRING.invokeExact((Addressable) dev.address(), string, (Addressable) string, string.byteSize() >> 1);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to execute hid_get_indexed_string", e);
        }
    }
    
    /**
     * Get a string describing the last error which occurred.
     * <p>
     * Whether a function sets the last error is noted in its
     * documentation. These functions will reset the last error
     * to NULL before their execution.
     * <p>
     * Strings returned from hid_error() must not be freed by the user!
     * <p>
     * This function is thread-safe, and error messages are thread-local.
     *
     * @param dev A device handle returned from hid_open(),
     *            or NULL to get the last non-device-specific error
     *            (e.g. for errors in hid_open() itself).
     * @return This function returns a string containing the last error
     * which occurred or NULL if none has occurred.
     */
    public static String hid_error(HidDevice dev) {
        try {
            var address = (MemoryAddress) HID_ERROR.invokeExact((Addressable) dev.address());
            return address.equals(MemoryAddress.NULL) ? null : address.getUtf8String(0);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to execute hid_error", e);
        }
    }
    
    /**
     * Get a runtime version of the library.
     *
     * @return Pointer to statically allocated struct, that contains version.
     */
    public static HidApiVersion hid_version() {
        try {
            var address = (MemoryAddress) HID_VERSION.invokeExact();
            return address.equals(MemoryAddress.NULL) ? null : new HidApiVersion(address);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to execute hid_version", e);
        }
    }
    
    
    /**
     * Get a runtime version string of the library.
     *
     * @return Pointer to statically allocated string, that contains version string.
     */
    public static String hid_version_str() {
        try {
            var address = (MemoryAddress) HID_VERSION_STR.invokeExact();
            return address.equals(MemoryAddress.NULL) ? null : address.getUtf8String(0);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to execute hid_version_str", e);
        }
    }
}
