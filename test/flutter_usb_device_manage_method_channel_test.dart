import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_usb_device_manage/flutter_usb_device_manage_method_channel.dart';

void main() {
  MethodChannelFlutterUsbDeviceManage platform =
      MethodChannelFlutterUsbDeviceManage();
  const MethodChannel channel = MethodChannel('flutter_usb_device_manage');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('requestPermission', () async {});
}
