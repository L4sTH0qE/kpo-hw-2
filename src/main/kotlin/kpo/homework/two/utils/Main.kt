package kpo.homework.two.utils

import kotlin.system.exitProcess

fun main(args: Array<String>) {
    // Запуск окна авторизации и привязка к обработчику базы данных.
    try {
        AuthenticationConsole.startAuthenticationLoop()
    } // При ошибке во время работы программы.
    catch (ex: Exception) {
        println("Error: An unexpected error occurred while the program was running! Exiting the program...")
        exitProcess(0)
    }
}
