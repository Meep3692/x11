package com.github.moaxcp.x11protocol.parser

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString
@EqualsAndHashCode
class XPad implements XUnit {
    private int bytes

    XPad(int bytes) {
        this.bytes = bytes
    }

    @Override
    String getReadCode() {
        return "in.readPad($bytes);"
    }

    @Override
    String getWriteCode() {
        return "out.writePad($bytes);"
    }
}
