package com.eyram.svg_preview

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.progress.ProgressManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier
import org.jetbrains.kotlin.idea.highlighter.markers.InheritanceMergeableLineMarkerInfo
import org.jetbrains.kotlin.idea.highlighter.markers.SuperDeclarationMarkerNavigationHandler
import org.jetbrains.kotlin.idea.highlighter.markers.SuperDeclarationMarkerTooltip
import org.jetbrains.kotlin.psi.KtNamedDeclaration
import javax.swing.Icon

class TestLineMarker : LineMarkerProviderDescriptor() {
    override fun getName() = "Test LineMarker"

    override fun getIcon() = AllIcons.Ide.Like

    override fun getLineMarkerInfo(element: PsiElement) = null

    override fun collectSlowLineMarkers(
        elements: MutableList<out PsiElement>,
        result: MutableCollection<in LineMarkerInfo<*>>
    ) {

        if (elements.isEmpty()) return

        for(leaf in elements){
            ProgressManager.checkCanceled()
            if (leaf !is PsiIdentifier && leaf.firstChild != null) continue
            val element = leaf.parent as? KtNamedDeclaration ?: continue

            val anchor = element.nameIdentifier


            val lineMarkerInfo = anchor?.let {
                InheritanceMergeableLineMarkerInfo(
                    anchor,
                    it.textRange,
                    this.icon,
                    SuperDeclarationMarkerTooltip,
                    SuperDeclarationMarkerNavigationHandler(),
                    GutterIconRenderer.Alignment.RIGHT,
                ){this.name}
            }

            if(lineMarkerInfo != null) result.add(lineMarkerInfo)
        }
    }


}
