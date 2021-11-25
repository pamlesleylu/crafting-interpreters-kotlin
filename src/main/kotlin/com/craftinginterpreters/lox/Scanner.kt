package com.craftinginterpreters.lox

import com.craftinginterpreters.lox.TokenType.*

class Scanner(private val source: String) {
    private val tokens = mutableListOf<Token>()

    private var start = 0;
    private var current = 0;
    private var line = 1;

    fun scanTokens(): List<Token> {
        while (!isAtEnd()) {
            start = current
            scanToken()
        }

        tokens.add(Token(EOF, "", null, line))
        return tokens
    }

    private fun scanToken() {
        val token: Pair<TokenType, Any?>? = when (val currentChar = advance()) {
            '(' -> Pair(LEFT_PAREN, null)
            ')' -> Pair(RIGHT_PAREN, null)
            '{' -> Pair(LEFT_BRACE, null)
            '}' -> Pair(RIGHT_BRACE, null)
            ',' -> Pair(COMMA, null)
            '.' -> Pair(DOT, null)
            '-' -> Pair(MINUS, null)
            '+' -> Pair(PLUS, null)
            ';' -> Pair(SEMICOLON, null)
            '*' -> Pair(STAR, null)
            '!' -> when (match('=')) {
                true -> Pair(BANG_EQUAL, null)
                else -> Pair(BANG, null)
            }
            '=' -> when (match('=')) {
                true -> Pair(EQUAL_EQUAL, null)
                else -> Pair(EQUAL, null)
            }
            '<' -> when (match('=')) {
                true -> Pair(LESS_EQUAL, null)
                else -> Pair(LESS, null)
            }
            '>' -> when (match('=')) {
                true -> Pair(GREATER_EQUAL, null)
                else -> Pair(GREATER, null)
            }
            '/' -> when (match('/')) {
                true -> {
                    comment()
                    null
                }
                false -> Pair(SLASH, null)
            }
            ' ', '\r', '\t' -> {
                null
            }
            '\n' -> {
                line = line.inc()
                null
            }
            '"' -> {
                string()
            }
            else -> {
                if (isDigit(currentChar)) {
                    number()
                } else if (isAlpha(currentChar)) {
                    identifier()
                } else {
                    Klox.error(line, "Unexpected character.")
                    null
                }
            }
        }

        token?.let {
            val (tokenType, value) = it;
            addToken(tokenType, value)
        }
    }

    private fun identifier(): Pair<TokenType, Any?>? {
        while (isAlhaNumeric(peek()))
            advance();

        val text = source.substring(start, current)

        return Pair(getIdentifierType(text), null)
    }

    private fun getIdentifierType(text: String): TokenType = keywords[text] ?: IDENTIFIER

    private fun isAlhaNumeric(c: Char?): Boolean =
        isAlpha(c) || isDigit(c)

    private fun isAlpha(c: Char?): Boolean =
        (c in 'a'..'z') || (c in 'A'..'Z') || c == '_'

    private fun comment() {
        // TODO should i check for end of line also?
        while (peek() != null && !isAtEnd()) advance()
    }

    private fun string(): Pair<TokenType, Any>? {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n')
                line = line.inc()
            advance()
        }

        if (isAtEnd()) {
            Klox.error(line, "unterminated string")
            return null
        }

        advance()
        val value = source.substring((start + 1) until current)
        return Pair(STRING, value)
    }

    private fun isDigit(c: Char?): Boolean = c in '0'..'9'

    private fun number(): Pair<TokenType, Any>? {
        while (isDigit(peek())) advance()

        if (peek() == '.' && isDigit(peekNext())) {
            advance()

            while (isDigit(peek())) advance()
        }

        return Pair(NUMBER, source.substring(start..current).toDouble())
    }

    private fun isAtEnd(): Boolean = current >= source.length;

    private fun advance(): Char {
        val charAt = source[current]
        current = current.inc()
        return charAt
    }

    private fun match(expected: Char): Boolean {
        if (isAtEnd()) return false
        if (source[current] != expected) return false

        current = current.inc()
        return true
    }

    private fun peek(): Char? {
        if (isAtEnd()) return null
        return source[current]
    }

    private fun peekNext(): Char? {
        if (current + 1 >= source.length) return null
        return source[current + 1]
    }

    private fun addToken(type: TokenType, literal: Any? = null) {
        val text = source.substring(start, current)
        tokens.add(Token(type, text, literal, line))
    }

    companion object {
        val keywords = hashMapOf<String, TokenType>();

        init {
            keywords["and"] = AND
            keywords["class"] = CLASS
            keywords["else"] = ELSE
            keywords["false"] = FALSE
            keywords["for"] =  FOR
            keywords["fun"] = FUN
            keywords["if"] = IF
            keywords["nil"] = NIL
            keywords["or"] = OR
            keywords["print"] = PRINT
            keywords["return"] = RETURN
            keywords["super"] = SUPER
            keywords["this"] = THIS
            keywords["true"] =  TRUE
            keywords["var"] = VAR
            keywords["while"] = WHILE
        }
    }
}