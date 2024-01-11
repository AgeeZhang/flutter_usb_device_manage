import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_usb_device_manage/flutter_usb_device_manage.dart';
import 'package:flutter_usb_device_manage/flutter_usb_device_manage_platform_interface.dart';
import 'package:flutter_usb_device_manage/flutter_usb_device_manage_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockFlutterUsbDeviceManagePlatform
    with MockPlatformInterfaceMixin
    implements FlutterUsbDeviceManagePlatform {
  @override
  Future<String?> requestPermission() => Future.value('42');

  @override
  Future<String?> listenerEvent(onEvent, onError) {
    throw UnimplementedError();
  }

  @override
  Future<bool?> fingerprintCloseDevice() {
    // TODO: implement fingerprintCloseDevice
    throw UnimplementedError();
  }

  @override
  void fingerprintCollecting() {
    // TODO: implement fingerprintCollecting
  }

  @override
  Future<bool?> fingerprintOpenDevice() {
    // TODO: implement fingerprintOpenDevice
    throw UnimplementedError();
  }

  @override
  void fingerprintRegistration(int enrolCount) {
    // TODO: implement fingerprintRegistration
  }

  @override
  Future<bool?> rfidCloseUsbSerial() {
    // TODO: implement rfidCloseUsbSerial
    throw UnimplementedError();
  }

  @override
  Future<bool?> rfidOpenUsbSerial() {
    // TODO: implement rfidOpenUsbSerial
    throw UnimplementedError();
  }

  @override
  Future<bool?> rfidStartRead(int mode, int timeout) {
    // TODO: implement rfidStartRead
    throw UnimplementedError();
  }

  @override
  Future<bool?> rfidStopRead() {
    // TODO: implement rfidStopRead
    throw UnimplementedError();
  }
}

void main() {
  final FlutterUsbDeviceManagePlatform initialPlatform =
      FlutterUsbDeviceManagePlatform.instance;

  test('$MethodChannelFlutterUsbDeviceManage is the default instance', () {
    expect(
        initialPlatform, isInstanceOf<MethodChannelFlutterUsbDeviceManage>());
  });

  test('requestPermission', () async {
    FlutterUsbDeviceManage flutterUsbDeviceManagePlugin =
        FlutterUsbDeviceManage();
    MockFlutterUsbDeviceManagePlatform fakePlatform =
        MockFlutterUsbDeviceManagePlatform();
    FlutterUsbDeviceManagePlatform.instance = fakePlatform;
  });
}
