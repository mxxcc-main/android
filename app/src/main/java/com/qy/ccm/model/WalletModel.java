package com.qy.ccm.model;

/**
 * Description:
 * Data：2019/2/13-4:52 PM
 *
 */
public class WalletModel {

    public Boolean token(String name) {
        switch (name) {
            case "OMG":
            case "BNB":
            case "MKR":
            case "USDC":
            case "CRO":
            case "TUSD":
            case "ZRX":
            case "ZIL":
            case "BAT":
            case "LINK":
            case "ICX":
            case "PAX":
            case "R":
            case "REP":
            case "AE":
            case "GUSD":
            case "BCZERO":
            case "PPT":
            case "BTM":
                return true;
            default:
                return true;
        }
    }


    public String ethTokeName(String name) {
        switch (name) {
            case "OMG":
            case "BNB":
            case "MKR":
            case "USDC":
            case "CRO":
            case "TUSD":
            case "ZRX":
            case "ZIL":
            case "BAT":
            case "LINK":
            case "ICX":
            case "PAX":
            case "R":
            case "REP":
            case "AE":
            case "GUSD":
            case "BCZERO":
            case "PPT":
            case "BTM":
                return "CCM";
            default:
                return name;
        }
    }

}
