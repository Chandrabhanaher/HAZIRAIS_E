package com.example.ugcch.hazira;

public class Items {
    private String BRCHCODE;
    private String ITEMCODE;
    private String UNIT;
    private String STOCK;
    private String SEAM_NUMBER_1;
    private String LOCATION;
    private String TOKEN_NUMBER;
    private String ITEM_SIZE;
    private String AWS_CLASS;
    private String LT_LOT_NO;
    private String SUPP_BATCH_NO;

    private boolean isSelected;
    private String text;
    private String postion;
    private String TO_STOCK;


    public void setBRCHCODE(String BRCHCODE) {
        this.BRCHCODE = BRCHCODE;
    }

    public void setITEMCODE(String ITEMCODE) {
        this.ITEMCODE = ITEMCODE;
    }

    public void setUNIT(String UNIT) {
        this.UNIT = UNIT;
    }

    public void setSTOCK(String STOCK) {
        this.STOCK = STOCK;
    }

    public void setSEAM_NUMBER_1(String SEAM_NUMBER_1) {
        this.SEAM_NUMBER_1 = SEAM_NUMBER_1;
    }

    public void setLOCATION(String LOCATION) {
        this.LOCATION = LOCATION;
    }

    public void setTOKEN_NUMBER(String TOKEN_NUMBER) {
        this.TOKEN_NUMBER = TOKEN_NUMBER;
    }

    public void setITEM_SIZE(String ITEM_SIZE) {
        this.ITEM_SIZE = ITEM_SIZE;
    }

    public void setAWS_CLASS(String AWS_CLASS) {
        this.AWS_CLASS = AWS_CLASS;
    }

    public String getBRCHCODE() {
        return BRCHCODE;
    }

    public String getITEMCODE() {
        return ITEMCODE;
    }

    public String getUNIT() {
        return UNIT;
    }

    public String getSTOCK() {
        return STOCK;
    }

    public String getTOKEN_NUMBER() {
        return TOKEN_NUMBER;
    }

    public String getAWS_CLASS() {
        return AWS_CLASS;
    }

    public String getLOCATION() {
        return LOCATION;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getSEAM_NUMBER_1() {
        return SEAM_NUMBER_1;
    }

    public String getITEM_SIZE() {
        return ITEM_SIZE;
    }

    public boolean setSelected(boolean selected) {
        isSelected = selected;
        return selected;
    }

    public void setPostion(String postion) {
        this.postion = postion;
    }

    public String getPostion() {
        return postion;
    }

    public void setLT_LOT_NO(String LT_LOT_NO) {
        this.LT_LOT_NO = LT_LOT_NO;
    }

    public void setSUPP_BATCH_NO(String SUPP_BATCH_NO) {
        this.SUPP_BATCH_NO = SUPP_BATCH_NO;
    }

    public String getSUPP_BATCH_NO() {
        return SUPP_BATCH_NO;
    }

    public String getLT_LOT_NO() {
        return LT_LOT_NO;
    }

    public void setTO_STOCK(String TO_STOCK) {
        this.TO_STOCK = TO_STOCK;
    }

    public String getTO_STOCK() {
        return TO_STOCK;
    }
}
