import 'flutter_usb_device_manage_platform_interface.dart';

class FlutterUsbRFIDManage {
  Future<bool?> openUsbSerial() {
    return FlutterUsbDeviceManagePlatform.instance.rfidOpenUsbSerial();
  }

  Future<bool?> closeUsbSerial() {
    return FlutterUsbDeviceManagePlatform.instance.rfidCloseUsbSerial();
  }

  Future<bool?> startRead(int mode, int timeout) {
    return FlutterUsbDeviceManagePlatform.instance.rfidStartRead(mode, timeout);
  }

  Future<bool?> stopRead() {
    return FlutterUsbDeviceManagePlatform.instance.rfidStopRead();
  }
}
