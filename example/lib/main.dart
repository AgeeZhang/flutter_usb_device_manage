import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter_usb_device_manage/flutter_usb_device_manage.dart';
import 'package:flutter_usb_device_manage/flutter_usb_fingerprint_manage.dart';
import 'package:flutter_usb_device_manage/flutter_usb_rfid_manage.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _message = 'Unknown';
  final _flutterUsbDeviceManagePlugin = FlutterUsbDeviceManage();
  final _flutterUsbFingerprintManagePlugin = FlutterUsbFingerprintManage();
  final _flutterUsbRFIDManagePlugin = FlutterUsbRFIDManage();

  @override
  void initState() {
    super.initState();
    initListenMessage();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initListenMessage() async {
    _flutterUsbDeviceManagePlugin.listenerEvent(
        (message) => {
              setState(() {
                _message = message;
              })
            },
        // ignore: avoid_print
        (error) => {print("$error")});
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('串口设备调试程序'),
        ),
        body: Column(
          children: [
            TextField(
                style: const TextStyle(color: Color(0xFFA7ABBB), fontSize: 15),
                controller: TextEditingController.fromValue(
                    TextEditingValue(text: _message)),
                decoration: InputDecoration(
                  counterText: '',
                  hintMaxLines: 10,
                  filled: true,
                  hintStyle: const TextStyle(
                      color: Color.fromARGB(255, 5, 5, 6), fontSize: 15),
                  hintText: '消息内容',
                  contentPadding:
                      const EdgeInsets.symmetric(horizontal: 15, vertical: 10),
                  enabledBorder: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(6),
                      borderSide: BorderSide.none),
                  focusedBorder: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(6),
                      borderSide: BorderSide.none),
                )),
            Row(
              crossAxisAlignment: CrossAxisAlignment.center,
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: [
                MaterialButton(
                    color: Colors.blue,
                    textColor: Colors.white,
                    child: const Text('USB设备授权'),
                    onPressed: () {
                      _flutterUsbDeviceManagePlugin.requestPermission();
                    }),
              ],
            ),
            Row(
              crossAxisAlignment: CrossAxisAlignment.center,
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: [
                MaterialButton(
                    color: Colors.blue,
                    textColor: Colors.white,
                    child: const Text('打开指纹模块'),
                    onPressed: () {
                      _flutterUsbFingerprintManagePlugin.openDevice();
                    }),
                MaterialButton(
                    color: Colors.blue,
                    textColor: Colors.white,
                    child: const Text('登记指纹'),
                    onPressed: () {
                      _flutterUsbFingerprintManagePlugin.registration(2);
                    }),
                MaterialButton(
                    color: Colors.blue,
                    textColor: Colors.white,
                    child: const Text('采集指纹'),
                    onPressed: () {
                      _flutterUsbFingerprintManagePlugin.collecting();
                    }),
                MaterialButton(
                    color: Colors.blue,
                    textColor: Colors.white,
                    child: const Text('关闭指纹模块'),
                    onPressed: () {
                      _flutterUsbFingerprintManagePlugin.closeDevice();
                    }),
              ],
            ),
            Row(
              crossAxisAlignment: CrossAxisAlignment.center,
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: [
                MaterialButton(
                    color: Colors.blue,
                    textColor: Colors.white,
                    child: const Text('打开RFID模块通讯'),
                    onPressed: () {
                      _flutterUsbRFIDManagePlugin.openUsbSerial();
                    }),
                MaterialButton(
                    color: Colors.blue,
                    textColor: Colors.white,
                    child: const Text('开始读取'),
                    onPressed: () {
                      _flutterUsbRFIDManagePlugin.startRead(1, 500000);
                    }),
                MaterialButton(
                    color: Colors.blue,
                    textColor: Colors.white,
                    child: const Text('停止读取'),
                    onPressed: () {
                      _flutterUsbRFIDManagePlugin.stopRead();
                    }),
                MaterialButton(
                    color: Colors.blue,
                    textColor: Colors.white,
                    child: const Text('关闭RFID模块通信'),
                    onPressed: () {
                      _flutterUsbRFIDManagePlugin.closeUsbSerial();
                    }),
              ],
            )
          ],
        ),
      ),
    );
  }
}
