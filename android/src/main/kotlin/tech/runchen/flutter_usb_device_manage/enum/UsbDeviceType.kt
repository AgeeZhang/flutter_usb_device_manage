package tech.runchen.flutter_usb_device_manage.enum

enum class UsbDeviceType constructor(
    var productId: Int,
    var vendorId: Int
) {
    FINGERPRINT(0x7638, 0x2109),
    RFID(0x6001, 0x403),
    MHD(0x2, 0x3),
}