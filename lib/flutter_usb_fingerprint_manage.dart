import 'flutter_usb_device_manage_platform_interface.dart';

class FlutterUsbFingerprintManage {
  Future<bool?> openDevice() {
    return FlutterUsbDeviceManagePlatform.instance.fingerprintOpenDevice();
  }

  Future<bool?> closeDevice() {
    return FlutterUsbDeviceManagePlatform.instance.fingerprintCloseDevice();
  }

  void registration(int enrolCount) {
    return FlutterUsbDeviceManagePlatform.instance
        .fingerprintRegistration(enrolCount);
  }

  void collecting() {
    return FlutterUsbDeviceManagePlatform.instance.fingerprintCollecting();
  }
}
