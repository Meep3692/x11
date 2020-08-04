package com.github.moaxcp.x11protocol.parser

import com.github.moaxcp.x11protocol.generator.Conventions
import com.github.moaxcp.x11protocol.parser.expression.ExpressionFactory
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import groovy.util.slurpersupport.Node
import javax.lang.model.element.Modifier

class XEnum extends XType {
    List<XEnumItem> items = []

    String getJavaClassName() {
        Conventions."get${type.capitalize()}JavaName"(name)
    }

    @Override
    ClassName getJavaType() {
        (ClassName) super.getJavaType()
    }

    TypeSpec getTypeSpec() {
        return TypeSpec.enumBuilder(javaType)
            .addModifiers(Modifier.PUBLIC)
            .addSuperinterface(ClassName.get(result.basePackage, 'IntValue'))
            .addField(FieldSpec.builder(TypeName.INT, 'value', Modifier.PRIVATE).build())
            .addMethod(MethodSpec.constructorBuilder()
                .addParameter(TypeName.INT, 'value')
                .addStatement("this.\$N = \$N", 'value', 'value')
                .build())
            .addMethod(MethodSpec.methodBuilder('getValue')
                .addAnnotation(Override)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return value")
                .returns(TypeName.INT)
                .build())
            .with(true) {TypeSpec.Builder builder ->
                items.each {
                    builder.addEnumConstant(it.name, TypeSpec.anonymousClassBuilder('$L', it.value.expression).build())
                }
                builder
            }
            .build()
    }
    
    static XEnum getXEnum(XResult result, Node node) {
        XEnum xEnum = new XEnum(result:result, name:node.attributes().get('name'), type:node.name())
        node.childNodes().each { Node it ->
            if(it.name() != 'item') {
                throw new IllegalArgumentException("could not parse $it")
            }
            XEnumItem item = new XEnumItem()
            item.name = (String) it.attributes().get('name')
            item.value = ExpressionFactory.getExpression((Node) it.childNodes().next())
            xEnum.items.add(item)
        }
        return xEnum
    }
}
