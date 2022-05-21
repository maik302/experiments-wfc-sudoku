package logic

import data.Node

interface WFCLogic {
    fun run()
    fun <T: Node> T.collapse()
}