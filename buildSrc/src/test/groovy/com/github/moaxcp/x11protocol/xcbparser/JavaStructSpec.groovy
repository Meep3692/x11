package com.github.moaxcp.x11protocol.xcbparser

import com.github.moaxcp.x11protocol.XmlSpec
import com.squareup.javapoet.TypeSpec

import static com.github.moaxcp.x11protocol.xcbparser.JavaStruct.javaStruct

class JavaStructSpec extends XmlSpec {
    def 'FormatStruct TypeSpec'() {
        given:
        xmlBuilder.xcb(header:'xproto') {
            struct(name:'Format') {
                field(type: 'CARD8', name: 'depth')
                field(type: 'CARD8', name: 'bitsPerPixel')
                field(type: 'CARD8', name: 'scanlinePad')
                pad(bytes:'5')
            }
        }
        addChildNodes()

        when:
        TypeSpec typeSpec = result.resolveXType('Format').javaType.typeSpecs[0]

        then:
        typeSpec.toString() == '''\
            @lombok.Value
            @lombok.Builder
            public class Format implements com.github.moaxcp.x11client.protocol.XStruct {
              private byte depth;
            
              private byte bitsPerPixel;
            
              private byte scanlinePad;
            
              public static com.github.moaxcp.x11client.protocol.xproto.Format readFormat(
                  com.github.moaxcp.x11client.protocol.X11Input in) throws java.io.IOException {
                byte depth = in.readCard8();
                byte bitsPerPixel = in.readCard8();
                byte scanlinePad = in.readCard8();
                in.readPad(5);
                com.github.moaxcp.x11client.protocol.xproto.Format.FormatBuilder javaBuilder = com.github.moaxcp.x11client.protocol.xproto.Format.builder();
                javaBuilder.depth(depth);
                javaBuilder.bitsPerPixel(bitsPerPixel);
                javaBuilder.scanlinePad(scanlinePad);
                return javaBuilder.build();
              }
            
              @java.lang.Override
              public void write(com.github.moaxcp.x11client.protocol.X11Output out) throws java.io.IOException {
                out.writeCard8(depth);
                out.writeCard8(bitsPerPixel);
                out.writeCard8(scanlinePad);
                out.writePad(5);
              }
            
              @java.lang.Override
              public int getSize() {
                return 8;
              }
              
              public static class FormatBuilder {
                public int getSize() {
                  return 8;
                }
              }
            }
        '''.stripIndent()
    }

    def 'ScreenStruct TypeSpec'() {
        given:
        xmlBuilder.xcb(header:'xproto') {
            'enum'(name:'EventMask') {
                item(name:'NoEvent') {
                    value('0')
                }
            }
            'enum'(name:'BackingStore') {
                item(name:'NotUseful') {
                    value('0')
                }
            }
            struct(name:'SCREEN') {
                field(type:'CARD32', name:'root')
                field(type:'CARD32', name:'default_colormap')
                field(type:'CARD32', mask:'EventMask', name:'current_input_masks')
                field(type:'BYTE', name:'backing_stores', enum:'BackingStore')
            }
        }
        addChildNodes()

        when:
        XTypeStruct struct = result.resolveXType('SCREEN')
        JavaStruct javaStruct = javaStruct(struct)

        then:
        javaStruct.typeSpecs[0].toString() == '''\
            @lombok.Value
            @lombok.Builder
            public class Screen implements com.github.moaxcp.x11client.protocol.XStruct {
              private int root;
            
              private int defaultColormap;
            
              private int currentInputMasks;
            
              private byte backingStores;
            
              public static com.github.moaxcp.x11client.protocol.xproto.Screen readScreen(
                  com.github.moaxcp.x11client.protocol.X11Input in) throws java.io.IOException {
                int root = in.readCard32();
                int defaultColormap = in.readCard32();
                int currentInputMasks = in.readCard32();
                byte backingStores = in.readByte();
                com.github.moaxcp.x11client.protocol.xproto.Screen.ScreenBuilder javaBuilder = com.github.moaxcp.x11client.protocol.xproto.Screen.builder();
                javaBuilder.root(root);
                javaBuilder.defaultColormap(defaultColormap);
                javaBuilder.currentInputMasks(currentInputMasks);
                javaBuilder.backingStores(backingStores);
                return javaBuilder.build();
              }
            
              @java.lang.Override
              public void write(com.github.moaxcp.x11client.protocol.X11Output out) throws java.io.IOException {
                out.writeCard32(root);
                out.writeCard32(defaultColormap);
                out.writeCard32(currentInputMasks);
                out.writeByte(backingStores);
              }
            
              public boolean isCurrentInputMasksEnabled(
                  @lombok.NonNull com.github.moaxcp.x11client.protocol.xproto.EventMask... maskEnums) {
                for(com.github.moaxcp.x11client.protocol.xproto.EventMask m : maskEnums) {
                  if(!m.isEnabled(currentInputMasks)) {
                    return false;
                  }
                }
                return true;
              }
            
              @java.lang.Override
              public int getSize() {
                return 13;
              }
            
              public static class ScreenBuilder {
                public boolean isCurrentInputMasksEnabled(
                    @lombok.NonNull com.github.moaxcp.x11client.protocol.xproto.EventMask... maskEnums) {
                  for(com.github.moaxcp.x11client.protocol.xproto.EventMask m : maskEnums) {
                    if(!m.isEnabled(currentInputMasks)) {
                      return false;
                    }
                  }
                  return true;
                }
            
                public com.github.moaxcp.x11client.protocol.xproto.Screen.ScreenBuilder currentInputMasksEnable(
                    com.github.moaxcp.x11client.protocol.xproto.EventMask... maskEnums) {
                  for(com.github.moaxcp.x11client.protocol.xproto.EventMask m : maskEnums) {
                    currentInputMasks((int) m.enableFor(currentInputMasks));
                  }
                  return this;
                }
            
                public com.github.moaxcp.x11client.protocol.xproto.Screen.ScreenBuilder currentInputMasksDisable(
                    com.github.moaxcp.x11client.protocol.xproto.EventMask... maskEnums) {
                  for(com.github.moaxcp.x11client.protocol.xproto.EventMask m : maskEnums) {
                    currentInputMasks((int) m.disableFor(currentInputMasks));
                  }
                  return this;
                }
            
                public com.github.moaxcp.x11client.protocol.xproto.Screen.ScreenBuilder backingStores(
                    com.github.moaxcp.x11client.protocol.xproto.BackingStore backingStores) {
                  this.backingStores = (byte) backingStores.getValue();
                  return this;
                }
            
                public com.github.moaxcp.x11client.protocol.xproto.Screen.ScreenBuilder backingStores(
                    byte backingStores) {
                  this.backingStores = backingStores;
                  return this;
                }
            
                public int getSize() {
                  return 13;
                }
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
        javaStruct.typeSpecs[0].toString() == '''\
            @lombok.Value
            @lombok.Builder
            public class DeviceTimeCoord implements com.github.moaxcp.x11client.protocol.XStruct {
              private int time;
            
              @lombok.NonNull
              private java.util.List<java.lang.Integer> axisvalues;
            
              public static com.github.moaxcp.x11client.protocol.xproto.DeviceTimeCoord readDeviceTimeCoord(
                  byte numAxes, com.github.moaxcp.x11client.protocol.X11Input in) throws java.io.IOException {
                int time = in.readCard32();
                java.util.List<java.lang.Integer> axisvalues = in.readInt32(numAxes);
                com.github.moaxcp.x11client.protocol.xproto.DeviceTimeCoord.DeviceTimeCoordBuilder javaBuilder = com.github.moaxcp.x11client.protocol.xproto.DeviceTimeCoord.builder();
                javaBuilder.time(time);
                javaBuilder.axisvalues(axisvalues);
                return javaBuilder.build();
              }
            
              @java.lang.Override
              public void write(com.github.moaxcp.x11client.protocol.X11Output out) throws java.io.IOException {
                out.writeCard32(time);
                out.writeInt32(axisvalues);
              }
            
              @java.lang.Override
              public int getSize() {
                return 4 + 4 * axisvalues.size();
              }
              
              public static class DeviceTimeCoordBuilder {
                public int getSize() {
                  return 4 + 4 * axisvalues.size();
                }
              }
            }
        '''.stripIndent()
    }

    def 'fieldref with boolean type'() {
        given:
        xmlBuilder.xcb(header:'xkb') {
            struct(name:'SetKeyType') {
                field(type:'BOOL', name:'preserve')
                field(type:'CARD8', name:'nMapEntries')
                list(type:'INT32', name:'preserve_entries') {
                    op(op:'*') {
                        fieldref("preserve")
                        fieldref("nMapEntries")
                    }
                }
            }
        }
        addChildNodes()

        when:
        XTypeStruct struct = result.resolveXType('SetKeyType')
        JavaStruct javaStruct = javaStruct(struct)

        then:
        javaStruct.typeSpecs[0].toString() == '''\
            @lombok.Value
            @lombok.Builder
            public class SetKeyType implements com.github.moaxcp.x11client.protocol.XStruct {
              private boolean preserve;
            
              private byte nMapEntries;
            
              @lombok.NonNull
              private java.util.List<java.lang.Integer> preserveEntries;
            
              public static com.github.moaxcp.x11client.protocol.xproto.SetKeyType readSetKeyType(
                  com.github.moaxcp.x11client.protocol.X11Input in) throws java.io.IOException {
                boolean preserve = in.readBool();
                byte nMapEntries = in.readCard8();
                java.util.List<java.lang.Integer> preserveEntries = in.readInt32((preserve ? 1 : 0) * Byte.toUnsignedInt(nMapEntries));
                com.github.moaxcp.x11client.protocol.xproto.SetKeyType.SetKeyTypeBuilder javaBuilder = com.github.moaxcp.x11client.protocol.xproto.SetKeyType.builder();
                javaBuilder.preserve(preserve);
                javaBuilder.nMapEntries(nMapEntries);
                javaBuilder.preserveEntries(preserveEntries);
                return javaBuilder.build();
              }
            
              @java.lang.Override
              public void write(com.github.moaxcp.x11client.protocol.X11Output out) throws java.io.IOException {
                out.writeBool(preserve);
                out.writeCard8(nMapEntries);
                out.writeInt32(preserveEntries);
              }
            
              @java.lang.Override
              public int getSize() {
                return 2 + 4 * preserveEntries.size();
              }
              
              public static class SetKeyTypeBuilder {
                public int getSize() {
                  return 2 + 4 * preserveEntries.size();
                }
              }
            }
        '''.stripIndent()

    }

    def 'SetupRequest size with pad align'() {
        given:
        xmlBuilder.xcb(header:'xproto') {
            struct(name: 'SetupRequest') {
                field(type:'CARD8', name:'byte_order')
                pad(bytes:'1')
                field(type:'CARD16', name:'protocol_major_version')
                field(type:'CARD16', name:'protocol_minor_version')
                field(type:'CARD16', name:'authorization_protocol_name_len')
                field(type:'CARD16', name:'authorization_protocol_data_len')
                pad(bytes:'2')
                list(type:'char', name:'authorization_protocol_name') {
                    fieldref('authorization_protocol_name_len')
                }
                pad(align:'4')
                list(type:'char', name:'authorization_protocol_data') {
                    fieldref('authorization_protocol_data_len')
                }
                pad(align:'4')
            }
        }
        addChildNodes()

        when:
        TypeSpec typeSpec = result.resolveXType('SetupRequest').javaType.typeSpecs[0]

        then:
        typeSpec.toString() == '''\
            @lombok.Value
            @lombok.Builder
            public class SetupRequest implements com.github.moaxcp.x11client.protocol.XStruct {
              private byte byteOrder;
            
              private short protocolMajorVersion;
            
              private short protocolMinorVersion;
            
              @lombok.NonNull
              private java.util.List<java.lang.Byte> authorizationProtocolName;
            
              @lombok.NonNull
              private java.util.List<java.lang.Byte> authorizationProtocolData;
            
              public static com.github.moaxcp.x11client.protocol.xproto.SetupRequest readSetupRequest(
                  com.github.moaxcp.x11client.protocol.X11Input in) throws java.io.IOException {
                byte byteOrder = in.readCard8();
                in.readPad(1);
                short protocolMajorVersion = in.readCard16();
                short protocolMinorVersion = in.readCard16();
                short authorizationProtocolNameLen = in.readCard16();
                short authorizationProtocolDataLen = in.readCard16();
                in.readPad(2);
                java.util.List<java.lang.Byte> authorizationProtocolName = in.readChar(Short.toUnsignedInt(authorizationProtocolNameLen));
                in.readPadAlign(Short.toUnsignedInt(authorizationProtocolNameLen));
                java.util.List<java.lang.Byte> authorizationProtocolData = in.readChar(Short.toUnsignedInt(authorizationProtocolDataLen));
                in.readPadAlign(Short.toUnsignedInt(authorizationProtocolDataLen));
                com.github.moaxcp.x11client.protocol.xproto.SetupRequest.SetupRequestBuilder javaBuilder = com.github.moaxcp.x11client.protocol.xproto.SetupRequest.builder();
                javaBuilder.byteOrder(byteOrder);
                javaBuilder.protocolMajorVersion(protocolMajorVersion);
                javaBuilder.protocolMinorVersion(protocolMinorVersion);
                javaBuilder.authorizationProtocolName(authorizationProtocolName);
                javaBuilder.authorizationProtocolData(authorizationProtocolData);
                return javaBuilder.build();
              }
            
              @java.lang.Override
              public void write(com.github.moaxcp.x11client.protocol.X11Output out) throws java.io.IOException {
                out.writeCard8(byteOrder);
                out.writePad(1);
                out.writeCard16(protocolMajorVersion);
                out.writeCard16(protocolMinorVersion);
                short authorizationProtocolNameLen = (short) authorizationProtocolName.size();
                out.writeCard16(authorizationProtocolNameLen);
                short authorizationProtocolDataLen = (short) authorizationProtocolData.size();
                out.writeCard16(authorizationProtocolDataLen);
                out.writePad(2);
                out.writeChar(authorizationProtocolName);
                out.writePadAlign(Short.toUnsignedInt(authorizationProtocolNameLen));
                out.writeChar(authorizationProtocolData);
                out.writePadAlign(Short.toUnsignedInt(authorizationProtocolDataLen));
              }
            
              @java.lang.Override
              public int getSize() {
                return 12 + 1 * authorizationProtocolName.size() + com.github.moaxcp.x11client.protocol.XObject.getSizeForPadAlign(4, 1 * authorizationProtocolName.size()) + 1 * authorizationProtocolData.size() + com.github.moaxcp.x11client.protocol.XObject.getSizeForPadAlign(4, 1 * authorizationProtocolData.size());
              }
            
              public static class SetupRequestBuilder {
                public int getSize() {
                  return 12 + 1 * authorizationProtocolName.size() + com.github.moaxcp.x11client.protocol.XObject.getSizeForPadAlign(4, 1 * authorizationProtocolName.size()) + 1 * authorizationProtocolData.size() + com.github.moaxcp.x11client.protocol.XObject.getSizeForPadAlign(4, 1 * authorizationProtocolData.size());
                }
              }
            }
        '''.stripIndent()
    }
}
