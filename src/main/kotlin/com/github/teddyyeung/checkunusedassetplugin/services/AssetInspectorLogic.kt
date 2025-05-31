package com.github.teddyyeung.checkunusedassetplugin.services

import java.io.File

object AssetInspectorLogic {
    private val imageExtensions = listOf("png", "jpg", "jpeg", "gif", "bmp", "svg", "webp")

    fun inspect(searchDirs: List<String>, baseDir: String): List<Triple<String, String, String>> {
        // 1. 이미지 폴더 목록 가져오기 (디렉토리만)
        val imageDirs: List<File> = File(baseDir)
            .listFiles { file: File -> file.isDirectory }
            ?.toList()
            ?: emptyList()

        // 2. Dart 코드에서 참조된 변수명 추출
        val assetRefs: Set<String> = extractAssetRefs(searchDirs)

        // 3. 미참조 asset 찾기
        val unreferenced = mutableListOf<Triple<String, String, String>>()
        for (dir in imageDirs) {
            val files = dir.listFiles { file: File ->
                file.isFile && imageExtensions.contains(file.extension.lowercase())
            } ?: continue

            for (file in files) {
                val parent = dir.name
                val nameWoExt = file.nameWithoutExtension
                val camelCaseName = toCamelCase(nameWoExt)
                val varName = "Assets.images.$parent.$camelCaseName".lowercase()
                if (!assetRefs.contains(varName)) {
                    unreferenced.add(Triple(varName, file.extension, file.absolutePath))
                }
            }
        }
        return unreferenced
    }

    private fun extractAssetRefs(searchDirs: List<String>): Set<String> {
        val pattern = Regex("""Assets\.images\.[a-zA-Z0-9_]+\.[a-zA-Z0-9_]+""")
        val refs = mutableSetOf<String>()
        for (dir in searchDirs) {
            File(dir).walkTopDown().filter { it.extension == "dart" }.forEach { file ->
                file.forEachLine { line ->
                    pattern.findAll(line).forEach { matchResult ->
                        refs.add(matchResult.value.lowercase())
                    }
                }
            }
        }
        return refs
    }

    private fun toCamelCase(str: String): String {
        return str.split('-', '_', ' ').mapIndexed { idx, part ->
            if (idx == 0) part.lowercase()
            else part.replaceFirstChar { it.uppercase() }
        }.joinToString("")
    }
}