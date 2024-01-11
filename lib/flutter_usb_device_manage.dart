import 'flutter_usb_device_manage_platform_interface.dart';

class FlutterUsbDeviceManage {
  void requestPermission() {
    return FlutterUsbDeviceManagePlatform.instance.requestPermission();
  }

  void listenerEvent(onEvent, onError) {
    return FlutterUsbDeviceManagePlatform.instance
        .listenerEvent(onEvent, onError);
  }
}
