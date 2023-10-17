package com.greensoft.greentranserpnative.ui.print.printManager

import com.greensoft.greentranserpnative.ui.operation.grList.models.PrintStickerModel
import net.posprinter.TSCConst
import net.posprinter.TSCPrinter
import javax.inject.Inject


class PrintManager @Inject constructor(private val tscPrinter: TSCPrinter?) {

    fun printStickers(compName: String?, stickerList: ArrayList<PrintStickerModel>) {
        if(tscPrinter == null) {
            return
        }
        val defaultStartX = 20
        val defaultStartY = 20
        val defaultBarHeight = 2
        val defaultBarWidth = 580
        val defaultBarAndVarGap = 15
        val compNameX = defaultStartX + 10
        val barAfterCompX = defaultStartX - 10
        val barAfterCompY = defaultStartY + 30
        val barCodeX = defaultStartX + 10
        val barCodeY = defaultStartY + 45
        val barCodeHeight = 120
        val barAfterBarCodeX = defaultStartX - 10
        val barAfterBarCodeY = barCodeHeight + 105
        val grY = barAfterBarCodeY + 15
        val dtX = 300
        val barAfterGrX = defaultStartX - 10
        val barAfterGrY = grY + defaultBarAndVarGap + 10
        val originY = barAfterGrY + defaultBarAndVarGap
        val barAfterOrgX = defaultStartX - 10
        val barAfterOrgY = originY + defaultBarAndVarGap + 10
        val destY = barAfterOrgY + defaultBarAndVarGap
        val barAfterDestX = defaultStartX - 10
        val barAfterDestY = destY + defaultBarAndVarGap + 10
        val pckgsY = barAfterDestY + defaultBarAndVarGap
        val weightX = 300
        for (i in stickerList.indices) {
            val model = stickerList[i]
            tscPrinter!!.sizeMm(120.0, 60.0)
                .gapInch(0.0, 0.0)
                .offsetInch(0.0)
                .speed(5.0)
                .density(10)
                .direction(TSCConst.DIRECTION_FORWARD)
                .reference(5, 2)
                .cls()
                .box(4, 6, 568, 390, 3) //                .box(16, 16, 540, 380, 3)
                .text(
                    compNameX,
                    defaultStartY,
                    TSCConst.FNT_16_24,
                    TSCConst.ROTATION_0,
                    1,
                    1,
                    compName
                )
                .bar(barAfterCompX, barAfterCompY, defaultBarWidth, defaultBarHeight)
                .barcode(
                    barCodeX,
                    barCodeY,
                    TSCConst.CODE_TYPE_128,
                    barCodeHeight,
                    TSCConst.ALIGNMENT_CENTER,
                    TSCConst.ROTATION_0,
                    3,
                    3,
                    model.stickerno
                )
                .bar(barAfterBarCodeX, barAfterBarCodeY, defaultBarWidth, defaultBarHeight)
                .text(
                    defaultStartX,
                    grY,
                    TSCConst.FNT_16_24,
                    TSCConst.ROTATION_0,
                    1,
                    1,
                    "GR | " + model.grno
                )
                .text(
                    dtX,
                    grY,
                    TSCConst.FNT_16_24,
                    TSCConst.ROTATION_0,
                    1,
                    1,
                    "DT | " + model.grdt
                )
                .bar(barAfterGrX, barAfterGrY, defaultBarWidth, defaultBarHeight)
                .text(
                    defaultStartX,
                    originY,
                    TSCConst.FNT_16_24,
                    TSCConst.ROTATION_0,
                    1,
                    1,
                    "ORG   | " + model.originname
                )
                .bar(barAfterOrgX, barAfterOrgY, defaultBarWidth, defaultBarHeight)
                .text(
                    defaultStartX,
                    destY,
                    TSCConst.FNT_16_24,
                    TSCConst.ROTATION_0,
                    1,
                    1,
                    "DEST  | " + model.destinationname
                )
                .bar(barAfterDestX, barAfterDestY, defaultBarWidth, defaultBarHeight)
                .text(
                    defaultStartX,
                    pckgsY,
                    TSCConst.FNT_16_24,
                    TSCConst.ROTATION_0,
                    1,
                    1,
                    "PCKGS | " + model.packageid + "/" + model.pckgs
                )
                .text(
                    weightX,
                    pckgsY,
                    TSCConst.FNT_16_24,
                    TSCConst.ROTATION_0,
                    1,
                    1,
                    "WEIGHT | " + model.weight
                ) //                .qrcode(265, 30, TSCConst.EC_LEVEL_H, 4, TSCConst.QRCODE_MODE_MANUAL, TSCConst.ROTATION_0, "test qrcode")
                //                .text(200, 144, TSCConst.FNT_16_24, TSCConst.ROTATION_0, 1, 1, "Test EN")
                //                .text(38, 165, TSCConst.FNT_16_24, TSCConst.ROTATION_0, 1, 2, "HELLO")
                //                .bar(200, 183, 166, 30)
                //                .bar(334, 145, 30, 30)
                .print(1)
        }
    }
}