package com.qy.ccm.bean.wallet;

import com.qy.ccm.bean.base.BaseBean;

import java.util.List;

/**
 * Description:
 * Data：2019/1/9-5:03 PM
 *
 */
public class UsdtDetailsBean extends BaseBean {

    private String address;
    private List<BalanceBean> balance;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<BalanceBean> getBalance() {
        return balance;
    }

    public void setBalance(List<BalanceBean> balance) {
        this.balance = balance;
    }

    public static class BalanceBean {
        /**
         * divisible : true
         * frozen : 0
         * id : 31
         * pendingneg : 0
         * pendingpos : 0
         * propertyinfo : {"amount":"0.00000000","block":324140,"blockhash":"00000000000000001e76250b3725547b5887329cfe3a8bb930a70e66747384d3","blocktime":1412613555,"category":"Financial and insurance activities","confirmations":186115,"creationtxid":"5ed3694e8a4fa8d3ec5c75eb6789492c69e65511522b220e94ab51da2b6dd53f","data":"The next paradigm of money.","divisible":true,"ecosystem":"main","fee":"0.00010000","fixedissuance":false,"flags":{},"freezingenabled":true,"ismine":false,"issuer":"3MbYQMMmSkC3AgWkj9FMo5LsPTW1zBTwXL","managedissuance":true,"name":"TetherUS","positioninblock":767,"propertyid":31,"propertyname":"TetherUS","propertytype":"divisible","rdata":null,"registered":false,"sendingaddress":"3MbYQMMmSkC3AgWkj9FMo5LsPTW1zBTwXL","subcategory":"Activities auxiliary to financial service and insurance activities","totaltokens":"2520000000.00000000","txid":"5ed3694e8a4fa8d3ec5c75eb6789492c69e65511522b220e94ab51da2b6dd53f","type":"Create Property - Manual","type_int":54,"url":"https://tether.to","valid":true,"version":0}
         * reserved : 0
         * symbol : SP31
         * value : 4700000000
         * error : false
         */

        private boolean divisible;
        private String frozen;
        private String id;
        private String pendingneg;
        private String pendingpos;
        private PropertyinfoBean propertyinfo;
        private String reserved;
        private String symbol;
        private String value;
        private boolean error;

        public boolean isDivisible() {
            return divisible;
        }

        public void setDivisible(boolean divisible) {
            this.divisible = divisible;
        }

        public String getFrozen() {
            return frozen;
        }

        public void setFrozen(String frozen) {
            this.frozen = frozen;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPendingneg() {
            return pendingneg;
        }

        public void setPendingneg(String pendingneg) {
            this.pendingneg = pendingneg;
        }

        public String getPendingpos() {
            return pendingpos;
        }

        public void setPendingpos(String pendingpos) {
            this.pendingpos = pendingpos;
        }

        public PropertyinfoBean getPropertyinfo() {
            return propertyinfo;
        }

        public void setPropertyinfo(PropertyinfoBean propertyinfo) {
            this.propertyinfo = propertyinfo;
        }

        public String getReserved() {
            return reserved;
        }

        public void setReserved(String reserved) {
            this.reserved = reserved;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public boolean isError() {
            return error;
        }

        public void setError(boolean error) {
            this.error = error;
        }

        public static class PropertyinfoBean {
            /**
             * amount : 0.00000000
             * block : 324140
             * blockhash : 00000000000000001e76250b3725547b5887329cfe3a8bb930a70e66747384d3
             * blocktime : 1412613555
             * category : Financial and insurance activities
             * confirmations : 186115
             * creationtxid : 5ed3694e8a4fa8d3ec5c75eb6789492c69e65511522b220e94ab51da2b6dd53f
             * data : The next paradigm of money.
             * divisible : true
             * ecosystem : main
             * fee : 0.00010000
             * fixedissuance : false
             * flags : {}
             * freezingenabled : true
             * ismine : false
             * issuer : 3MbYQMMmSkC3AgWkj9FMo5LsPTW1zBTwXL
             * managedissuance : true
             * name : TetherUS
             * positioninblock : 767
             * propertyid : 31
             * propertyname : TetherUS
             * propertytype : divisible
             * rdata : null
             * registered : false
             * sendingaddress : 3MbYQMMmSkC3AgWkj9FMo5LsPTW1zBTwXL
             * subcategory : Activities auxiliary to financial service and insurance activities
             * totaltokens : 2520000000.00000000
             * txid : 5ed3694e8a4fa8d3ec5c75eb6789492c69e65511522b220e94ab51da2b6dd53f
             * type : Create Property - Manual
             * type_int : 54
             * url : https://tether.to
             * valid : true
             * version : 0
             */

            private String amount;
            private int block;
            private String blockhash;
            private int blocktime;
            private String category;
            private int confirmations;
            private String creationtxid;
            private String data;
            private boolean divisible;
            private String ecosystem;
            private String fee;
            private boolean fixedissuance;
            private FlagsBean flags;
            private boolean freezingenabled;
            private boolean ismine;
            private String issuer;
            private boolean managedissuance;
            private String name;
            private int positioninblock;
            private int propertyid;
            private String propertyname;
            private String propertytype;
            private Object rdata;
            private boolean registered;
            private String sendingaddress;
            private String subcategory;
            private String totaltokens;
            private String txid;
            private String type;
            private int type_int;
            private String url;
            private boolean valid;
            private int version;

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public int getBlock() {
                return block;
            }

            public void setBlock(int block) {
                this.block = block;
            }

            public String getBlockhash() {
                return blockhash;
            }

            public void setBlockhash(String blockhash) {
                this.blockhash = blockhash;
            }

            public int getBlocktime() {
                return blocktime;
            }

            public void setBlocktime(int blocktime) {
                this.blocktime = blocktime;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public int getConfirmations() {
                return confirmations;
            }

            public void setConfirmations(int confirmations) {
                this.confirmations = confirmations;
            }

            public String getCreationtxid() {
                return creationtxid;
            }

            public void setCreationtxid(String creationtxid) {
                this.creationtxid = creationtxid;
            }

            public String getData() {
                return data;
            }

            public void setData(String data) {
                this.data = data;
            }

            public boolean isDivisible() {
                return divisible;
            }

            public void setDivisible(boolean divisible) {
                this.divisible = divisible;
            }

            public String getEcosystem() {
                return ecosystem;
            }

            public void setEcosystem(String ecosystem) {
                this.ecosystem = ecosystem;
            }

            public String getFee() {
                return fee;
            }

            public void setFee(String fee) {
                this.fee = fee;
            }

            public boolean isFixedissuance() {
                return fixedissuance;
            }

            public void setFixedissuance(boolean fixedissuance) {
                this.fixedissuance = fixedissuance;
            }

            public FlagsBean getFlags() {
                return flags;
            }

            public void setFlags(FlagsBean flags) {
                this.flags = flags;
            }

            public boolean isFreezingenabled() {
                return freezingenabled;
            }

            public void setFreezingenabled(boolean freezingenabled) {
                this.freezingenabled = freezingenabled;
            }

            public boolean isIsmine() {
                return ismine;
            }

            public void setIsmine(boolean ismine) {
                this.ismine = ismine;
            }

            public String getIssuer() {
                return issuer;
            }

            public void setIssuer(String issuer) {
                this.issuer = issuer;
            }

            public boolean isManagedissuance() {
                return managedissuance;
            }

            public void setManagedissuance(boolean managedissuance) {
                this.managedissuance = managedissuance;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getPositioninblock() {
                return positioninblock;
            }

            public void setPositioninblock(int positioninblock) {
                this.positioninblock = positioninblock;
            }

            public int getPropertyid() {
                return propertyid;
            }

            public void setPropertyid(int propertyid) {
                this.propertyid = propertyid;
            }

            public String getPropertyname() {
                return propertyname;
            }

            public void setPropertyname(String propertyname) {
                this.propertyname = propertyname;
            }

            public String getPropertytype() {
                return propertytype;
            }

            public void setPropertytype(String propertytype) {
                this.propertytype = propertytype;
            }

            public Object getRdata() {
                return rdata;
            }

            public void setRdata(Object rdata) {
                this.rdata = rdata;
            }

            public boolean isRegistered() {
                return registered;
            }

            public void setRegistered(boolean registered) {
                this.registered = registered;
            }

            public String getSendingaddress() {
                return sendingaddress;
            }

            public void setSendingaddress(String sendingaddress) {
                this.sendingaddress = sendingaddress;
            }

            public String getSubcategory() {
                return subcategory;
            }

            public void setSubcategory(String subcategory) {
                this.subcategory = subcategory;
            }

            public String getTotaltokens() {
                return totaltokens;
            }

            public void setTotaltokens(String totaltokens) {
                this.totaltokens = totaltokens;
            }

            public String getTxid() {
                return txid;
            }

            public void setTxid(String txid) {
                this.txid = txid;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getType_int() {
                return type_int;
            }

            public void setType_int(int type_int) {
                this.type_int = type_int;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public boolean isValid() {
                return valid;
            }

            public void setValid(boolean valid) {
                this.valid = valid;
            }

            public int getVersion() {
                return version;
            }

            public void setVersion(int version) {
                this.version = version;
            }

            public static class FlagsBean {
            }
        }
    }
}
