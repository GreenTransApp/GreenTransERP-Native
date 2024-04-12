package com.greensoft.greentranserpnative.ui.operation.pod_entry.models

data class RelationListModel(
    val commandmessage: Any?,
    val commandstatus: Int?,
    val relations: String?
)
{
    override fun toString(): String {
        return relations.toString()
    }
}