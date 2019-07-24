package com.example.ugcch.hazira;

class Item_list {


    private String SRNO;
    private String ITEMCODE;
    private String QTY;
    private String DOCNO;
    private String BOOKCODE;
    private String YYYYMM;
    private String DOCDATE;
    private String IMPORT_DOCNO;
    private String LOT_NO;

    public void setSRNO(String SRNO) {
        this.SRNO = SRNO;
    }

    public void setITEMCODE(String ITEMCODE) {
        this.ITEMCODE = ITEMCODE;
    }

    public void setADJ_QTY(String QTY) {
        this.QTY = QTY;
    }

    public void setDOCNO(String DOCNO) {
        this.DOCNO = DOCNO;
    }

    public void setBOOKCODE(String BOOKCODE) {
        this.BOOKCODE = BOOKCODE;
    }

    public void setYYYYMM(String YYYYMM) {
        this.YYYYMM = YYYYMM;
    }

    public String getSRNO() {
        return SRNO;
    }

    public String getITEMCODE() {
        return ITEMCODE;
    }

    public String getADJ_QTY() {
        return QTY;
    }

    public String getDOCNO() {
        return DOCNO;
    }

    public String getBOOKCODE() {
        return BOOKCODE;
    }

    public String getYYYYMM() {
        return YYYYMM;
    }

    public void setDOCDATE(String DOCDATE) {
        this.DOCDATE = DOCDATE;
    }

    public String getDOCDATE() {
        return DOCDATE;
    }

    public void setIMPORT_DOCNO(String IMPORT_DOCNO) {
        this.IMPORT_DOCNO = IMPORT_DOCNO;
    }

    public String getIMPORT_DOCNO() {
        return IMPORT_DOCNO;
    }

    public void setLOT_NO(String LOT_NO) {
        this.LOT_NO = LOT_NO;
    }
}
