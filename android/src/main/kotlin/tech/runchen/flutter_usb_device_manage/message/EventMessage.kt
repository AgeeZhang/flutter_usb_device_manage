package tech.runchen.flutter_usb_device_manage.message

import tech.runchen.flutter_usb_device_manage.enum.EventType

data class EventMessage(var type: EventType, var message: String)
