package com.github.teddyyeung.checkunusedassetplugin.toolWindow

import com.github.teddyyeung.checkunusedassetplugin.services.AssetInspectorLogic
import com.intellij.openapi.project.Project
import javax.swing.JButton


import java.awt.BorderLayout
import javax.swing.*

class AssetInspectorPanel(val project: Project) : JPanel(BorderLayout()) {

    private val searchDirsField = JTextField("features,lib")
    private val baseDirField = JTextField("assets/images")
    private val runButton = JButton("검사 시작")
    private val resultArea = JTextArea()

    init {
        // 상단 입력부
        val inputPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            add(JLabel("검색할 소스 폴더 (쉼표 구분):"))
            add(searchDirsField)
            add(JLabel("이미지 asset base 폴더:"))
            add(baseDirField)
            add(runButton)
        }
        add(inputPanel, BorderLayout.NORTH)
        add(JScrollPane(resultArea), BorderLayout.CENTER)

        runButton.addActionListener {
            inspectAssets()
        }
    }

    private fun inspectAssets() {
        val searchDirs = searchDirsField.text.split(",").map { it.trim() }
        val baseDir = baseDirField.text.trim()
        // 아래에 실제 검사 로직 호출
        val result = AssetInspectorLogic.inspect(searchDirs, baseDir)
        resultArea.text = result.toString()
    }
}