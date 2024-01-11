import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'flutter_usb_device_manage_platform_interface.dart';

/// An implementation of [FlutterUsbDeviceManagePlatform] that uses method channels.
class MethodChannelFlutterUsbDeviceManage
    extends FlutterUsbDeviceManagePlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('flutter_usb_device_manage');

  static const EventChannel _eventChannel =
      EventChannel("flutter_usb_device_manage.event_channel");

  @override
  void requestPermission() {
    methodChannel.invokeMethod<String>('requestPermission');
  }

  @override
  Future<bool?> fingerprintOpenDevice() async {
    final result =
        await methodChannel.invokeMethod<bool>('Fingerprint.openDevice');
    return result;
  }

  @override
  Future<bool?> fingerprintCloseDevice() async {
    final result =
        await methodChannel.invokeMethod<bool>('Fingerprint.closeDevice');
    return result;
  }

  @override
  void fingerprintRegistration(int enrolCount) {
    methodChannel
        .invokeMethod('Fingerprint.registration', {"enrolCount": enrolCount});
  }

  @override
  void fingerprintCollecting() {
    methodChannel.invokeMethod('Fingerprint.collecting');
  }

  @override
  Future<bool?> rfidOpenUsbSerial() async {
    final result = await methodChannel.invokeMethod<bool>('RFID.openUsbSerial');
    return result;
  }

  @override
  Future<bool?> rfidCloseUsbSerial() async {
    final result =
        await methodChannel.invokeMethod<bool>('RFID.closeUsbSerial');
    return result;
  }

  @override
  Future<bool?> rfidStartRead(int mode, int timeout) async {
    final result = await methodChannel.invokeMethod<bool>(
        'RFID.startRead', {"mode": mode, "timeout": timeout});
    return result;
  }

  @override
  Future<bool?> rfidStopRead() async {
    final result = await methodChannel.invokeMethod<bool>('RFID.stopRead');
    return result;
  }

  @override
  void listenerEvent(onEvent, onError) {
    _eventChannel.receiveBroadcastStream().listen(onEvent, onError: onError);
  }
}
