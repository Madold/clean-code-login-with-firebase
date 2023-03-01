package com.markusw.loginwithfirebase.core.utils

import android.content.Context
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.markusw.loginwithfirebase.domain.DialogInfo

object DialogBuilder {
    fun buildDialog(
        context: Context,
        dialogInfo: DialogInfo,
        onPositiveButtonClicked: (DialogInterface, Int) -> Unit,
        onNegativeButtonClicked: (DialogInterface, Int) -> Unit = { _, _ ->}
    ): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(context).apply {
            setTitle(dialogInfo.title)
            setMessage(dialogInfo.message)
            setPositiveButton(dialogInfo.positiveButtonText) { dialog, which ->
                onPositiveButtonClicked(dialog, which)
            }
            setNegativeButton(dialogInfo.negativeButtonText) { dialog, which->
                onNegativeButtonClicked(dialog, which)
            }
        }
    }
}