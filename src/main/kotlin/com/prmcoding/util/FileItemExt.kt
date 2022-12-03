package com.prmcoding.util

import io.ktor.http.content.*
import java.io.File
import java.util.*

fun PartData.FileItem.save(path: String): String {
    val fileBytes = streamProvider().readBytes()
    val fileExtension = originalFileName?.takeLastWhile { it != '.' }
    val folder = File(path)
    val fileName = UUID.randomUUID().toString() + "." + fileExtension
    folder.mkdir()
    File("$path$fileName").writeBytes(fileBytes)
    return fileName
}