package br.com.ivan.springsecurityauth.mapper

interface Mapper<T, U> {
    fun map(t: T): U
}
