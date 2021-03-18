package ua.com.pandasushi.controller.kitchen;

import ua.com.pandasushi.database.common.menu.INGREDIENTS;
import ua.com.pandasushi.database.common.menu.PRODUCTS_INGREDIENTS;
import ua.com.pandasushi.main.GlobalPandaApp;

import java.util.ArrayList;

public class CustomInventory {
    private static int[] ingIds = new int[] {
            1007,1041,1061,1082,1015,1004,1074,1153,1160,
            1121,1051,1088,1135,1048,1017,1040,1138,1137,
            1119,1120,1118,1155,1081,1157,1019,1016,1022,
            1059,1014,1117,1116,1162,1114,1113,1055,1013,
            1009,1025,1018,1039,1127,1086,1033,1140,1148,
            1054,1165,1072,1038,1133,1002,1006,1042,1035,
            1102,1083,1028,1092,1068,1154,1084,1010,1161,
            1008,1078,1027,1159,1107,1058,1080,1049,1104,
            1108,1129,1046,1032,1003,1164,1021,1131,1053,
            1047,1034,1005,1152,1050,1151,1128,1166,1029,
            1150,1103,1145,1079,1156,1158,1064,1132,1012,
            1167,1144,1069,1026,1011,1126,1057,1077,1110,
            1020,1163,1134,1101
    };

    public static void printIngsWithProducts() {
        ArrayList<INGREDIENTS> ings = GlobalPandaApp.site.getIngredients();
        ArrayList<PRODUCTS_INGREDIENTS> prodIngs = GlobalPandaApp.site.getProdIng();
        for (int i : ingIds) {
            for (INGREDIENTS ing : ings) {
                if (ing.getIngredientId() == i) {
                    System.out.println(ing.getIngredientId() + "|" + ing.getIngredientName());
                    for (PRODUCTS_INGREDIENTS prodIng : prodIngs) {
                        if (prodIng.getIngredientId() == i) {
                            System.out.println(prodIng.getProductId() + "|" + prodIng.getProductName());
                        }
                    }
                    System.out.println("--------------------------------");
                    break;
                }
            }
        }
    }
}
