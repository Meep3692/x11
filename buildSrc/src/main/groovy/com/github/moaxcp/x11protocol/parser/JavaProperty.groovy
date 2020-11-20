package com.github.moaxcp.x11protocol.parser

import com.squareup.javapoet.*
import javax.lang.model.element.Modifier
import lombok.NonNull
import lombok.Setter

import static com.github.moaxcp.x11protocol.generator.Conventions.convertX11VariableNameToJava
import static java.util.Objects.requireNonNull
/**
 * A JavaProperty represents a property within a JavaType.
 */
abstract class JavaProperty implements JavaUnit {
    final JavaType javaType
    final XUnitField x11Field
    
    boolean readOnly
    boolean localOnly
    boolean readParam
    JavaBitcaseInfo bitcaseInfo

    JavaProperty(Map map) {
        javaType = requireNonNull(map.javaType, 'javaType must not be null')
        x11Field = requireNonNull(map.x11Field, 'field must not be null')
        readOnly = map.readOnly
        localOnly = map.localOnly
        if(map.x11Field.bitcaseInfo) {
            bitcaseInfo = new JavaBitcaseInfo(map.x11Field.result, map.x11Field.bitcaseInfo)
        }
    }
    
    JavaProperty(JavaType javaType, XUnitField field) {
        this.javaType = requireNonNull(javaType, 'javaType must not be null')
        this.x11Field = requireNonNull(field, 'field must not be null')
        this.localOnly = field.localOnly
        if(field.bitcaseInfo) {
            this.bitcaseInfo = new JavaBitcaseInfo(field.result, field.bitcaseInfo)
        }
    }
    
    XUnitField getXUnit() {
        return x11Field
    }
    
    String getName() {
        return convertX11VariableNameToJava(x11Field.name)
    }
    
    String getX11Type() {
        return x11Field.resolvedType.name
    }
    
    abstract TypeName getTypeName()
    
    abstract boolean isNonNull()

    FieldSpec getMember() {
        if(localOnly) {
            return null
        }
        FieldSpec.Builder builder = FieldSpec.builder(typeName, name)
            .addModifiers(Modifier.PRIVATE)
        if(nonNull) {
            builder.addAnnotation(NonNull)
        }
        if(readOnly) {
            builder.addAnnotation(
                AnnotationSpec.builder(Setter)
                    .addMember('value', CodeBlock.of('$T.PRIVATE', ClassName.get('lombok', 'AccessLevel')))
                    .build())
        }
        return builder.build()
    }

    String getSetterName() {
        if(localOnly) {
            return null
        }
        return "set${name.capitalize()}"
    }

    String getGetterName() {
        if(localOnly) {
            return null
        }
        return "get${name.capitalize()}"
    }

    List<MethodSpec> getMethods() {
        return []
    }
    
    List<MethodSpec> getBuilderMethods(ClassName thisClass) {
        return []
    }

    CodeBlock declareAndInitializeTo(String readCall) {
        return CodeBlock.builder().addStatement('$T $L = $L', typeName, name, readCall).build()
    }

    CodeBlock declareAndInitializeTo(CodeBlock readCall) {
        return CodeBlock.builder().addStatement('$T $L = $L', typeName, name, readCall).build()
    }
}