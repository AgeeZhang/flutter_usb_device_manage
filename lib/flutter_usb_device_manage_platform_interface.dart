import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'flutter_usb_device_manage_method_channel.dart';

abstract class FlutterUsbDeviceManagePlatform extends PlatformInterface {
  /// Constructs a FlutterUsbDeviceManagePlatform.
  FlutterUsbDeviceManagePlatform() : super(token: _token);

  static final Object _token = Object();

  static FlutterUsbDeviceManagePlatform _instance =
      MethodChannelFlutterUsbDeviceManage();

  /// The default instance of [FlutterUsbDeviceManagePlatform] to use.
  ///
  /// Defaults to [MethodChannelFlutterUsbDeviceManage].
  static FlutterUsbDeviceManagePlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [FlutterUsbDeviceManagePlatform] when
  /// they register themselves.
  static set instance(FlutterUsbDeviceManagePlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  void requestPermission() {
    throw UnimplementedError('requestPermission() has not been implemented.');
  }

  Future<bool?> fingerprintOpenDevice() {
    throw UnimplementedError(
        'fingerprintOpenDevice() has not been implemented.');
  }

  Future<bool?> fingerprintCloseDevice() {
    throw UnimplementedError(
        'fingerprintCloseDevice() has not been implemented.');
  }

  void fingerprintRegistration(int enrolCount) {
    throw UnimplementedError(
        'fingerprintRegistration(enrolCount) has not been implemented.');
  }

  void fingerprintCollecting() {
    throw UnimplementedError(
        'fingerprintCollecting() has not been implemented.');
  }

  Future<bool?> rfidOpenUsbSerial() {
    throw UnimplementedError('rfidOpenUsbSerial() has not been implemented.');
  }

  Future<bool?> rfidCloseUsbSerial() {
    throw UnimplementedError('frfidCloseUsbSerial() has not been implemented.');
  }

  Future<bool?> rfidStartRead(int mode, int timeout) {
    throw UnimplementedError(
        'rfidStartRead(mode, timeout) has not been implemented.');
  }

  Future<bool?> rfidStopRead() {
    throw UnimplementedError('frfidStopRead() has not been implemented.');
  }

  void listenerEvent(onEvent, onError) {
    throw UnimplementedError('listenerEvent() has not been implemented.');
  }
}
