package com.github.moaxcp.x11protocol.parser

import com.squareup.javapoet.ArrayTypeName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.TypeName

class JavaRequiredStartAlign implements JavaUnit {
    JavaType javaType
    XUnit xUnit
    int align
    boolean readParam

    @Override
    XUnit getXUnit() { //todo why is this not generated by groovy
        return xUnit
    }

    @Override
    TypeName getTypeName() {
        return ArrayTypeName.of(byte.class)
    }

    @Override
    CodeBlock getReadCode() {
        return CodeBlock.of('//todo align')
    }

    @Override
    CodeBlock getDeclareAndReadCode() {
        return CodeBlock.of('//todo align')
    }

    @Override
    CodeBlock getWriteCode() {
        return CodeBlock.of('//todo align')
    }

    @Override
    CodeBlock getSizeExpression() {
        return CodeBlock.of('$L', 0)
    }

    @Override
    Optional<Integer> getFixedSize() {
        throw new UnsupportedOperationException('not supported')
    }
}
