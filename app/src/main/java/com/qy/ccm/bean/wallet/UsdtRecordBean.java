package com.qy.ccm.bean.wallet;


import com.qy.ccm.bean.base.BaseBean;

import java.util.List;

/**
 * usdt交易记录
 */
public class UsdtRecordBean extends BaseBean {


    /**
     * address : 1FpBSz8wLQJfJ1ew3x7UrdCbgGgSNJ7bHW
     * current_page : 0
     * pages : 2
     * transactions : [{"amount":"30.00000000","block":559814,"blockhash":"0000000000000000002453093d75a9e906bfbe67ae87b058e97f294076f1b48a","blocktime":1548286972,"confirmations":2975,"divisible":true,"fee":"0.00001263","flags":null,"ismine":false,"positioninblock":355,"propertyid":31,"propertyname":"TetherUS","referenceaddress":"1dnxkgnizR4dAHLGtqJ8gtDYpP2ThPwWw","sendingaddress":"1FpBSz8wLQJfJ1ew3x7UrdCbgGgSNJ7bHW","txid":"038a6301cf79a9d4369ca70f754a960d1ec94a549556ca156ef61355add69236","type":"Simple Send","type_int":0,"valid":true,"version":0},{"amount":"1.00000000","block":559411,"blockhash":"0000000000000000000cddeb7a38abcba7bff08200b4127cbf37df1af958cbea","blocktime":1548044945,"confirmations":3378,"divisible":true,"fee":"0.00000468","flags":null,"ismine":false,"positioninblock":1068,"propertyid":31,"propertyname":"TetherUS","referenceaddress":"1FpBSz8wLQJfJ1ew3x7UrdCbgGgSNJ7bHW","sendingaddress":"1FpBSz8wLQJfJ1ew3x7UrdCbgGgSNJ7bHW","txid":"8c66002f15ab125729783a68e9c724afeac0915a28483e4af6591860f758623f","type":"Simple Send","type_int":0,"valid":true,"version":0},{"amount":"2.00000000","block":557406,"blockhash":"0000000000000000000f67beae92426d09d18c0e7e40cd22d6b5749c63d2537d","blocktime":1546841653,"confirmations":5383,"divisible":true,"fee":"0.00000900","flags":null,"ismine":false,"positioninblock":1021,"propertyid":31,"propertyname":"TetherUS","referenceaddress":"1KayP7ehgYMQgNLon91xbfH2ncjMHGKVCP","sendingaddress":"1FpBSz8wLQJfJ1ew3x7UrdCbgGgSNJ7bHW","txid":"c690d0f3cb918f68ec1987a53d6dc2ae93526fe9285787ebdc0a9dba807b17b3","type":"Simple Send","type_int":0,"valid":true,"version":0},{"amount":"2.00000000","block":557395,"blockhash":"0000000000000000001799c701c3f73e68f97a617775d2374d319d73ea48b291","blocktime":1546833209,"confirmations":5394,"divisible":true,"fee":"0.00000900","flags":{"registered":true},"invalidreason":"Sender has insufficient balance","ismine":false,"positioninblock":478,"propertyid":1,"propertyname":"Omni","referenceaddress":"1KayP7ehgYMQgNLon91xbfH2ncjMHGKVCP","sendingaddress":"1FpBSz8wLQJfJ1ew3x7UrdCbgGgSNJ7bHW","txid":"c8e4bb8338d9b539809c31cd95cc0868826fff82a170c312e8422525258feab3","type":"Simple Send","type_int":0,"valid":false,"version":0},{"amount":"2.00000000","block":557394,"blockhash":"000000000000000000027b1934eae1aeaacb644bc1adf622a5e347391ae974d6","blocktime":1546832938,"confirmations":5395,"divisible":true,"fee":"0.00000900","flags":{"registered":true},"invalidreason":"Sender has insufficient balance","ismine":false,"positioninblock":1342,"propertyid":1,"propertyname":"Omni","referenceaddress":"1KayP7ehgYMQgNLon91xbfH2ncjMHGKVCP","sendingaddress":"1FpBSz8wLQJfJ1ew3x7UrdCbgGgSNJ7bHW","txid":"ca02a88df7f9a98f21ca35679ed2c649e9680e0b2beefd01c011510bda3dffe5","type":"Simple Send","type_int":0,"valid":false,"version":0},{"amount":"0.20000000","block":557389,"blockhash":"0000000000000000002651c4f9f4908c3ce93838631f33e7f756cf8f9f8f6b1c","blocktime":1546829847,"confirmations":5400,"divisible":true,"fee":"0.00000900","flags":{"registered":true},"invalidreason":"Sender has insufficient balance","ismine":false,"positioninblock":1614,"propertyid":1,"propertyname":"Omni","referenceaddress":"1KayP7ehgYMQgNLon91xbfH2ncjMHGKVCP","sendingaddress":"1FpBSz8wLQJfJ1ew3x7UrdCbgGgSNJ7bHW","txid":"7db34c6c1180015a8a0771c190dce72916a56040441ed600250c4f73bb1eb7e0","type":"Simple Send","type_int":0,"valid":false,"version":0},{"amount":"0.10000000","block":557388,"blockhash":"00000000000000000024bcb3ed9590c78a91bf3d021ae21ba9547b1e3bb8894e","blocktime":1546829169,"confirmations":5401,"divisible":true,"fee":"0.00000600","flags":{"registered":true},"invalidreason":"Sender has insufficient balance","ismine":false,"positioninblock":1752,"propertyid":1,"propertyname":"Omni","referenceaddress":"1KayP7ehgYMQgNLon91xbfH2ncjMHGKVCP","sendingaddress":"1FpBSz8wLQJfJ1ew3x7UrdCbgGgSNJ7bHW","txid":"619839a0b8d99fe1037d6a7cbe5fd8c51f192e12bf1e1c79886f1ec6634affce","type":"Simple Send","type_int":0,"valid":false,"version":0},{"amount":"0.10000000","block":557387,"blockhash":"0000000000000000000f6260472aadad171455a623983412284b00678fa71317","blocktime":1546828428,"confirmations":5402,"divisible":true,"fee":"0.00000600","flags":{"registered":true},"invalidreason":"Sender has insufficient balance","ismine":false,"positioninblock":1073,"propertyid":1,"propertyname":"Omni","referenceaddress":"1FpBSz8wLQJfJ1ew3x7UrdCbgGgSNJ7bHW","sendingaddress":"1FpBSz8wLQJfJ1ew3x7UrdCbgGgSNJ7bHW","txid":"8dbc2b744ea7e0a6726f7f3c3bcb2e7ea352e906d800a34e04e059297e6da42d","type":"Simple Send","type_int":0,"valid":false,"version":0},{"amount":"1.00000000","block":557387,"blockhash":"0000000000000000000f6260472aadad171455a623983412284b00678fa71317","blocktime":1546828428,"confirmations":5402,"divisible":true,"fee":"0.00000665","flags":null,"ismine":false,"positioninblock":1072,"propertyid":31,"propertyname":"TetherUS","referenceaddress":"1KayP7ehgYMQgNLon91xbfH2ncjMHGKVCP","sendingaddress":"1FpBSz8wLQJfJ1ew3x7UrdCbgGgSNJ7bHW","txid":"f92fd7d4c5e2814f52ed3bfa08d2bf90a186dbe2d867dc8347a350f75db1e9ed","type":"Simple Send","type_int":0,"valid":true,"version":0},{"amount":"0.10000000","block":557383,"blockhash":"00000000000000000002e528ba11c6a8945619b0fc35dbecad03949ba8e77094","blocktime":1546825409,"confirmations":5406,"divisible":true,"fee":"0.00000600","flags":{"registered":true},"invalidreason":"Sender has insufficient balance","ismine":false,"positioninblock":1068,"propertyid":1,"propertyname":"Omni","referenceaddress":"1FpBSz8wLQJfJ1ew3x7UrdCbgGgSNJ7bHW","sendingaddress":"1FpBSz8wLQJfJ1ew3x7UrdCbgGgSNJ7bHW","txid":"ddc23c8faaf0885ad1669c5ee92c9e138c170667337eea1f75f35691113e6487","type":"Simple Send","type_int":0,"valid":false,"version":0}]
     */

    private String address;
    private int current_page;
    private int pages;

    private List<TransactionsBean> transactions;
    private List<BalanceBean> balance;

    public List<BalanceBean> getBalance() {
        return balance;
    }

    public void setBalance(List<BalanceBean> balance) {
        this.balance = balance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<TransactionsBean> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionsBean> transactions) {
        this.transactions = transactions;
    }

    public static class TransactionsBean {
        /**
         * amount : 30.00000000
         * block : 559814
         * blockhash : 0000000000000000002453093d75a9e906bfbe67ae87b058e97f294076f1b48a
         * blocktime : 1548286972
         * confirmations : 2975
         * divisible : true
         * fee : 0.00001263
         * flags : null
         * ismine : false
         * positioninblock : 355
         * propertyid : 31
         * propertyname : TetherUS
         * referenceaddress : 1dnxkgnizR4dAHLGtqJ8gtDYpP2ThPwWw
         * sendingaddress : 1FpBSz8wLQJfJ1ew3x7UrdCbgGgSNJ7bHW
         * txid : 038a6301cf79a9d4369ca70f754a960d1ec94a549556ca156ef61355add69236
         * type : Simple Send
         * type_int : 0
         * valid : true
         * version : 0
         * invalidreason : Sender has insufficient balance
         */

        private String amount;
        private int block;
        private String blockhash;
        private int blocktime;
        private int confirmations;
        private boolean divisible;
        private String fee;
        private Object flags;
        private boolean ismine;
        private int positioninblock;
        private int propertyid;
        private String propertyname;
        private String referenceaddress;
        private String sendingaddress;
        private String txid;
        private String type;
        private int type_int;
        private boolean valid;
        private int version;
        private String invalidreason;

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

        public int getConfirmations() {
            return confirmations;
        }

        public void setConfirmations(int confirmations) {
            this.confirmations = confirmations;
        }

        public boolean isDivisible() {
            return divisible;
        }

        public void setDivisible(boolean divisible) {
            this.divisible = divisible;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public Object getFlags() {
            return flags;
        }

        public void setFlags(Object flags) {
            this.flags = flags;
        }

        public boolean isIsmine() {
            return ismine;
        }

        public void setIsmine(boolean ismine) {
            this.ismine = ismine;
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

        public String getReferenceaddress() {
            return referenceaddress;
        }

        public void setReferenceaddress(String referenceaddress) {
            this.referenceaddress = referenceaddress;
        }

        public String getSendingaddress() {
            return sendingaddress;
        }

        public void setSendingaddress(String sendingaddress) {
            this.sendingaddress = sendingaddress;
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

        public String getInvalidreason() {
            return invalidreason;
        }

        public void setInvalidreason(String invalidreason) {
            this.invalidreason = invalidreason;
        }
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
         * value : 1700000000
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
