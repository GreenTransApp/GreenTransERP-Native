package com.greensoft.greentranserpnative.ui.operation.multiple_pod_entry_list.models

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