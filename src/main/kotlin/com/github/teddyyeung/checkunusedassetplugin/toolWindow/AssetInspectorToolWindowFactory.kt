package com.github.teddyyeung.checkunusedassetplugin.toolWindow

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory


class AssetInspectorToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val assetInspectorPanel = AssetInspectorPanel(project)
        val contentFactory = ContentFactory.getInstance()
        val content = contentFactory.createContent(assetInspectorPanel, "", false)
        toolWindow.contentManager.addContent(content)
    }
}
