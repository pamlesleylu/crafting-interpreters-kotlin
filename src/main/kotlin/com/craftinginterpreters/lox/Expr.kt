package com.craftinginterpreters.lox

abstract class Expr {
    abstract fun <R> accept(visitor: Visitor<R>): R
}

interface Visitor<R> {
    fun visitBinaryExpr(expr: Binary): R
    fun visitGroupingExpr(expr: Grouping): R
    fun visitLiteralExpr(expr: Literal): R
    fun visitUnaryExpr(expr: Unary): R
}

class Binary(val left: Expr, val operator: Token, val right: Expr) : Expr() {
    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitBinaryExpr(this)
}

class Grouping(val expression: Expr) : Expr() {
    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitGroupingExpr(this)
}

class Literal(val value: Any) : Expr() {
    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitLiteralExpr(this)
}

class Unary(val operator: Token, val right: Expr) : Expr() {
    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitUnaryExpr(this)
}
