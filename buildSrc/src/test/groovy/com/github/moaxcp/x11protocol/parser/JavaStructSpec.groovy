package com.github.moaxcp.x11protocol.parser

import com.github.moaxcp.x11protocol.XmlSpec

import static com.github.moaxcp.x11protocol.parser.JavaStruct.javaStruct

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
        XTypeStruct struct = result.resolveXType('Format')
        JavaStruct javaStruct = javaStruct(struct)

        then:
        javaStruct.typeSpec.toString() == '''\
            @lombok.Value
            @lombok.Builder
            public class FormatStruct implements com.github.moaxcp.x11client.protocol.XStruct {
              private byte depth;
            
              private byte bitsPerPixel;
            
              private byte scanlinePad;
            
              public static com.github.moaxcp.x11client.protocol.xproto.FormatStruct readFormatStruct(
                  com.github.moaxcp.x11client.protocol.X11Input in) throws java.io.IOException {
                byte depth = in.readCard8();
                byte bitsPerPixel = in.readCard8();
                byte scanlinePad = in.readCard8();
                in.readPad(5);
                com.github.moaxcp.x11client.protocol.xproto.FormatStructBuilder javaBuilder = com.github.moaxcp.x11client.protocol.xproto.FormatStructBuilder.builder();
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
                return 1 + 1 + 1 + 5;
              }
              
              public static class FormatStructBuilder {
                @java.lang.Override
                public int getSize() {
                  return 1 + 1 + 1 + 5;
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
        javaStruct.typeSpec.toString() == '''\
            @lombok.Value
            @lombok.Builder
            public class ScreenStruct implements com.github.moaxcp.x11client.protocol.XStruct {
              private int root;
            
              private int defaultColormap;
            
              private int currentInputMasks;
            
              @lombok.NonNull
              private com.github.moaxcp.x11client.protocol.xproto.BackingStoreEnum backingStores;
            
              public static com.github.moaxcp.x11client.protocol.xproto.ScreenStruct readScreenStruct(
                  com.github.moaxcp.x11client.protocol.X11Input in) throws java.io.IOException {
                int root = in.readCard32();
                int defaultColormap = in.readCard32();
                int currentInputMasks = in.readCard32();
                com.github.moaxcp.x11client.protocol.xproto.BackingStoreEnum backingStores = com.github.moaxcp.x11client.protocol.xproto.BackingStoreEnum.getByCode(in.readByte());
                com.github.moaxcp.x11client.protocol.xproto.ScreenStructBuilder javaBuilder = com.github.moaxcp.x11client.protocol.xproto.ScreenStructBuilder.builder();
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
                out.writeByte((byte) backingStores.getValue());
              }
            
              public boolean isCurrentInputMasksEnabled(
                  com.github.moaxcp.x11client.protocol.xproto.EventMaskEnum maskEnum) {
                return maskEnum.isEnabled(currentInputMasks);
              }
            
              @java.lang.Override
              public int getSize() {
                return 4 + 4 + 4 + 1;
              }
            
              public static class ScreenStructBuilder {
                public com.github.moaxcp.x11client.protocol.xproto.ScreenStruct.ScreenStructBuilder currentInputMasksEnable(
                    com.github.moaxcp.x11client.protocol.xproto.EventMaskEnum maskEnum) {
                  currentInputMasks = (int) maskEnum.enableFor(currentInputMasks);
                  return this;
                }
            
                public com.github.moaxcp.x11client.protocol.xproto.ScreenStruct.ScreenStructBuilder currentInputMasksDisable(
                    com.github.moaxcp.x11client.protocol.xproto.EventMaskEnum maskEnum) {
                  currentInputMasks = (int) maskEnum.disableFor(currentInputMasks);
                  return this;
                }
                
                @java.lang.Override
                public int getSize() {
                  return 4 + 4 + 4 + 1;
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
        javaStruct.typeSpec.toString() == '''\
            @lombok.Value
            @lombok.Builder
            public class DeviceTimeCoordStruct implements com.github.moaxcp.x11client.protocol.XStruct {
              private int time;
            
              @lombok.NonNull
              private java.util.List<java.lang.Integer> axisvalues;
            
              public static com.github.moaxcp.x11client.protocol.xproto.DeviceTimeCoordStruct readDeviceTimeCoordStruct(
                  byte numAxes, com.github.moaxcp.x11client.protocol.X11Input in) throws java.io.IOException {
                int time = in.readCard32();
                java.util.List<java.lang.Integer> axisvalues = in.readInt32(numAxes);
                com.github.moaxcp.x11client.protocol.xproto.DeviceTimeCoordStructBuilder javaBuilder = com.github.moaxcp.x11client.protocol.xproto.DeviceTimeCoordStructBuilder.builder();
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
              
              public static class DeviceTimeCoordStructBuilder {
                @java.lang.Override
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
        javaStruct.typeSpec.toString() == '''\
            @lombok.Value
            @lombok.Builder
            public class SetKeyTypeStruct implements com.github.moaxcp.x11client.protocol.XStruct {
              private boolean preserve;
            
              private byte nMapEntries;
            
              @lombok.NonNull
              private java.util.List<java.lang.Integer> preserveEntries;
            
              public static com.github.moaxcp.x11client.protocol.xproto.SetKeyTypeStruct readSetKeyTypeStruct(
                  com.github.moaxcp.x11client.protocol.X11Input in) throws java.io.IOException {
                boolean preserve = in.readBool();
                byte nMapEntries = in.readCard8();
                java.util.List<java.lang.Integer> preserveEntries = in.readInt32((preserve ? 1 : 0) * Byte.toUnsignedInt(nMapEntries));
                com.github.moaxcp.x11client.protocol.xproto.SetKeyTypeStructBuilder javaBuilder = com.github.moaxcp.x11client.protocol.xproto.SetKeyTypeStructBuilder.builder();
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
                return 1 + 1 + 4 * preserveEntries.size();
              }
              
              public static class SetKeyTypeStructBuilder {
                @java.lang.Override
                public int getSize() {
                  return 1 + 1 + 4 * preserveEntries.size();
                }
              }
            }
        '''.stripIndent()

    }
}
