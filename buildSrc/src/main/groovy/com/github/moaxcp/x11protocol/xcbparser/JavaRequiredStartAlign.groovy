package com.github.moaxcp.x11protocol.xcbparser

import com.squareup.javapoet.ArrayTypeName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.TypeName

class JavaRequiredStartAlign implements JavaUnit {
    JavaType javaType
    XUnit xUnit
    int align
    boolean readParam
    TypeName readTypeName

    @Override
    XUnit getXUnit() { //todo why is this not generated by groovy
        return xUnit
    }

    @Override
    CodeBlock getDeclareCode() {
        return null
    }

    @Override
    CodeBlock getDefaultValue() {
        return null
    }

    @Override
    String getName() {
        return null
    }

    @Override
    TypeName getTypeName() {
        return ArrayTypeName.of(byte.class)
    }

    @Override
    CodeBlock getReadCode() {
        return CodeBlock.of('//todo align\n')
    }

    @Override
    void addBuilderCode(CodeBlock.Builder code) {

    }

    @Override
    CodeBlock getDeclareAndReadCode() {
        return CodeBlock.of('//todo align\n')
    }

    @Override
    void addWriteCode(CodeBlock.Builder code) {
        code.add('//todo align\n')
    }

    @Override
    boolean isReadProtocol() {
        return !readParam
    }

    @Override
    CodeBlock getSizeExpression() {
        return CodeBlock.of('$L', 0)
    }

    @Override
    Optional<Integer> getFixedSize() {
        return Optional.empty()
    }
}
