package com.github.moaxcp.x11protocol.parser

import com.github.moaxcp.x11protocol.XmlSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec

import static com.github.moaxcp.x11protocol.parser.JavaStruct.javaStruct

class JavaStructSpec extends XmlSpec {
    def 'FormatStruct TypeSpec'() {
        given:
        JavaStruct struct = new JavaStruct(
            basePackage: result.basePackage,
            simpleName:'FormatStruct',
            className: ClassName.get('com.github.moaxcp.x11client.protocol.xproto', 'FormatStruct'),
            protocol:[
                new JavaPrimativeProperty(
                    name: 'depth',
                    x11Primative: 'CARD8',
                    memberTypeName: TypeName.BYTE
                ),
                new JavaPrimativeProperty(
                    name: 'bitsPerPixel',
                    x11Primative: 'CARD8',
                    memberTypeName: TypeName.BYTE
                ),
                new JavaPrimativeProperty(
                    name: 'scanlinePad',
                    x11Primative: 'CARD8',
                    memberTypeName: TypeName.BYTE
                ),
                new JavaPad(bytes: 5)
            ]
        )

        when:
        TypeSpec spec = struct.typeSpec

        then:
        spec.toString() == '''\
            @lombok.Data
            class FormatStruct {
              private byte depth;
            
              private byte bitsPerPixel;
            
              private byte scanlinePad;
            
              public static com.github.moaxcp.x11client.protocol.xproto.FormatStruct readFormatStruct(
                  com.github.moaxcp.x11client.protocol.X11Input in) throws java.io.IOException {
                byte depth = in.readCard8();
                byte bitsPerPixel = in.readCard8();
                byte scanlinePad = in.readCard8();
                in.readPad(5);
                com.github.moaxcp.x11client.protocol.xproto.FormatStruct struct = new com.github.moaxcp.x11client.protocol.xproto.FormatStruct();
                struct.setDepth(depth);
                struct.setBitsPerPixel(bitsPerPixel);
                struct.setScanlinePad(scanlinePad);
                return struct;
              }
            
              public void writeFormatStruct(com.github.moaxcp.x11client.protocol.X11Output out) throws
                  java.io.IOException {
                out.writeCard8(depth);
                out.writeCard8(bitsPerPixel);
                out.writeCard8(scanlinePad);
                out.writePad(5);
              }
            }
        '''.stripIndent()
    }

    def 'ScreenStruct TypeSpec'() {
        given:
        JavaStruct struct = new JavaStruct(
            basePackage: result.basePackage,
            simpleName: 'ScreenStruct',
            className: ClassName.get('com.github.moaxcp.x11client.protocol.xproto', 'ScreenStruct'),
            protocol: [
                new JavaPrimativeProperty(
                    name: 'root',
                    x11Primative: 'CARD32',
                    memberTypeName: TypeName.INT
                ),
                new JavaPrimativeProperty(
                    name: 'defaultColormap',
                    x11Primative: 'CARD32',
                    memberTypeName: TypeName.INT
                ),
                new JavaPrimativeProperty(
                    name: 'currentInputMasks',
                    x11Primative: 'CARD32',
                    memberTypeName: TypeName.INT,
                    maskTypeName: ClassName.get('com.github.moaxcp.x11client.protocol.xproto', 'EventMaskEnum')
                ),
                new JavaEnumProperty(
                    name: 'backingStores',
                    x11Primative: 'BYTE',
                    ioTypeName: TypeName.BYTE,
                    memberTypeName: ClassName.get('com.github.moaxcp.x11client.protocol.xproto', 'BackingStoreEnum')
                )
            ]
        )

        when:
        TypeSpec spec = struct.typeSpec

        then:
        spec.toString() == '''\
            @lombok.Data
            class ScreenStruct {
              private int root;
            
              private int defaultColormap;
            
              private int currentInputMasks;
            
              private com.github.moaxcp.x11client.protocol.xproto.BackingStoreEnum backingStores;
            
              public static com.github.moaxcp.x11client.protocol.xproto.ScreenStruct readScreenStruct(
                  com.github.moaxcp.x11client.protocol.X11Input in) throws java.io.IOException {
                int root = in.readCard32();
                int defaultColormap = in.readCard32();
                int currentInputMasks = in.readCard32();
                com.github.moaxcp.x11client.protocol.xproto.BackingStoreEnum backingStores = com.github.moaxcp.x11client.protocol.xproto.BackingStoreEnum.getByCode(in.readByte());
                com.github.moaxcp.x11client.protocol.xproto.ScreenStruct struct = new com.github.moaxcp.x11client.protocol.xproto.ScreenStruct();
                struct.setRoot(root);
                struct.setDefaultColormap(defaultColormap);
                struct.setCurrentInputMasks(currentInputMasks);
                struct.setBackingStores(backingStores);
                return struct;
              }
            
              public void writeScreenStruct(com.github.moaxcp.x11client.protocol.X11Output out) throws
                  java.io.IOException {
                out.writeCard32(root);
                out.writeCard32(defaultColormap);
                out.writeCard32(currentInputMasks);
                out.writeByte((byte) backingStores.getValue()));
              }
            
              public void currentInputMasksEnable(
                  com.github.moaxcp.x11client.protocol.xproto.EventMaskEnum mask) {
                currentInputMasks = (int) mask.enableFor(currentInputMasks);
              }
            
              public void currentInputMasksDisable(
                  com.github.moaxcp.x11client.protocol.xproto.EventMaskEnum mask) {
                currentInputMasks = (int) mask.disableFor(currentInputMasks);
              }
            }
        '''.stripIndent()
    }

    def 'paramref with DeviceTimeCoord'() {
        given:
        xmlBuilder.xcb(header:'xinput') {
            typedef(oldname:'CARD32', newname:'TIMESTAMP')
            struct(name:'DeviceTimeCoord') {
                field(type:'TIMESTAMP', name:'time')
                list(type:'INT32', name:'axisvalues') {
                    paramref(type:'CARD8', 'num_axes')
                }
            }
        }
        addChildNodes()

        when:
        XTypeStruct struct = result.resolveXType('DeviceTimeCoord')
        JavaStruct javaStruct = javaStruct(struct)

        then:
        javaStruct.typeSpec.toString() == '''\
            @lombok.Data
            class DeviceTimeCoordStruct {
              private int time;
            
              private int[] axisvalues;
            
              public static com.github.moaxcp.x11client.protocol.xproto.DeviceTimeCoordStruct readDeviceTimeCoordStruct(
                  com.github.moaxcp.x11client.protocol.X11Input in, byte numAxes) throws java.io.IOException {
                int time = in.readCard32();
                int[] axisvalues = in.readInt32(numAxes);
                com.github.moaxcp.x11client.protocol.xproto.DeviceTimeCoordStruct struct = new com.github.moaxcp.x11client.protocol.xproto.DeviceTimeCoordStruct();
                struct.setTime(time);
                struct.setAxisvalues(axisvalues);
                return struct;
              }
            
              public void writeDeviceTimeCoordStruct(com.github.moaxcp.x11client.protocol.X11Output out) throws
                  java.io.IOException {
                out.writeCard32(time);
                out.writeInt32(axisvalues);
              }
            }
        '''.stripIndent()

    }
}