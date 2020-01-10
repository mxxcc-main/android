package com.qy.ccm.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Data：2019/5/8-11:35 AM
 *
 * @author
 */
public class EthConfig {
    /**
     * OMG
     */
    public static final String OMG_CONTRACT = "0xd26114cd6EE289AccF82350c8d8487fedB8A0C07";

    /**
     * BNB
     */
    public static final String BNB_CONTRACT = "0xB8c77482e45F1F44dE1745F52C74426C631bDD52";

    /**
     * MKR
     */
    public static final String MKR_CONTRACT = "0x9f8f72aa9304c8b593d555f12ef6589cc3a579a2";

    /**
     * USDC
     */
    public static final String USDC_CONTRACT = "0xa0b86991c6218b36c1d19d4a2e9eb0ce3606eb48";

    /**
     * CRO
     */
    public static final String CRO_CONTRACT = "0xa0b73e1ff0b80914ab6fe0444e65848c4c34450b";
//    SAR
//
//
//    SAR-ASI
//
//
//    ISR-S
//
//
//    CCM-S
//
//
//    SAR-ES

    /**
     * SAR
     */
    public static final String SAR_CONTRACT = "0x024a6fc246df1691575e82e8c7e64a58cf9d335a";
    /**
     * SAR_ASI
     */
    public static final String SAR_ASI_CONTRACT = "0x9697551049f2586ccb772d3e1596cc01300b5990";
    /**
     * CCM_ISR
     */
    public static final String CCM_ISR_CONTRACT = "0x622ab21b14f66b6f0e4f06243d3dfbbe3bc43ef6";
    /**
     * SAR_SAN
     */
    public static final String SAR_SAN_CONTRACT = "0xa191a35525a577d00d10cfdff01a160b6167d737";
    /**
     * HKDD
     */
    public static final String HKDD_CONTRACT = "0x41858f0e97fa8f3dce7e95299b488eaade431a67";
    /**
     * SAR_S
     */
    public static final String SAR_S_CONTRACT = "0x9ff42f595acc7ccb3a20d7b7a1b3242f85731d77";
    /**
     * ISR_S
     */
    public static final String ISR_S_CONTRACT = "0xfede366c8379b72e8f84217f44e975946bca83d1";
    /**
     * CCM_S
     */
    public static final String CCM_S_CONTRACT = "0x2ac499e2a03f27774e5f6833066549f4cd1bddf5";
    /**
     * SAR_ES
     */
    public static final String SAR_ES_CONTRACT = "0x38127df1b09566f79434affe1ecd4b0b05462657";
    /**
     * HKDD_S
     */
    public static final String HKDD_S_CONTRACT = "0x6188cd77bfdcb2a16ca1bd34f45319fdcc8539cb";


    /**
     * SAN-0301
     */
    public static final String SAN_0301_CONTRACT = "0x12bbe6e6648788075f9fab486140b8ddf2f7474d";
    /**
     * SAN-0306
     */
    public static final String SAN_0306_CONTRACT = "0x01acfc3396f1ec3b9dbf9021ada9a90cd057164a";
    /**
     * SAN-0502
     */
    public static final String SAN_0502_CONTRACT = "0xe6cbf336a90d0897cabd9d5f12e5ac363e38b143";
    /**
     * SAN-3105
     */
    public static final String SAN_3105_CONTRACT = "0x77974a15c70e4f7e2cf8d5f237a856f6c083961d";
    /**
     * SAN-3301
     */
    public static final String SAN_3301_CONTRACT = "0x270ab9d656bdd7d6a199322d84ee9caaa14639bd";
    /**
     * SAN-2101
     */
    public static final String SAN_2101_CONTRACT = "0xd6aade24afe7bfd3ed48aced6f946f0f8404d3f0";
    /**
     * SAN-2105
     */
    public static final String SAN_2105_CONTRACT = "0x111bd36af68ba3aed08b7d6672c695158245619c";
    /**
     * SAN-2406
     */
    public static final String SAN_2406_CONTRACT = "0x199844ef9bebf84b2569ff6d33330c5b170085ef";
    /**
     * SAN-410101
     */
//    public static final String SAN_410101_CONTRACT = "0x1dcea3bf721d59eaf14a5ee59208fcbc7fc1685f";
    public static final String SAN_410101_CONTRACT = "0x35c528053e91fed199f887cdfbb06efb613748fe";
    public static final String SAN_2906_CONTRACT = "0xd0f878d97a8e13823f351482f2f4ad77a2a6d77e";
    public static final String SAN_3201_CONTRACT = "0x84270ad6f21784dfdd2d70e1b31a1cf31e61dcf8";
    public static final String SAN_1606_CONTRACT = "0x4136baf9ef6ee9a46e0bba5e8163c7f9ccf451e3";
    public static final String SAN_2901_CONTRACT = "0xc2d302f5b70d329130f21377cf4cf7f6a9108142";


//    2109  12    13   add

    public static final String SAN_410101S_CONTRACT = "0x4cf988c8110ede6b9f2115d3a3a2a7f06f11358f";
    //    public static final String SAN_1106_CONTRACT = "0x66084e616b8b1efa198f488f31cd9c586b321d5f";
//    public static final String SAN_1104_CONTRACT = "0x340945dd1890c99b24a2424594f2bc8f3af6739c";
    public static final String SAN_1106_CONTRACT = "0x5e14ed237ad7f6fb095669548a5c2d36d9175a86";
    public static final String SAN_1104_CONTRACT = "0xba093ffba37c9d2dc323680c05718e23e3426fbd";


    public static final String SAN_1802_CONTRACT = "0x26c941eea2a0a48479142cb9009acefa2920c8e9";
    public static final String SAN_410102_CONTRACT = "0x61d70851f3046434f50f356011b80a30df634620";

    public static final Map<String, String> map = new HashMap<>();

    public EthConfig() {

        map.put("SAR", SAR_CONTRACT);
        map.put("SAR_ASI", SAR_ASI_CONTRACT);
        map.put("CCM_ISR", CCM_ISR_CONTRACT);
        map.put("SAR_SAN", SAR_SAN_CONTRACT);
        map.put("HKDD", HKDD_CONTRACT);
        map.put("SAR_S", SAR_S_CONTRACT);
        map.put("ISR_S", ISR_S_CONTRACT);
        map.put("CCM_S", CCM_S_CONTRACT);
        map.put("SAR_ES", SAR_ES_CONTRACT);
        map.put("HKDD_S", HKDD_S_CONTRACT);
//        =====
        map.put("SAN-0301", SAN_0301_CONTRACT);
        map.put("SAN-0306", SAN_0306_CONTRACT);
        map.put("SAN-0502", SAN_0502_CONTRACT);
        map.put("SAN-3105", SAN_3105_CONTRACT);
        map.put("SAN-3301", SAN_3301_CONTRACT);
        map.put("SAN-2101", SAN_2101_CONTRACT);
        map.put("SAN-2105", SAN_2105_CONTRACT);
        map.put("SAN-2406", SAN_2406_CONTRACT);
        map.put("SAN-410101", SAN_410101_CONTRACT);
        map.put("SAN-2906", SAN_2906_CONTRACT);
        map.put("SAN-3201", SAN_3201_CONTRACT);
        map.put("SAN-1606", SAN_1606_CONTRACT);
        map.put("SAN-2901", SAN_2901_CONTRACT);


        map.put("SAN-410101S", SAN_410101S_CONTRACT);
        map.put("SAN-1106", SAN_1106_CONTRACT);
        map.put("SAN-1104", SAN_1104_CONTRACT);

        map.put("SAN-1802", SAN_1802_CONTRACT);
        map.put("SAN-410102", SAN_410102_CONTRACT);

    }

}
