# flutter_usb_device_manage

主要用于项目中USB设备的集中授权及硬件功能实现，目前包含了USB指纹模块和RFID通讯模块

## USB指纹模块
USB指纹模块主要功能是指纹的注册登记上报和采集匹配模版上报
### openDevice（打开设备）
打开指纹模块的通讯端口
### closeDevice（关闭设备）
关闭指纹模块的通讯端口
### registration（指纹登记）
通讯端口打开后调用该方法进行指纹的登记，包含登记次数参数（enrolCount），登记成功后会通过异步消息返回指纹特征的Base64字符串
### collecting（指纹采集）
通讯端口打开后调用该方法进行指纹采集，采集成功后会通过异步消息返回指纹特征的Base64字符串，可用于服务端比对
## RFID通讯模块

### openAndroidUsbSerial（打开USB串口）
打开RFID模块的USB转串口通讯
### closeAndroidUsbSerial（关闭USB串口）
关闭RFID模块的USB转串口通讯
### startRead（开始读取）
打开通讯后可通过调用该方法进行RFID标签的读取上报，参数包含模式（mode）、超时时间（timeout），标签上报通过异步消息上报
### stopRead（停止读取）
打开通讯后可通过调用该方法停止RFID标签的读取上报