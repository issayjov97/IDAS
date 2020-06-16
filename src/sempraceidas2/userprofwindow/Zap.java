/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sempraceidas2.userprofwindow;

import java.util.Arrays;

class M {

    public static void main(String[] args) {
        System.out.println(Arrays.toString(Zap.values()));
    }

}

public enum Zap {

    SPLNEN("SP", "Splnen"), NESPLNEN("NSP", "Nesplnen");

    private String code;
    private String text;

    private Zap(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public static Zap getByCode(String genderCode) {
        for (Zap g : Zap.values()) {
            if (g.code.equals(genderCode)) {
                return g;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.text;
    }

}
